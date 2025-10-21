package com.netroaki.chex.entity.ringworld.boss;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GuardianPrimeEntity extends Monster {
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
  private int phase = 1; // 1 Defense Protocol, 2 Siege Mode, 3 Fail-safe Override
  private int cooldown = 0;

  public GuardianPrimeEntity(EntityType<? extends GuardianPrimeEntity> type, Level level) {
    super(type, level);
    this.xpReward = 800;
    this.setPersistenceRequired();
  }

  @Override
  public void tick() {
    super.tick();
    if (!level().isClientSide) {
      bossEvent.setProgress(getHealth() / getMaxHealth());
      float hpFrac = getHealth() / getMaxHealth();
      if (hpFrac < 0.33f) phase = 3;
      else if (hpFrac < 0.66f) phase = 2;
      else phase = 1;

      if (cooldown > 0) cooldown--;
      else {
        usePhaseAbility();
        cooldown =
            switch (phase) {
              case 1 -> 80;
              case 2 -> 60;
              default -> 40;
            };
      }
    }
  }

  private void usePhaseAbility() {
    if (!(level() instanceof ServerLevel server)) return;
    switch (phase) {
      case 1 -> { // Defense: rotating shields cue
        server.playSound(
            null, blockPosition(), SoundEvents.SHIELD_BLOCK, getSoundSource(), 2.0f, 0.7f);
        server.sendParticles(
            ParticleTypes.ENCHANT, getX(), getY() + 2, getZ(), 20, 1.5, 1.0, 1.5, 0.0);
      }
      case 2 -> { // Siege: plasma burst cue
        server.playSound(
            null, blockPosition(), SoundEvents.BEACON_POWER_SELECT, getSoundSource(), 2.0f, 0.9f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(10))) {
          p.hurt(p.damageSources().magic(), 2.0f);
        }
      }
      case 3 -> { // Fail-safe: platform collapse cue
        server.playSound(
            null, blockPosition(), SoundEvents.ANCIENT_DEBRIS_BREAK, getSoundSource(), 2.0f, 0.8f);
        server.sendParticles(
            ParticleTypes.SMOKE, getX(), getY() + 1, getZ(), 40, 2.5, 1.0, 2.5, 0.1);
      }
    }
  }

  @Override
  public void startSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
    super.startSeenByPlayer(player);
    bossEvent.addPlayer(player);
  }

  @Override
  public void stopSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
    super.stopSeenByPlayer(player);
    bossEvent.removePlayer(player);
  }

  @Override
  protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(source, looting, recentlyHit);
  }
}
