package com.netroaki.chex.gt;

import java.util.Map;

/**
 * Loader-neutral GTCEu bridge interface. Forge will provide the implementation.
 */
public interface GtBridge {
    boolean isAvailable();

    String getGTTierForNoduleTier(int noduleTier);

    String getVeinSizeForNoduleTier(int noduleTier);

    int getOreCountForNoduleTier(int noduleTier);

    int[] getYRangeForNoduleTier(int noduleTier);

    void logIntegrationStatus();

    Map<String, Object> getMineralDataForPlanet(String planetId);

    boolean isMineralAvailableOnPlanet(String planetId, String mineral);

    int getMineralGTTier(String mineral);
}
