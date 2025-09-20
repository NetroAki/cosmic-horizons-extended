package com.netroaki.chex.integration;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.registry.NoduleTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Integration with KubeJS for recipe gating and tier-based conditions.
 * This provides JavaScript-accessible functions for checking player tiers.
 */
public class KubeJSIntegration {

    /**
     * Check if a player has unlocked a specific rocket tier
     * Usage in KubeJS: PlayerTier.hasRocketTier(player, tier)
     */
    public static boolean hasRocketTier(Player player, int tier) {
        if (player == null || tier < 1 || tier > 10)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        return capability.canAccessNoduleTier(NoduleTiers.getByTier(tier));
    }

    /**
     * Check if a player has unlocked a specific suit tier
     * Usage in KubeJS: PlayerTier.hasSuitTier(player, tier)
     */
    public static boolean hasSuitTier(Player player, int tier) {
        if (player == null || tier < 1 || tier > 5)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        // TODO: Implement suit tier checking when suit progression is added
        return true; // Placeholder for now
    }

    /**
     * Get a player's current rocket tier
     * Usage in KubeJS: PlayerTier.getRocketTier(player)
     */
    public static int getRocketTier(Player player) {
        if (player == null)
            return 0;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return 0;

        return capability.getRocketTier();
    }

    // Nodule-named aliases for KubeJS (forward-compat)
    public static boolean hasNoduleTier(Player player, int tier) {
        return hasRocketTier(player, tier);
    }

    public static int getNoduleTier(Player player) {
        return getRocketTier(player);
    }

    /**
     * Get a player's current suit tier
     * Usage in KubeJS: PlayerTier.getSuitTier(player)
     */
    public static int getSuitTier(Player player) {
        if (player == null)
            return 0;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return 0;

        return capability.getSuitTier();
    }

    /**
     * Check if a player has discovered a specific planet
     * Usage in KubeJS: PlayerTier.hasDiscoveredPlanet(player, planetId)
     */
    public static boolean hasDiscoveredPlanet(Player player, String planetId) {
        if (player == null || planetId == null)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        return capability.hasUnlockedPlanet(planetId);
    }

    /**
     * Check if a player has discovered a specific mineral
     * Usage in KubeJS: PlayerTier.hasDiscoveredMineral(player, mineralId)
     */
    public static boolean hasDiscoveredMineral(Player player, String mineralId) {
        if (player == null || mineralId == null)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        return capability.hasDiscoveredMineral(mineralId);
    }

    /**
     * Unlock a planet for a player
     * Usage in KubeJS: PlayerTier.unlockPlanet(player, planetId)
     */
    public static boolean unlockPlanet(Player player, String planetId) {
        if (player == null || planetId == null)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        capability.unlockPlanet(planetId);
        CHEX.LOGGER.info("Unlocked planet {} for player {}", planetId, player.getGameProfile().getName());
        return true;
    }

    /**
     * Unlock a mineral for a player
     * Usage in KubeJS: PlayerTier.unlockMineral(player, mineralId)
     */
    public static boolean unlockMineral(Player player, String mineralId) {
        if (player == null || mineralId == null)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        capability.discoverMineral(mineralId);
        CHEX.LOGGER.info("Discovered mineral {} for player {}", mineralId, player.getGameProfile().getName());
        return true;
    }

    /**
     * Unlock a rocket tier for a player
     * Usage in KubeJS: PlayerTier.unlockRocketTier(player, tier)
     */
    public static boolean unlockRocketTier(Player player, int tier) {
        if (player == null || tier < 1 || tier > 10)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        boolean success = capability.unlockNoduleTier(tier);
        if (success) {
            CHEX.LOGGER.info("Unlocked nodule tier T{} for player {}", tier, player.getGameProfile().getName());
        }
        return success;
    }

    public static boolean unlockNoduleTier(Player player, int tier) {
        return unlockRocketTier(player, tier);
    }

    /**
     * Get the nodule material theme for a tier
     * Usage in KubeJS: PlayerTier.getNoduleMaterial(tier)
     */
    public static String getNoduleMaterial(int tier) {
        NoduleTiers nt = NoduleTiers.getByTier(tier);
        return nt == null ? "" : nt.getMaterialName();
    }

    /**
     * Get the current player's nodule material theme based on their tier
     * Usage in KubeJS: PlayerTier.getPlayerNoduleMaterial(player)
     */
    public static String getPlayerNoduleMaterial(Player player) {
        if (player == null)
            return "";
        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return "";
        NoduleTiers nt = NoduleTiers.getByTier(capability.getRocketTier());
        return nt == null ? "" : nt.getMaterialName();
    }

