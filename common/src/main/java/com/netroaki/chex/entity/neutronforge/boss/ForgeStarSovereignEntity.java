package com.netroaki.chex.entity.neutronforge.boss;

import com.netroaki.chex.boss.BossController;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ForgeStarSovereignEntity extends Monster {
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(),
          BossEvent.BossBarColor.YELLOW,
          BossEvent.BossBarOverlay.NOTCHED_12);
  private int phase = 1; // 1 Magnetic, 2 Plasma, 3 Collapse
  private int cooldown = 0;

  public ForgeStarSovereignEntity(
      EntityType<? extends ForgeStarSovereignEntity> type, Level level) {
    super(type, level);
    this.xpReward = 1500;
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
      BossController.updatePhase(this, phase);

      if (cooldown > 0) cooldown--;
      else {
        usePhaseAbility();
        cooldown =
            switch (phase) {
              case 1 -> 80; // magnetic disarm cadence
              case 2 -> 55; // plasma volleys
              default -> 45; // collapse shock events
            };
      }
    }
  }

  private void usePhaseAbility() {
    if (!(level() instanceof ServerLevel server)) return;
    switch (phase) {
      case 1 -> { // Magnetic Phase: disarm cue (knock items briefly)
        server.playSound(
            null, blockPosition(), SoundEvents.ANVIL_LAND, getSoundSource(), 1.2f, 0.6f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(8))) {
          // Light shove to simulate magnetic tug
          double dx = getX() - p.getX();
          double dz = getZ() - p.getZ();
          double mag = Math.max(0.001, Math.hypot(dx, dz));
          p.push(0.3 * dx / mag, 0.05, 0.3 * dz / mag);
        }
        server.sendParticles(
            ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 1.0, getZ(), 40, 1.6, 0.5, 1.6, 0.03);
      }
      case 2 -> { // Plasma Phase: flame/ash telegraph
        server.playSound(
            null, blockPosition(), SoundEvents.FIRECHARGE_USE, getSoundSource(), 1.6f, 1.0f);
        server.sendParticles(
            ParticleTypes.FLAME, getX(), getY() + 1.2, getZ(), 60, 2.0, 0.6, 2.0, 0.03);
        server.sendParticles(
            ParticleTypes.ASH, getX(), getY() + 0.6, getZ(), 20, 1.0, 0.4, 1.0, 0.02);
      }
      case 3 -> { // Collapse Phase: outward shock ring
        server.playSound(
            null, blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, getSoundSource(), 2.0f, 0.9f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(12))) {
          double dx = p.getX() - getX();
          double dz = p.getZ() - getZ();
          double mag = Math.max(0.001, Math.hypot(dx, dz));
          p.push(1.1 * dx / mag, 0.25, 1.1 * dz / mag);
        }
        server.sendParticles(
            ParticleTypes.SONIC_BOOM, getX(), getY() + 1.0, getZ(), 8, 0.0, 0.0, 0.0, 0.0);
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
