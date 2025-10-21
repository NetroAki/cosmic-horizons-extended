package com.netroaki.chex.entity.torus.boss;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TorusWardenEntity extends Monster {
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(),
          BossEvent.BossBarColor.YELLOW,
          BossEvent.BossBarOverlay.NOTCHED_12);
  private int phase = 1; // 1 Structural Defense, 2 Gravity Collapse, 3 Singularity Protocol
  private int cooldown = 0;

  public TorusWardenEntity(EntityType<? extends TorusWardenEntity> type, Level level) {
    super(type, level);
    this.xpReward = 900;
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
      case 1 -> { // Structural Defense: drone summon cue
        server.playSound(
            null, blockPosition(), SoundEvents.IRON_GOLEM_REPAIR, getSoundSource(), 2.0f, 1.0f);
        server.sendParticles(
            ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 1.5, getZ(), 24, 2, 1, 2, 0.05);
      }
      case 2 -> { // Gravity Collapse: gravity flip cue
        server.playSound(
            null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, getSoundSource(), 2.0f, 0.6f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(10))) {
          p.push(0, 0.75, 0);
        }
        server.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 1, getZ(), 40, 3, 1, 3, 0.2);
      }
      case 3 -> { // Singularity Protocol: pull-in cue
        server.playSound(
            null, blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, getSoundSource(), 2.0f, 0.7f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(12))) {
          double dx = getX() - p.getX();
          double dz = getZ() - p.getZ();
          double mag = Math.max(0.001, Math.hypot(dx, dz));
          p.push(1.2 * dx / mag, 0.0, 1.2 * dz / mag);
        }
        server.sendParticles(ParticleTypes.ASH, getX(), getY() + 1, getZ(), 60, 2.5, 1, 2.5, 0.1);
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
