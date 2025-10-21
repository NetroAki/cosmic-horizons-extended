package com.netroaki.chex.entity.dyson.boss;

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

public class DysonApexEntity extends Monster {
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_12);
  private int phase = 1; // 1 Solar Flare, 2 Overload, 3 Collapse
  private int cooldown = 0;

  public DysonApexEntity(EntityType<? extends DysonApexEntity> type, Level level) {
    super(type, level);
    this.xpReward = 1300;
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
              case 1 -> 70; // solar flare cadence
              case 2 -> 50; // overload debuffs
              default -> 40; // collapse phase
            };
      }
    }
  }

  private void usePhaseAbility() {
    if (!(level() instanceof ServerLevel server)) return;
    switch (phase) {
      case 1 -> { // Solar Flare: plasma arc telegraph
        server.playSound(
            null, blockPosition(), SoundEvents.FIRECHARGE_USE, getSoundSource(), 2.0f, 1.2f);
        server.sendParticles(
            ParticleTypes.FLAME, getX(), getY() + 1.2, getZ(), 60, 2.0, 1.0, 2.0, 0.04);
      }
      case 2 -> { // Overload: EMP-like debuff cue
        server.playSound(
            null,
            blockPosition(),
            SoundEvents.REDSTONE_TORCH_BURNOUT,
            getSoundSource(),
            1.6f,
            0.6f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(10))) {
          p.hurt(server.damageSources().indirectMagic(this, this), 1.0f);
        }
        server.sendParticles(
            ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 0.5, getZ(), 50, 1.5, 0.5, 1.5, 0.02);
      }
      case 3 -> { // Collapse: panel collapse cue (shock ring)
        server.playSound(
            null, blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, getSoundSource(), 2.0f, 0.8f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(12))) {
          double dx = p.getX() - getX();
          double dz = p.getZ() - getZ();
          double mag = Math.max(0.001, Math.hypot(dx, dz));
          p.push(1.0 * dx / mag, 0.2, 1.0 * dz / mag);
        }
        server.sendParticles(ParticleTypes.ASH, getX(), getY() + 1, getZ(), 70, 2.5, 1, 2.5, 0.1);
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
