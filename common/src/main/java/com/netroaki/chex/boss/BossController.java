package com.netroaki.chex.boss;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Centralized lightweight Boss Controller. - Tracks boss registration and phase updates. -
 * Broadcasts defeat messages and cleans up state. - Simple API so boss entities can report their
 * phase without tight coupling.
 */
@Mod.EventBusSubscriber(
    modid = CosmicHorizonsExpanded.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BossController {

  public static final class BossInfo {
    public final UUID id;
    public final String name;
    public int phase;

    public BossInfo(UUID id, String name, int phase) {
      this.id = id;
      this.name = name;
      this.phase = phase;
    }
  }

  private static final Map<UUID, BossInfo> BOSSES = new ConcurrentHashMap<>();

  private BossController() {}

  /** Register the boss if not present and update its phase. */
  public static void updatePhase(LivingEntity entity, int phase) {
    BossInfo info =
        BOSSES.computeIfAbsent(
            entity.getUUID(), id -> new BossInfo(id, entity.getDisplayName().getString(), phase));
    info.phase = phase;
  }

  /** Remove a boss from tracking. */
  public static void unregister(Entity entity) {
    if (entity != null) BOSSES.remove(entity.getUUID());
  }

  /** Query current phase if tracked, else -1. */
  public static int getPhase(Entity entity) {
    BossInfo info = BOSSES.get(entity.getUUID());
    return info == null ? -1 : info.phase;
  }

  /** Server-wide broadcast in the boss's dimension. */
  private static void broadcast(Entity entity, String msg) {
    if (!(entity.level() instanceof ServerLevel level)) return;
    for (ServerPlayer p : level.players()) {
      p.displayClientMessage(Component.literal(msg), false);
    }
  }

  @SubscribeEvent
  public static void onBossDeath(LivingDeathEvent event) {
    LivingEntity ent = event.getEntity();
    BossInfo info = BOSSES.remove(ent.getUUID());
    if (info != null) {
      broadcast(ent, "§6" + info.name + " has been defeated!");
    }
  }
}
