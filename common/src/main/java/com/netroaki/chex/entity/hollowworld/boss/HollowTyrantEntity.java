package com.netroaki.chex.entity.hollowworld.boss;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HollowTyrantEntity extends Monster {
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(),
          BossEvent.BossBarColor.PURPLE,
          BossEvent.BossBarOverlay.NOTCHED_12);
  private int phase = 1; // 1 Fungal Wrath, 2 Crystal Dominion, 3 Void Phase
  private int cooldown = 0;

  public HollowTyrantEntity(EntityType<? extends HollowTyrantEntity> type, Level level) {
    super(type, level);
    this.xpReward = 1100;
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
      case 1 -> { // Fungal Wrath: spore cloud cue
        server.playSound(
            null, blockPosition(), SoundEvents.FUNGUS_BREAK, getSoundSource(), 2.0f, 0.8f);
        server.sendParticles(
            ParticleTypes.SPORE_BLOSSOM_AIR, getX(), getY() + 1.2, getZ(), 40, 2.0, 1.0, 2.0, 0.03);
      }
      case 2 -> { // Crystal Dominion: crystal spike cue (knock-up)
        server.playSound(
            null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, getSoundSource(), 2.0f, 1.0f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(8))) {
          p.push(0.0, 0.8, 0.0);
        }
        server.sendParticles(
            ParticleTypes.GLOW, getX(), getY() + 0.5, getZ(), 30, 1.2, 0.3, 1.2, 0.02);
      }
      case 3 -> { // Void Phase: inward pull + ash
        server.playSound(
            null, blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, getSoundSource(), 2.0f, 0.7f);
        for (Player p : server.getEntitiesOfClass(Player.class, getBoundingBox().inflate(12))) {
          double dx = getX() - p.getX();
          double dz = getZ() - p.getZ();
          double mag = Math.max(0.001, Math.hypot(dx, dz));
          p.push(1.0 * dx / mag, 0.0, 1.0 * dz / mag);
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
