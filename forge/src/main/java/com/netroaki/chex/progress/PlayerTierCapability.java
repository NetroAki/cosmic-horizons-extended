package com.netroaki.chex.progress;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = "cosmic_horizons_extended")
public class PlayerTierCapability implements INBTSerializable<CompoundTag> {

    public static final Capability<PlayerTierCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("cosmic_horizons_extended",
            "player_tier");

    private int rocketTier = 1; // Default to tier 1
    private BitSet milestones = new BitSet(32); // Track various achievements

    public PlayerTierCapability() {
    }

    public int getRocketTier() {
        return rocketTier;
    }

    public void setRocketTier(int tier) {
        this.rocketTier = Math.max(1, Math.min(10, tier)); // Clamp between 1-10
    }

    public boolean hasMilestone(int milestone) {
        return milestones.get(milestone);
    }

    public void setMilestone(int milestone) {
        milestones.set(milestone);
    }

    public void clearMilestone(int milestone) {
        milestones.clear(milestone);
    }

    public BitSet getMilestones() {
        return (BitSet) milestones.clone();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("rocketTier", rocketTier);

        // Serialize BitSet as byte array
        byte[] milestoneBytes = milestones.toByteArray();
        tag.putByteArray("milestones", milestoneBytes);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        rocketTier = tag.getInt("rocketTier");
        if (rocketTier < 1 || rocketTier > 10) {
            rocketTier = 1; // Reset to default if invalid
        }

        // Deserialize BitSet from byte array
        if (tag.contains("milestones", Tag.TAG_BYTE_ARRAY)) {
            byte[] milestoneBytes = tag.getByteArray("milestones");
            milestones = BitSet.valueOf(milestoneBytes);
        } else {
            milestones = new BitSet(32);
        }
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final PlayerTierCapability capability = new PlayerTierCapability();
        private final LazyOptional<PlayerTierCapability> lazyOptional = LazyOptional.of(() -> capability);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return INSTANCE.orEmpty(cap, lazyOptional);
        }

        @Override
        public CompoundTag serializeNBT() {
            return capability.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            capability.deserializeNBT(nbt);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Player> event) {
        event.addCapability(ID, new Provider());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // On death, keep the player's tier but reset milestones
            event.getOriginal().getCapability(INSTANCE).ifPresent(oldCap -> {
                event.getEntity().getCapability(INSTANCE).ifPresent(newCap -> {
                    newCap.setRocketTier(oldCap.getRocketTier());
                    // Don't copy milestones on death - they need to be re-earned
                });
            });
        } else {
            // On dimension change or other non-death events, copy everything
            event.getOriginal().getCapability(INSTANCE).ifPresent(oldCap -> {
                event.getEntity().getCapability(INSTANCE).ifPresent(newCap -> {
                    newCap.setRocketTier(oldCap.getRocketTier());
                    newCap.milestones = (BitSet) oldCap.getMilestones().clone();
                });
            });
        }
    }

    public static PlayerTierCapability get(Player player) {
        return player.getCapability(INSTANCE).orElse(new PlayerTierCapability());
    }
}
