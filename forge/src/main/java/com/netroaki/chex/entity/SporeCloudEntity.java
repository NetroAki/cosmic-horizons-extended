package com.netroaki.chex.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

/**
 * Lightweight area-effect cloud used by Pandora encounters. The implementation is intentionally
 * modest—it just applies poison and plays spore particles—because the full behaviour will be
 * fleshed out in later content passes.
 */
public class SporeCloudEntity extends AreaEffectCloud {

  public SporeCloudEntity(EntityType<SporeCloudEntity> type, Level level) {
    super(type, level);
    initialiseDefaults();
  }

  public SporeCloudEntity(Level level, double x, double y, double z) {
    this(com.netroaki.chex.registry.entities.CHEXEntities.SPORE_CLOUD.get(), level);
    this.setPos(x, y, z);
  }

  private void initialiseDefaults() {
    this.setRadius(2.5F);
    this.setRadiusPerTick(-0.01F);
    this.setDuration(200);
    this.setParticle(ParticleTypes.SPORE_BLOSSOM_AIR);
    this.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0, true, true));
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    // Reapply defaults if data was stripped (e.g. for placeholder saves)
    if (!tag.contains("Radius")) {
      initialiseDefaults();
    }
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
