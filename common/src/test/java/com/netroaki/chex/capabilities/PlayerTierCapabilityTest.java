package com.netroaki.chex.capabilities;

import com.netroaki.chex.registry.NoduleTiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTierCapabilityTest {

    private PlayerTierCapability capability;
    private static final String TEST_PLANET = "test:test_planet";
    private static final String TEST_MINERAL = "test_mineral";

    @BeforeEach
    void setUp() {
        capability = new PlayerTierCapability();
    }

    @Test
    void testInitialState() {
        assertEquals(1, capability.getRocketTier());
        assertEquals(1, capability.getSuitTier());
        assertTrue(capability.getUnlockedPlanets().contains("cosmos:earth_moon"));
        assertTrue(capability.getDiscoveredMinerals().contains("iron"));
        assertFalse(capability.hasMilestone(PlayerTierCapability.MILESTONE_FIRST_LAUNCH));
    }

    @Test
    void testRocketTierManagement() {
        // Test setting valid tier
        capability.setRocketTier(3);
        assertEquals(3, capability.getRocketTier());

        // Test tier clamping (min)
        capability.setRocketTier(0);
        assertEquals(1, capability.getRocketTier());

        // Test tier clamping (max)
        capability.setRocketTier(15);
        assertEquals(10, capability.getRocketTier());
    }

    @Test
    void testSuitTierManagement() {
        // Test setting valid tier
        capability.setSuitTier(3);
        assertEquals(3, capability.getSuitTier());

        // Test tier clamping (min)
        capability.setSuitTier(0);
        assertEquals(1, capability.getSuitTier());

        // Test tier clamping (max)
        capability.setSuitTier(10);
        assertEquals(5, capability.getSuitTier());
    }

    @Test
    void testUnlockRocketTier() {
        // Test unlocking higher tier
        assertTrue(capability.unlockRocketTier(2));
        assertEquals(2, capability.getRocketTier());

        // Test unlocking same or lower tier
        assertFalse(capability.unlockRocketTier(1));
        assertFalse(capability.unlockRocketTier(2));
        assertEquals(2, capability.getRocketTier());

        // Test max tier
        capability.setRocketTier(9);
        assertTrue(capability.unlockRocketTier(10));
        assertFalse(capability.unlockRocketTier(11)); // Shouldn't go beyond max
    }

    @Test
    void testUnlockSuitTier() {
        // Test unlocking higher tier
        assertTrue(capability.unlockSuitTier(2));
        assertEquals(2, capability.getSuitTier());

        // Test unlocking same or lower tier
        assertFalse(capability.unlockSuitTier(1));
        assertFalse(capability.unlockSuitTier(2));
        assertEquals(2, capability.getSuitTier());

        // Test max tier
        capability.setSuitTier(4);
        assertTrue(capability.unlockSuitTier(5));
        assertFalse(capability.unlockSuitTier(6)); // Shouldn't go beyond max
    }

    @Test
    void testPlanetUnlocking() {
        assertFalse(capability.hasUnlockedPlanet(TEST_PLANET));
        capability.unlockPlanet(TEST_PLANET);
        assertTrue(capability.hasUnlockedPlanet(TEST_PLANET));
        assertTrue(capability.getUnlockedPlanets().contains(TEST_PLANET));
    }

    @Test
    void testMineralDiscovery() {
        assertFalse(capability.hasDiscoveredMineral(TEST_MINERAL));
        capability.discoverMineral(TEST_MINERAL);
        assertTrue(capability.hasDiscoveredMineral(TEST_MINERAL));
        assertTrue(capability.getDiscoveredMinerals().contains(TEST_MINERAL));
    }

    @Test
    void testMilestoneTracking() {
        int milestone = PlayerTierCapability.MILESTONE_FIRST_LAUNCH;
        assertFalse(capability.hasMilestone(milestone));
        
        capability.setMilestone(milestone);
        assertTrue(capability.hasMilestone(milestone));
        
        capability.clearMilestone(milestone);
        assertFalse(capability.hasMilestone(milestone));
    }

    @Test
    void testNbtSerialization() {
        // Set up test data
        capability.setRocketTier(3);
        capability.setSuitTier(2);
        capability.unlockPlanet(TEST_PLANET);
        capability.discoverMineral(TEST_MINERAL);
        capability.setMilestone(PlayerTierCapability.MILESTONE_FIRST_LAUNCH);

        // Serialize
        CompoundTag nbt = capability.serializeNBT();
        
        // Create new instance and deserialize
        PlayerTierCapability newCap = new PlayerTierCapability();
        newCap.deserializeNBT(nbt);

        // Verify data was preserved
        assertEquals(3, newCap.getRocketTier());
        assertEquals(2, newCap.getSuitTier());
        assertTrue(newCap.hasUnlockedPlanet(TEST_PLANET));
        assertTrue(newCap.hasDiscoveredMineral(TEST_MINERAL));
        assertTrue(newCap.hasMilestone(PlayerTierCapability.MILESTONE_FIRST_LAUNCH));
    }

    @Test
    void testCanAccessNoduleTier() {
        capability.setRocketTier(3);
        assertTrue(capability.canAccessNoduleTier(NoduleTiers.T1));
        assertTrue(capability.canAccessNoduleTier(NoduleTiers.T3));
        assertFalse(capability.canAccessNoduleTier(NoduleTiers.T4));
    }

    @Test
    void testCanAccessSuitTier() {
        capability.setSuitTier(2);
        assertTrue(capability.canAccessSuitTier(1));
        assertTrue(capability.canAccessSuitTier(2));
        assertFalse(capability.canAccessSuitTier(3));
    }

    @Test
    void testCanEnterDimension() {
        // Test with unlocked planet
        capability.unlockPlanet(TEST_PLANET);
        assertTrue(capability.canEnterDimension(new ResourceLocation(TEST_PLANET)));
        
        // Test with locked planet
        assertFalse(capability.canEnterDimension(new ResourceLocation("locked:planet")));
    }

    @Test
    void testResetProgression() {
        // Set up test data
        capability.setRocketTier(5);
        capability.setSuitTier(3);
        capability.unlockPlanet(TEST_PLANET);
        capability.discoverMineral(TEST_MINERAL);
        capability.setMilestone(PlayerTierCapability.MILESTONE_FIRST_LAUNCH);

        // Reset
        capability.resetProgression();

        // Verify reset
        assertEquals(1, capability.getRocketTier());
        assertEquals(1, capability.getSuitTier());
        assertTrue(capability.getUnlockedPlanets().contains("cosmos:earth_moon"));
        assertFalse(capability.getUnlockedPlanets().contains(TEST_PLANET));
        assertTrue(capability.getDiscoveredMinerals().contains("iron"));
        assertFalse(capability.getDiscoveredMinerals().contains(TEST_MINERAL));
        assertFalse(capability.hasMilestone(PlayerTierCapability.MILESTONE_FIRST_LAUNCH));
    }
}
