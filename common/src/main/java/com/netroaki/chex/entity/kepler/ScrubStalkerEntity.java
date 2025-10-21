package com.netroaki.chex.entity.kepler;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class ScrubStalkerEntity extends Monster {
  public ScrubStalkerEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 25.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 5.0D)
        .add(Attributes.ARMOR, 2.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
    this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  public static boolean checkSpawnRules(
      EntityType<ScrubStalkerEntity> type,
      LevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    return Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random);
  }

  @Override
  public boolean checkSpawnObstruction(LevelReader level) {
    return level.isUnobstructed(this);
  }
}
