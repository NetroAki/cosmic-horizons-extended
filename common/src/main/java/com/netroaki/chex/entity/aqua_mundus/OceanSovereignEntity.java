package com.netroaki.chex.entity.aqua_mundus;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class OceanSovereignEntity extends AquaticEntity implements PowerableMob {
  // Multi-head management
  public final SovereignPart[] subEntities;
  public final SovereignPart head;
  public final SovereignPart tail;
  public final SovereignPart[] heads;

  // Boss bar and phase tracking
  private final BossEvent bossEvent =
      (BossEvent)
          (new BossEvent(
              this.getDisplayName(),
              BossEvent.BossBarColor.BLUE,
              BossEvent.BossBarOverlay.PROGRESS));
  private int phase = 0;
  private int attackCooldown = 0;
  private int phaseTransitionTicks = 0;
  private int whirlpoolCooldown = 0;
  private int sonicBoomCooldown = 0;

  // Animation states
  private float bodyYRot;
  private float bodyXRot;
  private float prevBodyYRot;
  private float prevBodyXRot;
  private float tentacleMovement;
  private float prevTentacleMovement;

  // Attack states
  private boolean isPerformingSonicAttack = false;
  private int sonicAttackAnimation = 0;
  private int prevSonicAttackAnimation = 0;

  private boolean isPerformingWhirlpool = false;
  private int whirlpoolAnimation = 0;
  private int prevWhirlpoolAnimation = 0;
  private BlockPos whirlpoolCenter;

  // Attack patterns
  private static final int MAX_HEADS = 3;
  private static final int PHASE_TRANSITION_DURATION = 100;
  private static final int WHIRLPOOL_COOLDOWN = 200;
  private static final int SONIC_BOOM_COOLDOWN = 150;

  public OceanSovereignEntity(EntityType<? extends OceanSovereignEntity> type, Level level) {
    super(type, level);
    this.noCulling = true;
    this.moveControl = new OceanSovereignMoveControl(this);
    this.lookControl = new OceanSovereignLookControl(this);

    // Initialize parts
    this.head = new SovereignPart(this, "head", 2.0F, 2.0F);
    this.tail = new SovereignPart(this, "tail", 1.5F, 1.5F);

    // Initialize multiple heads
    this.heads = new SovereignPart[MAX_HEADS];
    for (int i = 0; i < MAX_HEADS; i++) {
      this.heads[i] = new SovereignPart(this, "head_" + i, 1.2F, 1.2F);
    }

    // Combine all parts
    this.subEntities = new SovereignPart[2 + MAX_HEADS]; // Main head + tail + extra heads
    this.subEntities[0] = this.head;
    this.subEntities[1] = this.tail;
    System.arraycopy(this.heads, 0, this.subEntities, 2, MAX_HEADS);

    this.xpReward = 500;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 1000.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.6D)
        .add(Attributes.ATTACK_DAMAGE, 12.0D)
        .add(Attributes.ARMOR, 15.0D)
        .add(Attributes.ARMOR_TOUGHNESS, 10.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.FOLLOW_RANGE, 64.0D)
        .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new OceanSovereignChargeAttackGoal(this));
    this.goalSelector.addGoal(2, new OceanSovereignWhirlpoolGoal(this));
    this.goalSelector.addGoal(3, new OceanSovereignSonicBoomGoal(this));
    this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 16.0F));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  // Sonic attack state management
  public boolean isPerformingSonicAttack() {
    return this.isPerformingSonicAttack;
  }

  public void setSonicAttackState(boolean performing) {
    this.isPerformingSonicAttack = performing;
    if (!performing) {
      this.sonicAttackAnimation = 0;
      this.prevSonicAttackAnimation = 0;
    }
  }

  public float getSonicAttackAnimation(float partialTicks) {
    return Mth.lerp(partialTicks, this.prevSonicAttackAnimation, this.sonicAttackAnimation) / 20.0F;
  }

  // Whirlpool state management
  public boolean isPerformingWhirlpool() {
    return this.isPerformingWhirlpool;
  }

  public void setWhirlpoolState(boolean performing) {
    this.isPerformingWhirlpool = performing;
    if (!performing) {
      this.whirlpoolAnimation = 0;
      this.prevWhirlpoolAnimation = 0;
      this.whirlpoolCenter = null;
    } else {
      this.whirlpoolCenter = this.blockPosition();
    }
  }

  public float getWhirlpoolAnimation(float partialTicks) {
    return Mth.lerp(partialTicks, this.prevWhirlpoolAnimation, this.whirlpoolAnimation) / 20.0F;
  }

  @Nullable
  public BlockPos getWhirlpoolCenter() {
    return this.whirlpoolCenter;
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Update boss bar
    this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

    // Update sonic attack animation
    this.prevSonicAttackAnimation = this.sonicAttackAnimation;
    if (this.isPerformingSonicAttack && this.sonicAttackAnimation < 20) {
      this.sonicAttackAnimation++;
    } else if (!this.isPerformingSonicAttack && this.sonicAttackAnimation > 0) {
      this.sonicAttackAnimation--;
    }

    // Update whirlpool animation
    this.prevWhirlpoolAnimation = this.whirlpoolAnimation;
    if (this.isPerformingWhirlpool && this.whirlpoolAnimation < 20) {
      this.whirlpoolAnimation++;
    } else if (!this.isPerformingWhirlpool && this.whirlpoolAnimation > 0) {
      this.whirlpoolAnimation--;
    }

    // Update part positions
    this.updateParts();

    // Update cooldowns
    if (this.attackCooldown > 0) this.attackCooldown--;
    if (this.whirlpoolCooldown > 0) this.whirlpoolCooldown--;
    if (this.sonicBoomCooldown > 0) this.sonicBoomCooldown--;

    // Handle phase transitions
    if (this.phaseTransitionTicks > 0) {
      this.phaseTransitionTicks--;
      if (this.phaseTransitionTicks <= 0) {
        this.startNewPhase();
      }
    }

    // Update animations
    this.prevTentacleMovement = this.tentacleMovement;
    if (this.isInWaterOrBubble()) {
      this.tentacleMovement = Mth.lerp(0.05F, this.tentacleMovement, 1.0F);
    } else {
      this.tentacleMovement = Mth.lerp(0.05F, this.tentacleMovement, 0.0F);
    }

    // Phase transition check
    if (!this.level().isClientSide) {
      if (this.getHealth() < this.getMaxHealth() * 0.66f && this.phase == 0) {
        this.startPhaseTransition();
      } else if (this.getHealth() < this.getMaxHealth() * 0.33f && this.phase == 1) {
        this.startPhaseTransition();
      }
    }
  }

  private void startPhaseTransition() {
    this.phaseTransitionTicks = PHASE_TRANSITION_DURATION;
    this.setDeltaMovement(Vec3.ZERO);
    this.setNoAi(true);

    // Visual effects for phase transition
    if (this.level() instanceof ServerLevel serverLevel) {
      for (int i = 0; i < 50; i++) {
        double offsetX = (this.random.nextDouble() - 0.5) * 10.0;
        double offsetY = (this.random.nextDouble() - 0.5) * 5.0;
        double offsetZ = (this.random.nextDouble() - 0.5) * 10.0;

        serverLevel.sendParticles(
            ParticleTypes.BUBBLE_COLUMN_UP,
            this.getX() + offsetX,
            this.getY() + offsetY,
            this.getZ() + offsetZ,
            5,
            0.0,
            0.0,
            0.0,
            0.2);
      }
    }
  }

  private void startNewPhase() {
    this.phase++;
    this.setNoAi(false);

    // Heal slightly on phase change
    this.heal(this.getMaxHealth() * 0.2f);

    // Add effects based on phase
    if (this.phase == 1) {
      // Phase 2: Faster movement and attacks
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.8D);
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(16.0D);
    } else if (this.phase == 2) {
      // Phase 3: Even faster with more damage
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.0D);
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(20.0D);
      this.getAttribute(Attributes.ARMOR).setBaseValue(20.0D);
    }
  }

  private void updateParts() {
    // Update main head position
    float headYaw = this.getYRot() * ((float) Math.PI / 180F);
    float headPitch = this.getXRot() * ((float) Math.PI / 180F);

    Vec3 headPos =
        this.position()
            .add(0, 1.0D, 0)
            .add(new Vec3(Math.sin(-headYaw) * 2.0, 0, Math.cos(headYaw) * 2.0));

    this.head.setPos(headPos.x, headPos.y, headPos.z);
    this.head.setYRot(this.getYRot());
    this.head.setXRot(this.getXRot());

    // Update additional heads
    for (int i = 0; i < this.heads.length; i++) {
      float angle = (float) (this.tickCount * 0.2 + i * (2 * Math.PI / this.heads.length));
      float radius = 3.0F + (float) Math.sin(this.tickCount * 0.1 + i) * 0.5F;

      Vec3 offset =
          new Vec3(
              Math.sin(angle) * radius,
              Math.sin(this.tickCount * 0.1 + i) * 0.5,
              Math.cos(angle) * radius);

      this.heads[i].setPos(
          this.getX() + offset.x, this.getY() + 1.0 + offset.y, this.getZ() + offset.z);

      // Make heads look at the main target
      if (this.getTarget() != null) {
        Vec3 targetPos = this.getTarget().position();
        double dx = targetPos.x - this.heads[i].getX();
        double dy = targetPos.y - this.heads[i].getY();
        double dz = targetPos.z - this.heads[i].getZ();

        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;
        float pitch = (float) (-(Math.atan2(dy, horizontalDist) * (180D / Math.PI)));

        this.heads[i].setYRot(yaw);
        this.heads[i].setXRot(pitch);
      }
    }

    // Update tail position (sinusoidal movement)
    float tailAngle = (float) (this.tickCount * 0.2);
    Vec3 tailOffset =
        new Vec3(
            -Math.sin(headYaw) * 4.0 + Math.sin(tailAngle) * 0.5,
            Math.sin(tailAngle * 0.7) * 0.5,
            -Math.cos(headYaw) * 4.0 + Math.cos(tailAngle) * 0.5);

    this.tail.setPos(
        this.getX() + tailOffset.x, this.getY() + 0.5 + tailOffset.y, this.getZ() + tailOffset.z);

    // Tail follows body with some delay
    float tailYaw = Mth.lerp(0.1F, this.tail.getYRot(), this.getYRot() + 180);
    float tailPitch = Mth.lerp(0.1F, this.tail.getXRot(), 0);
    this.tail.setYRot(tailYaw);
    this.tail.setXRot(tailPitch);
  }

  // Custom move control for smooth underwater movement
  static class OceanSovereignMoveControl extends MoveControl {
    private final OceanSovereignEntity sovereign;

    public OceanSovereignMoveControl(OceanSovereignEntity mob) {
      super(mob);
      this.sovereign = mob;
    }

    @Override
    public void tick() {
      if (this.operation == Operation.MOVE_TO) {
        Vec3 toTarget =
            new Vec3(
                this.wantedX - this.sovereign.getX(),
                this.wantedY - this.sovereign.getY(),
                this.wantedZ - this.sovereign.getZ());
        double distance = toTarget.length();

        if (distance < this.sovereign.getBoundingBox().getSize()) {
          this.operation = Operation.WAIT;
          this.sovereign.setDeltaMovement(this.sovereign.getDeltaMovement().scale(0.5D));
        } else {
          this.sovereign.setDeltaMovement(
              this.sovereign
                  .getDeltaMovement()
                  .add(toTarget.scale(this.speedModifier * 0.05D / distance)));

          if (this.sovereign.getTarget() == null) {
            Vec3 deltaMovement = this.sovereign.getDeltaMovement();
            this.sovereign.setYRot(
                -((float) Mth.atan2(deltaMovement.x, deltaMovement.z)) * (180F / (float) Math.PI));
            this.sovereign.yBodyRot = this.sovereign.getYRot();
          } else {
            double dx = this.sovereign.getTarget().getX() - this.sovereign.getX();
            double dz = this.sovereign.getTarget().getZ() - this.sovereign.getZ();
            this.sovereign.setYRot(-((float) Mth.atan2(dx, dz)) * (180F / (float) Math.PI));
            this.sovereign.yBodyRot = this.sovereign.getYRot();
          }
        }
      }
    }
  }

  // Custom look control for better head tracking
  static class OceanSovereignLookControl extends LookControl {
    public OceanSovereignLookControl(Mob mob) {
      super(mob);
    }

    @Override
    public void tick() {
      // Override to prevent head snapping
      if (this.lookAtCooldown > 0) {
        --this.lookAtCooldown;
        this.getYRotD()
            .ifPresent(
                yRot -> {
                  this.mob.yHeadRot =
                      this.rotateTowards(this.mob.yHeadRot, yRot, this.yMaxRotSpeed);
                });
        this.getXRotD()
            .ifPresent(
                xRot -> {
                  this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), xRot, this.xMaxRotAngle));
                });
      } else {
        this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
      }

      float wrappedYaw = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
      if (wrappedYaw < -50.0F) {
        this.mob.yBodyRot -= 4.0F;
      } else if (wrappedYaw > 50.0F) {
        this.mob.yBodyRot += 4.0F;
      }
    }
  }

  // Custom part entity for the boss
  public static class SovereignPart extends PartEntity<OceanSovereignEntity> {
    private final String name;
    private final float width;
    private final float height;

    public SovereignPart(OceanSovereignEntity parent, String name, float width, float height) {
      super(parent);
      this.name = name;
      this.width = width;
      this.height = height;
      this.refreshDimensions();
    }

    @Override
    public boolean isPickable() {
      return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
      // Forward damage to parent with slight reduction
      return !this.isInvulnerableTo(source) && this.getParent().hurt(source, amount * 0.75F);
    }

    @Override
    public boolean is(Entity entity) {
      return this == entity || this.getParent() == entity;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected void setRot(float yRot, float xRot) {
      this.setYRot(yRot % 360.0F);
      this.setXRot(xRot % 360.0F);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
      return EntityDimensions.scalable(this.width, this.height);
    }
  }

  // Attack goal for charge attacks
  static class OceanSovereignChargeAttackGoal extends Goal {
    private final OceanSovereignEntity sovereign;
    private int chargeTime;
    private int chargeDuration;
    private Vec3 chargeDirection;

    public OceanSovereignChargeAttackGoal(OceanSovereignEntity sovereign) {
      this.sovereign = sovereign;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      LivingEntity target = this.sovereign.getTarget();

      if (target == null || !target.isAlive() || !this.sovereign.hasLineOfSight(target)) {
        return false;
      }

      if (this.sovereign.attackCooldown > 0) {
        return false;
      }

      double distance = this.sovereign.distanceToSqr(target);
      return distance >= 16.0D && distance <= 100.0D && this.sovereign.isInWater();
    }

    @Override
    public void start() {
      LivingEntity target = this.sovereign.getTarget();
      if (target != null) {
        this.chargeDirection =
            new Vec3(
                    target.getX() - this.sovereign.getX(),
                    target.getY() - this.sovereign.getY(),
                    target.getZ() - this.sovereign.getZ())
                .normalize();

        this.chargeDuration = 20 + this.sovereign.getRandom().nextInt(30);
        this.chargeTime = 0;
        this.sovereign.setCharging(true);
      }
    }

    @Override
    public void tick() {
      this.chargeTime++;

      if (this.chargeDirection != null) {
        // Apply movement
        Vec3 motion = this.chargeDirection.scale(1.5D);
        this.sovereign.setDeltaMovement(motion);

        // Damage entities in the way
        for (Entity entity :
            this.sovereign
                .level()
                .getEntities(
                    this.sovereign,
                    this.sovereign.getBoundingBox().inflate(2.0D),
                    (e) -> e instanceof LivingEntity && e.isAlive() && e != this.sovereign)) {
          entity.hurt(this.sovereign.damageSources().mobAttack(this.sovereign), 10.0F);

          if (entity instanceof LivingEntity living) {
            living.knockback(1.5D, -motion.x, -motion.z);
            living.setDeltaMovement(living.getDeltaMovement().add(0.0D, 0.2D, 0.0D));
          }
        }
      }
    }

    @Override
    public void stop() {
      this.sovereign.setCharging(false);
      this.sovereign.attackCooldown = 40 + this.sovereign.getRandom().nextInt(60);
    }

    @Override
    public boolean canContinueToUse() {
      return this.chargeTime < this.chargeDuration && this.sovereign.isInWater();
    }
  }

  // Whirlpool attack goal
  static class OceanSovereignWhirlpoolGoal extends Goal {
    private final OceanSovereignEntity sovereign;
    private int whirlpoolTicks;
    private BlockPos centerPos;

    public OceanSovereignWhirlpoolGoal(OceanSovereignEntity sovereign) {
      this.sovereign = sovereign;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      if (this.sovereign.whirlpoolCooldown > 0 || this.sovereign.phase < 1) {
        return false;
      }

      LivingEntity target = this.sovereign.getTarget();
      return target != null && target.isAlive() && this.sovereign.distanceToSqr(target) < 400.0D;
    }

    @Override
    public void start() {
      this.whirlpoolTicks = 0;
      this.centerPos = this.sovereign.blockPosition();
      this.sovereign.whirlpoolCooldown = WHIRLPOOL_COOLDOWN;
    }

    @Override
    public void tick() {
      this.whirlpoolTicks++;

      // Create whirlpool effect
      if (!this.sovereign.level().isClientSide) {
        ServerLevel level = (ServerLevel) this.sovereign.level();
        double radius = 10.0D;
        int particleCount = 50;

        for (int i = 0; i < particleCount; i++) {
          double angle = (i / (double) particleCount) * Math.PI * 2.0;
          double x = this.centerPos.getX() + 0.5 + Math.cos(angle) * radius;
          double z = this.centerPos.getZ() + 0.5 + Math.sin(angle) * radius;

          // Spawn particles
          level.sendParticles(
              ParticleTypes.BUBBLE_COLUMN_UP,
              x,
              this.centerPos.getY() + 0.5,
              z,
              1,
              0.0,
              0.1,
              0.0,
              0.5);

          // Pull entities towards center
          AABB pullBox =
              new AABB(
                  x - 1.0,
                  this.centerPos.getY() - 2.0,
                  z - 1.0,
                  x + 1.0,
                  this.centerPos.getY() + 2.0,
                  z + 1.0);

          for (Entity entity : level.getEntities(null, pullBox)) {
            if (entity != this.sovereign && entity.isAlive() && !entity.isSpectator()) {
              Vec3 toCenter =
                  new Vec3(
                          this.centerPos.getX() + 0.5 - entity.getX(),
                          0,
                          this.centerPos.getZ() + 0.5 - entity.getZ())
                      .normalize()
                      .scale(0.2);

              entity.setDeltaMovement(entity.getDeltaMovement().add(toCenter.x, -0.1, toCenter.z));

              if (entity instanceof LivingEntity living) {
                living.hurt(this.sovereign.damageSources().drown(), 2.0F);
              }
            }
          }
        }
      }
    }

    @Override
    public boolean canContinueToUse() {
      return this.whirlpoolTicks < 100 && this.sovereign.getHealth() > 0.0F;
    }

    @Override
    public void stop() {
      this.sovereign.whirlpoolCooldown = WHIRLPOOL_COOLDOWN;
    }
  }

  // Sonic boom attack goal
  static class OceanSovereignSonicBoomGoal extends Goal {
    private final OceanSovereignEntity sovereign;
    private int sonicBoomTicks;

    public OceanSovereignSonicBoomGoal(OceanSovereignEntity sovereign) {
      this.sovereign = sovereign;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      if (this.sovereign.sonicBoomCooldown > 0 || this.sovereign.phase < 2) {
        return false;
      }

      LivingEntity target = this.sovereign.getTarget();
      return target != null && target.isAlive() && this.sovereign.distanceToSqr(target) < 100.0D;
    }

    @Override
    public void start() {
      this.sonicBoomTicks = 0;
      this.sovereign.sonicBoomCooldown = SONIC_BOOM_COOLDOWN;
      this.sovereign.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 0.5F);
    }

    @Override
    public void tick() {
      this.sonicBoomTicks++;

      // Create sonic boom effect
      if (!this.sovereign.level().isClientSide && this.sonicBoomTicks == 10) {
        ServerLevel level = (ServerLevel) this.sovereign.level();
        Vec3 lookVec = this.sovereign.getLookAngle();
        Vec3 startPos = this.sovereign.getEyePosition();

        for (int i = 0; i < 20; i++) {
          Vec3 pos = startPos.add(lookVec.scale(i));
          level.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);

          // Damage entities in the line
          AABB hitBox =
              new AABB(
                  pos.x - 1.0, pos.y - 1.0, pos.z - 1.0, pos.x + 1.0, pos.y + 1.0, pos.z + 1.0);

          for (Entity entity : level.getEntities(null, hitBox)) {
            if (entity != this.sovereign && entity.isAlive() && !entity.isSpectator()) {
              entity.hurt(this.sovereign.damageSources().sonicBoom(this.sovereign), 10.0F);

              if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
              }
            }
          }
        }
      }
    }

    @Override
    public boolean canContinueToUse() {
      return this.sonicBoomTicks < 20;
    }
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("Phase", this.phase);
    compound.putInt("AttackCooldown", this.attackCooldown);
    compound.putInt("WhirlpoolCooldown", this.whirlpoolCooldown);
    compound.putInt("SonicBoomCooldown", this.sonicBoomCooldown);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.phase = compound.getInt("Phase");
    this.attackCooldown = compound.getInt("AttackCooldown");
    this.whirlpoolCooldown = compound.getInt("WhirlpoolCooldown");
    this.sonicBoomCooldown = compound.getInt("SonicBoomCooldown");
  }

  @Override
  public void startSeenByPlayer(ServerPlayer player) {
    super.startSeenByPlayer(player);
    this.bossEvent.addPlayer(player);
  }

  @Override
  public void stopSeenByPlayer(ServerPlayer player) {
    super.stopSeenByPlayer(player);
    this.bossEvent.removePlayer(player);
  }

  @Override
  public void checkDespawn() {
    // Don't despawn the boss
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true;
  }

  @Override
  protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(source, looting, recentlyHit);
    // Drop Ocean Core and other loot
    this.spawnAtLocation(
        new ItemStack(Items.HEART_OF_THE_SEA)); // TODO: Replace with Ocean Core item
  }

  @Override
  protected ItemStack getBucketItemStack() {
    return new ItemStack(Items.HEART_OF_THE_SEA); // TODO: Replace with Ocean Core item
  }

  public static boolean checkOceanSovereignSpawnRules(
      EntityType<OceanSovereignEntity> type,
      LevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    // Only spawn via spawn egg or commands
    return spawnType == MobSpawnType.SPAWNER || spawnType == MobSpawnType.COMMAND;
  }
}
