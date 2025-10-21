package com.netroaki.chex.world.hazard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HazardManager {
  private static final Map<Level, Map<BlockPos, HazardZone>> hazardZones = new WeakHashMap<>();
  private static final Set<Level> activeLevels = Collections.newSetFromMap(new WeakHashMap<>());

  public static void addHazardZone(Level level, HazardZone hazardZone) {
    if (level.isClientSide) return;

    hazardZones
        .computeIfAbsent(level, k -> new ConcurrentHashMap<>())
        .put(hazardZone.getCenter(), hazardZone);
    activeLevels.add(level);
  }

  public static void removeHazardZone(Level level, BlockPos pos) {
    if (level.isClientSide) return;

    Map<BlockPos, HazardZone> levelHazards = hazardZones.get(level);
    if (levelHazards != null) {
      levelHazards.remove(pos);
      if (levelHazards.isEmpty()) {
        hazardZones.remove(level);
        activeLevels.remove(level);
      }
    }
  }

  public static Collection<HazardZone> getHazardZones(Level level) {
    Map<BlockPos, HazardZone> levelHazards = hazardZones.get(level);
    return levelHazards != null ? levelHazards.values() : Collections.emptyList();
  }

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.level.isClientSide) {
      return;
    }

    Level level = event.level;
    Map<BlockPos, HazardZone> levelHazards = hazardZones.get(level);

    if (levelHazards != null && !levelHazards.isEmpty()) {
      // Create a copy to avoid concurrent modification
      List<HazardZone> hazards = new ArrayList<>(levelHazards.values());

      for (HazardZone hazard : hazards) {
        if (hazard.isActive()) {
          hazard.tick(level);
        } else {
          // Remove inactive hazards
          levelHazards.remove(hazard.getCenter());
        }
      }

      // Clean up empty levels
      if (levelHazards.isEmpty()) {
        hazardZones.remove(level);
        activeLevels.remove(level);
      }
    }
  }

  @SubscribeEvent
  public static void onWorldUnload(LevelEvent.Unload event) {
    if (event.getLevel() instanceof ServerLevel) {
      hazardZones.remove(event.getLevel());
      activeLevels.remove(event.getLevel());
    }
  }

  public static void clearAll() {
    hazardZones.clear();
    activeLevels.clear();
  }
}
