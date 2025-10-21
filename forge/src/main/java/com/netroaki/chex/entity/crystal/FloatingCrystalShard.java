package com.netroaki.chex.entity.crystal;

import com.netroaki.chex.registry.entities.CHEXEntities;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Vector3f;

public class FloatingCrystalShard extends Entity {
  private static final EntityDataAccessor<Integer> CRYSTAL_TYPE =
      SynchedEntityData.defineId(FloatingCrystalShard.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> ROTATION =
      SynchedEntityData.defineId(FloatingCrystalShard.class, EntityDataSerializers.INT);

  private float hoverStart;
  private float rotationSpeed;
  private float hoverHeight;
  private float bobSpeed;

  public FloatingCrystalShard(EntityType<?> type, Level level) {
    super(type, level);
    this.hoverStart = (float) (Math.random() * Math.PI * 2.0D);
    this.rotationSpeed = (float) (Math.random() * 0.5D + 0.5D) * 0.1F;
    this.hoverHeight = (float) (Math.random() * 0.2D + 0.1D);
    this.bobSpeed = (float) (Math.random() * 0.05D + 0.05D);
  }

  public FloatingCrystalShard(Level level, double x, double y, double z) {
    this(CHEXEntities.FLOATING_CRYSTAL.get(), level);
    this.setPos(x, y, z);
    this.setDeltaMovement(
        (Math.random() - 0.5D) * 0.1D, Math.random() * 0.1D, (Math.random() - 0.5D) * 0.1D);
    this.entityData.set(CRYSTAL_TYPE, this.random.nextInt(4));
    this.entityData.set(ROTATION, this.random.nextInt(360));
  }

  @Override
  protected void defineSynchedData() {
    this.entityData.define(CRYSTAL_TYPE, 0);
    this.entityData.define(ROTATION, 0);
  }

  @Override
  public void tick() {
    super.tick();

    // Update position and motion
    this.xo = this.getX();
    this.yo = this.getY();
    this.zo = this.getZ();

    // Apply floating motion
    this.hoverStart += this.bobSpeed;
    double hoverY = Math.sin(this.hoverStart) * this.hoverHeight;

    // Update rotation
    int rotation = this.entityData.get(ROTATION);
    rotation = (rotation + (int) (this.rotationSpeed * 20.0F)) % 360;
    this.entityData.set(ROTATION, rotation);

    // Apply motion and collisions
    Vec3 motion = this.getDeltaMovement();
    this.setDeltaMovement(motion.x, 0.01D + hoverY * 0.01D, motion.z);

    if (!this.isNoGravity()) {
      this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
    }

    this.move(MoverType.SELF, this.getDeltaMovement());
    this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

    // Particle effects
    if (this.level().isClientSide) {
      this.spawnParticles();
    }

    // Despawn if too far from players
    if (!this.level().isClientSide) {
      Player player = this.level().getNearestPlayer(this, 32.0D);
      if (player == null || player.distanceToSqr(this) > 1024.0D) {
        this.discard();
      }
    }
  }

  private void spawnParticles() {
    int type = this.entityData.get(CRYSTAL_TYPE);
    Vector3f color = getCrystalColor(type);

    if (this.random.nextInt(5) == 0) {
      double x = this.getX() + (this.random.nextDouble() - 0.5D) * 0.5D;
      double y = this.getY() + (this.random.nextDouble() - 0.5D) * 0.5D;
      double z = this.getZ() + (this.random.nextDouble() - 0.5D) * 0.5D;

      this.level().addParticle(new DustParticleOptions(color, 1.0F), x, y, z, 0.0D, 0.0D, 0.0D);
    }
  }

  private static Vector3f getCrystalColor(int type) {
    return switch (type) {
      case 1 -> new Vector3f(0.5F, 0.8F, 1.0F); // Light blue
      case 2 -> new Vector3f(0.8F, 0.5F, 1.0F); // Purple
      case 3 -> new Vector3f(0.5F, 1.0F, 0.8F); // Cyan
      default -> new Vector3f(1.0F, 0.8F, 0.5F); // Yellow (default)
    };
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {
    this.entityData.set(CRYSTAL_TYPE, tag.getInt("CrystalType"));
    this.entityData.set(ROTATION, tag.getInt("Rotation"));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    tag.putInt("CrystalType", this.entityData.get(CRYSTAL_TYPE));
    tag.putInt("Rotation", this.entityData.get(ROTATION));
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  public boolean isPickable() {
    return true;
  }

  @Override
  public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
    if (this.isInvulnerableTo(source)) {
      return false;
    } else if (!this.level().isClientSide && !this.isRemoved()) {
      this.discard();
      this.level()
          .playSound(
              null,
              this.getX(),
              this.getY(),
              this.getZ(),
              SoundEvents.AMETHYST_BLOCK_BREAK,
              SoundSource.BLOCKS,
              0.7F,
              0.9F + this.random.nextFloat() * 0.2F);

      // Drop crystal shards when broken
      if (source.getEntity() instanceof Player) {
        this.spawnAtLocation(new ItemStack(Items.AMETHYST_SHARD, 1 + this.random.nextInt(3)));
      }

      return true;
    } else {
      return true;
    }
  }

  public int getCrystalType() {
    return this.entityData.get(CRYSTAL_TYPE);
  }

  public int getRotation() {
    return this.entityData.get(ROTATION);
  }
}
