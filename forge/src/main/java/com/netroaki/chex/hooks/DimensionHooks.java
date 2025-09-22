package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.CHEXConfig;
import com.netroaki.chex.registry.CHEXEffects;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.suits.SuitItems;
import com.netroaki.chex.suits.SuitTiers;
import com.netroaki.chex.travel.TravelGraph;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class DimensionHooks {
  public static void register() {
    MinecraftForge.EVENT_BUS.register(new DimensionHooks());
  }

  @SubscribeEvent
  public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    Player player = event.player;
    if (player == null || player.level().isClientSide) return;

    ResourceKey<Level> dim = player.level().dimension();
    if (!isCHEXOrCosmicPlanet(dim)) return;

    // Determine requirements and player's suit
    SuitTiers requiredSuit = getRequiredSuitForPlanet(dim);
    SuitTiers playerSuit = getPlayerSuitTier(player);

    // Oxygen handling delegated to Beyond Oxygen mod.

    // Radiation enforcement on farther/more extreme worlds
    if (hasRadiationHazard(dim)) {
      int radRequiredTier = getRadiationRequiredSuitTier(dim, requiredSuit);
      int playerTier = playerSuit != null ? playerSuit.getTier() : 0;
      if (playerTier < radRequiredTier) {
        // Custom radiation effect to model radiation poisoning
        player.addEffect(new MobEffectInstance(CHEXEffects.RADIATION.get(), 120, 0));
      }
    }
  }

  @SubscribeEvent
  public void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    Player player = event.getEntity();
    ResourceKey<Level> to = event.getTo();
    if (player == null || to == null) return;

    // Check if this is a CHEX or Cosmic Horizons planet
    if (isCHEXOrCosmicPlanet(to)) {
      // First check if player has the capability
      player.getCapability(com.netroaki.chex.capabilities.PlayerTierCapability.INSTANCE).ifPresent(capability -> {
        // Check if player can enter this dimension
        if (!capability.canEnterDimension(to.location())) {
          // Prevent entry and teleport back to spawn if not allowed
          if (!player.level().isClientSide) {
            player.teleportTo(
                player.getServer().overworld(),
                player.getX(),
                player.getY(),
                player.getZ(),
                player.getYRot(),
                player.getXRot()
            );
            player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§cYou don't have permission to enter this dimension!"),
                true
            );
          }
          return;
        }

        // Record first planet visit milestone
        if (!to.location().getPath().equals("overworld")) {
          capability.setMilestone(com.netroaki.chex.capabilities.PlayerTierCapability.MILESTONE_FIRST_PLANET);
        }
      });

      // Existing suit check logic
      SuitTiers requiredSuit = getRequiredSuitForPlanet(to);
      SuitTiers playerSuit = getPlayerSuitTier(player);

      if (requiredSuit != null && !isSuitAdequate(playerSuit, requiredSuit)) {
        handleInsufficientSuit(player, to, requiredSuit, playerSuit);
      }
    }
  }

  /** Check if the dimension is a CHEX or Cosmic Horizons planet */
  private boolean isCHEXOrCosmicPlanet(ResourceKey<Level> dimension) {
    String namespace = dimension.location().getNamespace();
    return namespace.equals("cosmic_horizons_extended") || namespace.equals("cosmos");
  }

  /** Get the required suit tier for a planet */
  private SuitTiers getRequiredSuitForPlanet(ResourceKey<Level> dimension) {
    // Check CHEX planets first
    var planetDef = PlanetRegistry.getPlanet(dimension.location());
    if (planetDef != null) {
      // Extract suit tier from suit tag (e.g., "chex:suits/suit3" -> 3)
      String suitTag = planetDef.requiredSuitTag();
      if (suitTag != null && suitTag.contains("suit")) {
        try {
          String tierStr = suitTag.substring(suitTag.lastIndexOf("suit") + 4);
          int tier = Integer.parseInt(tierStr);
          return SuitTiers.getByTier(tier);
        } catch (NumberFormatException e) {
          CHEX.LOGGER.warn("Could not parse suit tier from tag: {}", suitTag);
        }
      }
    }

    // Check travel graph for tier requirements
    int tier = TravelGraph.getTierForPlanet(dimension.location());
    if (tier != Integer.MAX_VALUE) {
      // Map rocket tier to suit tier (simplified mapping)
      int suitTier = Math.max(1, Math.min(5, (tier + 1) / 2));
      return SuitTiers.getByTier(suitTier);
    }

    return null;
  }

  // Oxygen requirement is handled by external mod; helper removed.

  private boolean hasRadiationHazard(ResourceKey<Level> dimension) {
    var def = PlanetRegistry.getPlanet(dimension.location());
    if (def != null) {
      // Heuristic: high-tier or extreme biome types are irradiated
      int tier = def.requiredRocketTier().getTier();
      String biomeType = def.biomeType() != null ? def.biomeType() : "";
      if (tier >= 8) return true;
      if (biomeType.contains("stellar")
          || biomeType.contains("gas_giant")
          || biomeType.contains("ring_system")
          || biomeType.contains("extreme")
          || biomeType.contains("ice_giant")) return true;
      return false;
    }
    // Unknown CH planets: assume far systems are risky if TravelGraph tier is high
    int tgTier = TravelGraph.getTierForPlanet(dimension.location());
    return tgTier != Integer.MAX_VALUE && tgTier >= 8;
  }

  private int getRadiationRequiredSuitTier(ResourceKey<Level> dimension, SuitTiers baseRequired) {
    // Radiation generally requires one tier higher than base suit, capped to 5
    int base = baseRequired != null ? baseRequired.getTier() : 1;
    int tgTier = TravelGraph.getTierForPlanet(dimension.location());
    // If very far (T9+), require at least tier 4; if T10, tier 5
    if (tgTier >= 10) return 5;
    if (tgTier >= 9) return Math.max(4, base + 1);
    return Math.max(2, Math.min(5, base + 1));
  }

  /** Get the player's current suit tier based on equipped armor */
  private SuitTiers getPlayerSuitTier(Player player) {
    SuitTiers highestTier = null;

    for (int i = 0; i < 4; i++) {
      var armorItem = player.getInventory().armor.get(i);
      if (armorItem.getItem() instanceof SuitItems.SuitItem suitItem) {
        SuitTiers suitTier = suitItem.getSuitTier();
        if (highestTier == null || suitTier.getTier() > highestTier.getTier()) {
          highestTier = suitTier;
        }
      }
    }

    return highestTier;
  }

  /** Check if the player's suit is adequate for the required suit */
  private boolean isSuitAdequate(SuitTiers playerSuit, SuitTiers requiredSuit) {
    if (playerSuit == null) return false;
    return playerSuit.getTier() >= requiredSuit.getTier();
  }

  /** Handle insufficient suit situation */
  private void handleInsufficientSuit(
      Player player, ResourceKey<Level> dimension, SuitTiers required, SuitTiers current) {
    String dimensionName = dimension.location().toString();
    String requiredName = required.getDisplayName();
    String currentName = current != null ? current.getDisplayName() : "None";

    // Send message to player
    player.displayClientMessage(
        net.minecraft.network.chat.Component.literal("§c[CHEX] Insufficient suit protection!")
            .append(
                net.minecraft.network.chat.Component.literal(
                        "\n§eRequired: " + requiredName + " (Tier " + required.getTier() + ")")
                    .append(
                        net.minecraft.network.chat.Component.literal(
                            "\n§eCurrent: "
                                + currentName
                                + (current != null ? " (Tier " + current.getTier() + ")" : "")))),
        true);

    if (CHEXConfig.suitBounceBack()) {
      // Bounce back to overworld
      var server = player.getServer();
      if (server != null) {
        var overworld = server.getLevel(Level.OVERWORLD);
        if (overworld != null) {
          var pos = overworld.getSharedSpawnPos();
          player.changeDimension(overworld);
          player.teleportTo(
              overworld,
              pos.getX() + 0.5,
              (double) pos.getY(),
              pos.getZ() + 0.5,
              java.util.Set.of(),
              player.getYRot(),
              player.getXRot());
          CHEX.LOGGER.info(
              "Bounced {} back to overworld due to insufficient suit for {} (required: {}, current:"
                  + " {})",
              player.getGameProfile().getName(),
              dimensionName,
              requiredName,
              currentName);
        }
      }
    } else {
      // No hypoxia; oxygen handling is external. Just notify.
      CHEX.LOGGER.info(
          "Insufficient suit for {} (required: {}, current: {}), not bouncing due to config",
          dimensionName,
          requiredName,
          currentName);
    }
  }

  private boolean hasRequiredSuit(Player player, String requiredSuitTag) {
    if (requiredSuitTag == null || requiredSuitTag.isEmpty()) return true;
    int idx = requiredSuitTag.indexOf(':');
    String path = idx >= 0 ? requiredSuitTag.substring(idx + 1) : requiredSuitTag;
    net.minecraft.resources.ResourceLocation tagId =
        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "suits/" + path);
    net.minecraft.tags.TagKey<net.minecraft.world.item.Item> tag =
        net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, tagId);
    var chest = player.getInventory().armor.get(2);
    if (chest.isEmpty()) return false;
    return chest.is(tag);
  }
}
