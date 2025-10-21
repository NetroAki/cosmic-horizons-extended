package com.netroaki.chex.entity.stormworld;

import com.netroaki.chex.registry.entities.CHEXEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Thin wrapper around {@link LightningBolt} so Stormworld systems can spawn a themed bolt without
 * pulling in the full vanilla implementation details just yet.
 */
public class StormworldLightningBolt extends LightningBolt {

  public StormworldLightningBolt(EntityType<? extends LightningBolt> type, Level level) {
    super(type, level);
  }

  public StormworldLightningBolt(Level level, Vec3 position) {
    this(CHEXEntities.STORMWORLD_LIGHTNING.get(), level);
    this.moveTo(position.x(), position.y(), position.z());
  }

  public StormworldLightningBolt(Level level, double x, double y, double z) {
    this(level, new Vec3(x, y, z));
  }
}
