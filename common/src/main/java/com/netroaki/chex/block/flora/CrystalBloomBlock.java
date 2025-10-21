package com.netroaki.chex.block.flora;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CrystalBloomBlock extends FlowerBlock {
  private static final int EFFECT_RADIUS = 4;
  private static final int EFFECT_DURATION = 200; // 10 seconds

  public CrystalBloomBlock() {
    super(
        MobEffects.MOVEMENT_SLOWDOWN,
        8, // Effect duration in ticks
        Properties.of()
            .mapColor(MapColor.COLOR_CYAN)
            .lightLevel(state -> 8) // Emits light
            .sound(SoundType.GLASS)
            .noOcclusion()
            .instabreak()
            .noCollission()
            .offsetType(Block.OffsetType.XZ));
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    // Add sparkle particles
    if (random.nextInt(5) == 0) {
      double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
      double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
      double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;

      level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0, 0);
    }

    // Occasionally play a crystal chime sound
    if (random.nextInt(100) == 0) {
      level.playLocalSound(
          pos.getX() + 0.5,
          pos.getY() + 0.5,
          pos.getZ() + 0.5,
          SoundEvents.AMETHYST_BLOCK_CHIME,
          SoundSource.BLOCKS,
          0.5f,
          0.8f + random.nextFloat() * 0.4f,
          false);
    }
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
      // Apply slowness to entities that touch the crystal bloom
      livingEntity.addEffect(
          new MobEffectInstance(
              MobEffects.MOVEMENT_SLOWDOWN, EFFECT_DURATION, 1, false, true, true));

      // Heal the player slightly if they're at low health
      if (livingEntity.getHealth() < livingEntity.getMaxHealth() * 0.5) {
        livingEntity.heal(1.0F);

        // Play healing effect
        if (level.random.nextInt(3) == 0) {
          level.playSound(
              null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.5f, 1.2f);
        }
      }
    }

    // Slow down movement when inside the block
    Vec3 vec3 = entity.getDeltaMovement();
    entity.setDeltaMovement(vec3.multiply(0.5D, 1.0D, 0.5D));

    super.entityInside(state, level, pos, entity);
  }

  @Override
  public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
    if (!level.isClientSide && entity instanceof LivingEntity) {
      // Apply regeneration effect in an area when stepped on
      AABB area = new AABB(pos).inflate(EFFECT_RADIUS);
      List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

      for (LivingEntity target : entities) {
        target.addEffect(
            new MobEffectInstance(
                MobEffects.REGENERATION, EFFECT_DURATION / 2, 0, false, true, true));
      }

      // Visual effect
      if (!entities.isEmpty()) {
        level.levelEvent(2007, pos, 0);
      }
    }

    super.stepOn(level, pos, state, entity);
  }

  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.DESTROY; // Can be pushed by pistons but breaks
  }
}
