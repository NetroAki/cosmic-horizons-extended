package com.netroaki.chex.reputation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class FactionReputation implements INBTSerializable<CompoundTag> {
  private static final String NBT_KEY = "ChexFactionReputation";
  private static final Map<UUID, FactionReputation> PLAYER_REPUTATION = new HashMap<>();

  public enum Faction {
    FREMEN("fremen", "Fremen", "The native people of Arrakis"),
    SPACING_GUILD("spacing_guild", "Spacing Guild", "The powerful spacefaring organization"),
    IMPERIUM("imperium", "Imperium", "The ruling empire of the known universe"),
    BENE_GESSERIT(
        "bene_gesserit", "Bene Gesserit", "The secretive sisterhood with psychic abilities"),
    SMUGGLERS("smugglers", "Smugglers", "Illegal traders operating on the fringes of the law");

    private final String id;
    private final String displayName;
    private final String description;

    Faction(String id, String displayName, String description) {
      this.id = id;
      this.displayName = displayName;
      this.description = description;
    }

    public String getId() {
      return id;
    }

    public String getDisplayName() {
      return displayName;
    }

    public String getDescription() {
      return description;
    }

    public static Faction byId(String id) {
      for (Faction faction : values()) {
        if (faction.id.equals(id)) {
          return faction;
        }
      }
      return null;
    }
  }

  public enum ReputationTier {
    HOSTILE(-1000, "Hostile", 0xFF0000, "This faction is openly hostile towards you"),
    UNFRIENDLY(-500, "Unfriendly", 0xFF5555, "This faction dislikes you"),
    NEUTRAL(0, "Neutral", 0xFFFFFF, "This faction is neutral towards you"),
    FRIENDLY(500, "Friendly", 0x55FF55, "This faction likes you"),
    HONORED(2000, "Honored", 0x00AA00, "This faction highly respects you"),
    EXALTED(5000, "Exalted", 0x00FFFF, "You are a legend to this faction");

    private final int minReputation;
    private final String displayName;
    private final int color;
    private final String description;

    ReputationTier(int minReputation, String displayName, int color, String description) {
      this.minReputation = minReputation;
      this.displayName = displayName;
      this.color = color;
      this.description = description;
    }

    public int getMinReputation() {
      return minReputation;
    }

    public String getDisplayName() {
      return displayName;
    }

    public int getColor() {
      return color;
    }

    public String getDescription() {
      return description;
    }

    public static ReputationTier byReputation(int reputation) {
      ReputationTier[] values = values();
      ReputationTier currentTier = values[0];

      for (ReputationTier tier : values) {
        if (reputation >= tier.minReputation) {
          currentTier = tier;
        } else {
          break;
        }
      }

      return currentTier;
    }
  }

  private final Map<Faction, Integer> reputationMap = new EnumMap<>(Faction.class);
  private final UUID playerId;

  public FactionReputation(UUID playerId) {
    this.playerId = playerId;
    // Initialize all factions to neutral
    for (Faction faction : Faction.values()) {
      reputationMap.put(faction, 0);
    }
  }

  public static FactionReputation get(Player player) {
    return PLAYER_REPUTATION.computeIfAbsent(player.getUUID(), FactionReputation::new);
  }

  public int getReputation(Faction faction) {
    return reputationMap.getOrDefault(faction, 0);
  }

  public void setReputation(Faction faction, int amount) {
    int oldRep = getReputation(faction);
    int newRep = Math.max(-10000, Math.min(10000, amount)); // Clamp between -10000 and 10000
    reputationMap.put(faction, newRep);

    // Notify about reputation changes (can be used for achievements, messages,
    // etc.)
    onReputationChanged(faction, oldRep, newRep);
  }

  public void addReputation(Faction faction, int amount) {
    setReputation(faction, getReputation(faction) + amount);
  }

  public ReputationTier getReputationTier(Faction faction) {
    return ReputationTier.byReputation(getReputation(faction));
  }

  public boolean isAtLeast(Faction faction, ReputationTier tier) {
    return getReputation(faction) >= tier.getMinReputation();
  }

  private void onReputationChanged(Faction faction, int oldRep, int newRep) {
    ReputationTier oldTier = ReputationTier.byReputation(oldRep);
    ReputationTier newTier = ReputationTier.byReputation(newRep);

    if (oldTier != newTier) {
      // Notify player of tier change
      Player player = getPlayer();
      if (player != null) {
        String message =
            String.format(
                "Your reputation with %s is now %s",
                faction.getDisplayName(), newTier.getDisplayName());
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(message));
      }
    }
  }

  private Player getPlayer() {
    // TODO: Fix when getServer() method is available
    return null; // net.minecraft.server.MinecraftServer.getServer().getPlayerList().getPlayer(playerId);
  }

  @SubscribeEvent
  public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      // Load player reputation data when they log in
      PLAYER_REPUTATION.put(player.getUUID(), new FactionReputation(player.getUUID()));

      // Load from player NBT
      CompoundTag playerData = player.getPersistentData();
      if (playerData.contains(NBT_KEY, Tag.TAG_COMPOUND)) {
        FactionReputation rep = get(player);
        rep.deserializeNBT(playerData.getCompound(NBT_KEY));
      }
    }
  }

  @SubscribeEvent
  public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      // Save player reputation data when they log out
      FactionReputation rep = PLAYER_REPUTATION.remove(player.getUUID());
      if (rep != null) {
        CompoundTag playerData = player.getPersistentData();
        playerData.put(NBT_KEY, rep.serializeNBT());
      }
    }
  }

  @SubscribeEvent
  public static void onPlayerClone(PlayerEvent.Clone event) {
    // Copy reputation data when a player dies and respawns
    if (event.isWasDeath()) {
      CompoundTag oldData = event.getOriginal().getPersistentData();
      if (oldData.contains(NBT_KEY, Tag.TAG_COMPOUND)) {
        event.getEntity().getPersistentData().put(NBT_KEY, oldData.getCompound(NBT_KEY).copy());
      }
    }
  }

  @Override
  public @NotNull CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    CompoundTag factionsTag = new CompoundTag();

    for (Map.Entry<Faction, Integer> entry : reputationMap.entrySet()) {
      factionsTag.putInt(entry.getKey().id, entry.getValue());
    }

    nbt.put("Factions", factionsTag);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    reputationMap.clear();

    if (nbt.contains("Factions", Tag.TAG_COMPOUND)) {
      CompoundTag factionsTag = nbt.getCompound("Factions");

      for (String key : factionsTag.getAllKeys()) {
        Faction faction = Faction.byId(key);
        if (faction != null) {
          reputationMap.put(faction, factionsTag.getInt(key));
        }
      }
    }

    // Ensure all factions are initialized
    for (Faction faction : Faction.values()) {
      reputationMap.putIfAbsent(faction, 0);
    }
  }
}
