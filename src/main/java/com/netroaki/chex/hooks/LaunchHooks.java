package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.CHEXConfig;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.registry.RocketTiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public final class LaunchHooks {
    private LaunchHooks() {
    }

    /**
     * Comprehensive launch validation for players traveling to planets
     */
    public static boolean canLaunch(Player player, ResourceKey<Level> target, int rocketTierLevel) {
        if (player == null) {
            CHEX.LOGGER.warn("Launch validation called with null player");
            return false;
        }

        RocketTiers tier = RocketTiers.fromLevel(rocketTierLevel);
        String fuelId = CHEXConfig.getFuelForTier(tier);
        PlanetDef def = PlanetRegistry.getPlanet(target.location());

        if (def == null) {
            CHEX.LOGGER.warn("Unknown target planet {}. Allowing by default.", target.location());
            return true;
        }

        // Check rocket tier requirement
        if (rocketTierLevel < def.requiredRocketTier().getTier()) {
            player.displayClientMessage(
                    Component.literal("§c[CHEX] Rocket tier too low! Required: T" + def.requiredRocketTier()
                            + ", Current: T" + rocketTierLevel),
                    true);
            return false;
        }

        // Check player's unlocked rocket tier
        PlayerTierCapability playerTier = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (playerTier != null && !playerTier.canAccessRocketTier(def.requiredRocketTier())) {
            player.displayClientMessage(
                    Component.literal(
                            "§c[CHEX] You haven't unlocked rocket tier T" + def.requiredRocketTier().getTier()
                                    + " yet!"),
                    true);
            return false;
        }

        // Check if player has unlocked this specific planet
        if (playerTier != null && !playerTier.hasUnlockedPlanet(target.location().toString())) {
            player.displayClientMessage(
                    Component.literal("§c[CHEX] You haven't discovered this planet yet! Explore more to unlock it."),
                    true);
            return false;
        }

        // Check suit requirements
        if (!validateSuitRequirements(player, def.requiredSuitTag())) {
            return false;
        }

        // Check fuel requirements (placeholder for now)
        if (!validateFuelRequirements(player, fuelId, def.requiredRocketTier().getTier())) {
            return false;
        }

        // Log successful validation
        CHEX.LOGGER.info("Launch validation passed: player={}, target={}, tier={}, requiredTier={}, fuel={}",
                player.getGameProfile().getName(), target.location(), rocketTierLevel,
                def.requiredRocketTier(), fuelId);

        return true;
    }

    /**
     * Validates that the player has the required suit tier for the destination
     */
    private static boolean validateSuitRequirements(Player player, String requiredSuitTag) {
        if (requiredSuitTag == null || requiredSuitTag.isEmpty()) {
            return true; // No suit requirement
        }

        try {
            ResourceLocation tagLocation = ResourceLocation.fromNamespaceAndPath(
                    requiredSuitTag.split(":")[0],
                    requiredSuitTag.split(":")[1]);
            TagKey<Item> suitTag = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), tagLocation);

            // Check if player is wearing a complete suit from the required tag
            boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).is(suitTag);
            boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).is(suitTag);
            boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).is(suitTag);
            boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).is(suitTag);

            if (!hasHelmet || !hasChestplate || !hasLeggings || !hasBoots) {
                player.displayClientMessage(
                        Component.literal("§c[CHEX] You need a complete " + getSuitTierName(requiredSuitTag)
                                + " suit to travel here!"),
                        true);
                return false;
            }

            return true;
        } catch (Exception e) {
            CHEX.LOGGER.error("Error validating suit requirements for tag {}: {}", requiredSuitTag, e.getMessage());
            return true; // Allow launch if suit validation fails
        }
    }

    /**
     * Validates fuel requirements (placeholder implementation)
     */
    private static boolean validateFuelRequirements(Player player, String requiredFuel, int rocketTier) {
        // TODO: Implement actual fuel validation by checking rocket/launch structure
        // tanks
        // For now, just log the requirement
        CHEX.LOGGER.info("Fuel validation: player={}, requiredFuel={}, rocketTier={}",
                player.getGameProfile().getName(), requiredFuel, rocketTier);

        // Placeholder: always return true for now
        // In a real implementation, this would check:
        // 1. If the player has a rocket/launch structure nearby
        // 2. If the rocket has the required fuel type
        // 3. If the rocket has enough fuel for the journey
        return true;
    }

    /**
     * Gets a friendly name for the suit tier from the tag
     */
    private static String getSuitTierName(String suitTag) {
        if (suitTag.contains("suit1"))
            return "Basic";
        if (suitTag.contains("suit2"))
            return "Advanced";
        if (suitTag.contains("suit3"))
            return "Heavy Duty";
        if (suitTag.contains("suit4"))
            return "Exotic";
        if (suitTag.contains("suit5"))
            return "Ultimate";
        return "Space";
    }

    /**
     * Validates launch requirements for a specific planet without a player context
     */
    public static boolean canLaunchToPlanet(ResourceKey<Level> target, int rocketTierLevel) {
        PlanetDef def = PlanetRegistry.getPlanet(target.location());

        if (def == null) {
            CHEX.LOGGER.warn("Unknown target planet {}. Allowing by default.", target.location());
            return true;
        }

        return rocketTierLevel >= def.requiredRocketTier().getTier();
    }

    /**
     * Gets the minimum rocket tier required for a planet
     */
    public static int getRequiredRocketTier(ResourceKey<Level> target) {
        PlanetDef def = PlanetRegistry.getPlanet(target.location());

        return def != null ? def.requiredRocketTier().getTier() : 1;
    }

    /**
     * Gets the required suit tag for a planet
     */
    public static String getRequiredSuitTag(ResourceKey<Level> target) {
        PlanetDef def = PlanetRegistry.getPlanet(target.location());

        return def != null ? def.requiredSuitTag() : "";
    }

    /**
     * Gets all planets accessible with a given rocket tier
     */
    public static List<PlanetDef> getAccessiblePlanets(int rocketTier) {
        return PlanetRegistry.getAllPlanets().values().stream()
                .filter(planet -> planet.requiredRocketTier().getTier() <= rocketTier)
                .toList();
    }
}
