package com.netroaki.chex.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class LoreKnowledgeProvider implements ICapabilitySerializable<CompoundTag> {
  public static final ResourceLocation IDENTIFIER =
      new ResourceLocation("cosmic_horizons_extended", "lore_knowledge");
  public static final Capability<ILoreKnowledge> LORE_KNOWLEDGE_CAPABILITY =
      CapabilityManager.get(new CapabilityToken<>() {});

  private final ILoreKnowledge loreKnowledge = new LoreKnowledgeImpl();
  private final LazyOptional<ILoreKnowledge> optional = LazyOptional.of(() -> loreKnowledge);

  @SubscribeEvent
  public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player) {
      event.addCapability(IDENTIFIER, new LoreKnowledgeProvider());
    }
  }

  @SubscribeEvent
  public static void onPlayerCloned(PlayerEvent.Clone event) {
    if (!event.isWasDeath()) return;

    event.getOriginal().reviveCaps();
    event
        .getOriginal()
        .getCapability(LORE_KNOWLEDGE_CAPABILITY)
        .ifPresent(
            oldStore -> {
              event
                  .getEntity()
                  .getCapability(LORE_KNOWLEDGE_CAPABILITY)
                  .ifPresent(
                      newStore -> {
                        newStore.deserializeNBT(oldStore.serializeNBT());
                      });
            });
    event.getOriginal().invalidateCaps();
  }

  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    // Sync capability data to client on respawn
    if (!event.getEntity().level().isClientSide) {
      syncCapability(event.getEntity());
    }
  }

  @SubscribeEvent
  public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    // Sync capability data to client on login
    if (!event.getEntity().level().isClientSide) {
      syncCapability(event.getEntity());
    }
  }

  @SubscribeEvent
  public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    // Sync capability data to client when changing dimensions
    if (!event.getEntity().level().isClientSide) {
      syncCapability(event.getEntity());
    }
  }

  public static void syncCapability(Player player) {
    if (!player.level().isClientSide
        && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
      player
          .getCapability(LORE_KNOWLEDGE_CAPABILITY)
          .ifPresent(
              cap -> {
                // TODO: Fix when CHEXNetwork and PacketDistributor are implemented
                // CHEXNetwork.INSTANCE.send(
                // PacketDistributor.PLAYER.with(() -> serverPlayer),
                // new LoreKnowledgeSyncPacket(cap));
              });
    }
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(
      @NotNull Capability<T> cap, @Nullable Direction side) {
    return LORE_KNOWLEDGE_CAPABILITY.orEmpty(cap, optional);
  }

  @Override
  public CompoundTag serializeNBT() {
    return loreKnowledge.serializeNBT();
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    loreKnowledge.deserializeNBT(nbt);
  }
}
