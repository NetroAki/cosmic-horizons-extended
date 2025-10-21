package com.netroaki.chex.entity.stormworld.boss;

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

public class StormlordColossusEntity extends Monster {
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
  private int phase = 1; // 1 Thunder, 2 Tempest, 3 Cataclysm
  private int abilityCooldown = 0;

  public StormlordColossusEntity(EntityType<? extends StormlordColossusEntity> type, Level level) {
    super(type, level);
    this.xpReward = 500;
    this.setPersistenceRequired();
  }

  @Override
  public void tick() {
    super.tick();
    if (!level().isClientSide) {
      bossEvent.setProgress(getHealth() / getMaxHealth());
      // Phase transitions by health thresholds
      float hp = getHealth() / getMaxHealth();
      if (hp < 0.33f) phase = 3;
      else if (hp < 0.66f) phase = 2;
      else phase = 1;

      if (abilityCooldown > 0) abilityCooldown--;
      else {
        usePhaseAbility();
        abilityCooldown =
            switch (phase) {
              case 1 -> 80; // Thunder cadence
              case 2 -> 60; // Tempest faster
              default -> 40; // Cataclysm rapid
            };
      }
    }
  }

  private void usePhaseAbility() {
    if (!(level() instanceof ServerLevel server)) return;
    switch (phase) {
      case 1 -> { // Thunder: lightning burst cues
        server.playSound(
            null, blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, getSoundSource(), 2.0f, 1.0f);
        // visual
        server.sendParticles(
            ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 2, getZ(), 20, 2, 1, 2, 0.1);
      }
      case 2 -> { // Tempest: shockwave
        server.playSound(
            null, blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, getSoundSource(), 2.0f, 0.6f);
        knockbackNearby(1.2);
      }
      case 3 -> { // Cataclysm: combo
        server.playSound(
            null, blockPosition(), SoundEvents.WARDEN_ROAR, getSoundSource(), 2.0f, 0.5f);
        knockbackNearby(1.6);
        server.sendParticles(ParticleTypes.CLOUD, getX(), getY() + 1, getZ(), 40, 3, 1, 3, 0.2);
      }
    }
  }

  private void knockbackNearby(double strength) {
    for (Player p : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(8))) {
      double dx = p.getX() - getX();
      double dz = p.getZ() - getZ();
      double mag = Math.max(0.001, Math.hypot(dx, dz));
      p.push(strength * dx / mag, 0.5, strength * dz / mag);
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
    // Loot handled by loot table
  }
}
