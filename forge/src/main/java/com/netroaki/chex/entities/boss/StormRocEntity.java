package com.netroaki.chex.entities.boss;

import com.netroaki.chex.registry.CHEXEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StormRocEntity extends Monster {

    private int lightningCooldown = 0;
    private int phase = 1; // 1 = normal, 2 = enraged
    private int wingFlapCooldown = 0;

    public StormRocEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setNoAi(false);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    public void tick() {
        super.tick();

        // Handle flying movement
        if (!this.level().isClientSide) {
            if (this.getTarget() != null) {
                // Face the target
                this.getLookControl().setLookAt(this.getTarget(), 180.0F, 30.0F);

                // Maintain altitude above target
                if (this.getY() < this.getTarget().getY() + 4.0D) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.1D, 0.0D));
                }

                // Lightning attack
                if (this.lightningCooldown <= 0 && this.random.nextInt(100) == 0) {
                    this.performLightningAttack();
                    this.lightningCooldown = 100 + this.random.nextInt(100);
                }

                // Wing flap attack
                if (this.wingFlapCooldown <= 0 && this.random.nextInt(80) == 0) {
                    this.performWingFlap();
                    this.wingFlapCooldown = 120 + this.random.nextInt(80);
                }

                // Phase transition at 50% health
                if (this.phase == 1 && this.getHealth() < this.getMaxHealth() * 0.5F) {
                    this.phase = 2;
                    this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12.0D);
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
                }
            }

            if (this.lightningCooldown > 0)
                this.lightningCooldown--;
            if (this.wingFlapCooldown > 0)
                this.wingFlapCooldown--;
        }
    }

    private void performLightningAttack() {
        if (this.getTarget() != null) {
            // Strike lightning at target's position
            BlockPos targetPos = this.getTarget().blockPosition();
            this.level().strikeLightning(targetPos);

            // Additional strikes in enraged phase
            if (this.phase == 2) {
                for (int i = 0; i < 3; i++) {
                    BlockPos offsetPos = targetPos.offset(
                            this.random.nextInt(5) - 2,
                            0,
                            this.random.nextInt(5) - 2);
                    this.level().strikeLightning(offsetPos);
                }
            }
        }
    }

    private void performWingFlap() {
        // Knockback nearby players
        for (Player player : this.level().getEntitiesOfClass(
                Player.class,
                this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
            if (player.isAlive() && !player.isSpectator()) {
                Vec3 knockback = player.position().subtract(this.position())
                        .normalize()
                        .scale(1.5D)
                        .add(0.0D, 0.5D, 0.0D);
                player.setDeltaMovement(knockback);
                player.hurtMarked = true;
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

}