    /**
     * Unlock a suit tier for a player
     * Usage in KubeJS: PlayerTier.unlockSuitTier(player, tier)
     */
    public static boolean unlockSuitTier(Player player, int tier) {
        if (player == null || tier < 1 || tier > 5)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        // TODO: Implement suit tier unlocking when suit progression is added
        CHEX.LOGGER.info("Suit tier unlocking not yet implemented for tier {}", tier);
        return false; // Placeholder for now
    }

    /**
     * Get all discovered planets for a player
     * Usage in KubeJS: PlayerTier.getDiscoveredPlanets(player)
     */
    public static String[] getDiscoveredPlanets(Player player) {
        if (player == null)
            return new String[0];

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return new String[0];

        return capability.getUnlockedPlanets().toArray(new String[0]);
    }

    /**
     * Get all discovered minerals for a player
     * Usage in KubeJS: PlayerTier.getDiscoveredMinerals(player)
     */
    public static String[] getDiscoveredMinerals(Player player) {
        if (player == null)
            return new String[0];

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return new String[0];

        return capability.getDiscoveredMinerals().toArray(new String[0]);
    }

    /**
     * Check if a player can access a specific planet based on their rocket tier
     * Usage in KubeJS: PlayerTier.canAccessPlanet(player, planetId)
     */
    public static boolean canAccessPlanet(Player player, String planetId) {
        if (player == null || planetId == null)
            return false;

        PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
        if (capability == null)
            return false;

        // Check if player has discovered the planet
        if (!capability.hasUnlockedPlanet(planetId))
            return false;

        // Check if player has the required rocket tier
        // This would need to be implemented based on the planet's requirements
        // For now, just check if they have any rocket tier
        return capability.getRocketTier() > 0;
    }

    /**
     * Get the required rocket tier for a planet
     * Usage in KubeJS: PlayerTier.getRequiredRocketTier(planetId)
     */
    public static int getRequiredRocketTier(String planetId) {
        if (planetId == null)
            return 0;

        // This would need to be implemented based on the planet's requirements
        // For now, return a placeholder
        return 1; // Placeholder
    }

    /**
     * Get the required suit tier for a planet
     * Usage in KubeJS: PlayerTier.getRequiredSuitTier(planetId)
     */
    public static int getRequiredSuitTier(String planetId) {
        if (planetId == null)
            return 0;

        // This would need to be implemented based on the planet's requirements
        // For now, return a placeholder
        return 1; // Placeholder
    }

    /**
     * Register KubeJS integration when the mod loads
     */
    public static void register() {
        CHEX.LOGGER
                .info("KubeJS integration registered - tier checking and unlocking functions available in JavaScript");
        CHEX.LOGGER.info("Available functions:");
        CHEX.LOGGER.info("  - PlayerTier.hasRocketTier(player, tier)");
        CHEX.LOGGER.info("  - PlayerTier.hasNoduleTier(player, tier)");
        CHEX.LOGGER.info("  - PlayerTier.hasSuitTier(player, tier)");
        CHEX.LOGGER.info("  - PlayerTier.getRocketTier(player)");
        CHEX.LOGGER.info("  - PlayerTier.getNoduleTier(player)");
        CHEX.LOGGER.info("  - PlayerTier.getNoduleMaterial(tier)");
        CHEX.LOGGER.info("  - PlayerTier.getPlayerNoduleMaterial(player)");
        CHEX.LOGGER.info("  - PlayerTier.getSuitTier(player)");
        CHEX.LOGGER.info("  - PlayerTier.hasDiscoveredPlanet(player, planetId)");
        CHEX.LOGGER.info("  - PlayerTier.hasDiscoveredMineral(player, mineralId)");
        CHEX.LOGGER.info("  - PlayerTier.unlockPlanet(player, planetId)");
        CHEX.LOGGER.info("  - PlayerTier.unlockMineral(player, mineralId)");
        CHEX.LOGGER.info("  - PlayerTier.unlockRocketTier(player, tier)");
        CHEX.LOGGER.info("  - PlayerTier.unlockNoduleTier(player, tier)");
        CHEX.LOGGER.info("  - PlayerTier.unlockSuitTier(player, tier)");
        CHEX.LOGGER.info("  - PlayerTier.getDiscoveredPlanets(player)");
        CHEX.LOGGER.info("  - PlayerTier.getDiscoveredMinerals(player)");
        CHEX.LOGGER.info("  - PlayerTier.canAccessPlanet(player, planetId)");
        CHEX.LOGGER.info("  - PlayerTier.getRequiredRocketTier(planetId)");
        CHEX.LOGGER.info("  - PlayerTier.getRequiredSuitTier(planetId)");
    }
}
