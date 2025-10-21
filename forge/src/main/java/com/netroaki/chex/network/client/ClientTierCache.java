package com.netroaki.chex.network.client;

import com.netroaki.chex.capabilities.PlayerTierCapability;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/** Lightweight client-side cache for player tier data to avoid server class accesses. */
@OnlyIn(Dist.CLIENT)
public final class ClientTierCache {

  private static final Map<UUID, PlayerTierCapability> CACHE = new ConcurrentHashMap<>();

  private ClientTierCache() {}

  public static void update(UUID playerId, PlayerTierCapability cap) {
    CACHE.put(playerId, cap);
  }

  public static PlayerTierCapability get(UUID playerId) {
    return CACHE.get(playerId);
  }
}
