package com.netroaki.chex.entity.exotica.boss;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RealityBreakerEntity extends Monster {
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_12);
  private int phase = 1; // 1 Fractal Division, 2 Quantum Distortion, 3 Prismatic Collapse
  private int cooldown = 0;

  public RealityBreakerEntity(EntityType<? extends RealityBreakerEntity> type, Level level) {
    super(type, level);
    this.xpReward = 1000;
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
      case 1 -> { // Fractal Division: clone cue (visual)
        server.playSound(
            null,
            blockPosition(),
            SoundEvents.ILLUSIONER_MIRROR_MOVE,
            getSoundSource(),
            2.0f,
            1.0f);
        server.sendParticles(
            ParticleTypes.END_ROD, getX(), getY() + 1.5, getZ(), 32, 2, 1, 2, 0.02);
      }
      case 2 -> { // Quantum Distortion: random displacement cue
        server.playSound(
            null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, getSoundSource(), 2.0f, 0.8f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(10))) {
          p.teleportTo(p.getX() + (server.random.nextBoolean() ? 1 : -1), p.getY(), p.getZ());
        }
        server.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 1, getZ(), 40, 3, 1, 3, 0.2);
      }
      case 3 -> { // Prismatic Collapse: radial knock + sparkle
        server.playSound(
            null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, getSoundSource(), 2.0f, 1.2f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(10))) {
          double dx = p.getX() - getX();
          double dz = p.getZ() - getZ();
          double mag = Math.max(0.001, Math.hypot(dx, dz));
          p.push(0.9 * dx / mag, 0.4, 0.9 * dz / mag);
        }
        server.sendParticles(ParticleTypes.GLOW, getX(), getY() + 1, getZ(), 60, 2.5, 1, 2.5, 0.1);
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
}
