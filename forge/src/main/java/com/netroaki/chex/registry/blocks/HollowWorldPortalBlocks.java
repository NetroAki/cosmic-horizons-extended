package com.netroaki.chex.registry.blocks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.blocks.HollowWorldPortalFrameBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HollowWorldPortalBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);

  // Portal Frame Block
  public static final RegistryObject<Block> PORTAL_FRAME =
      BLOCKS.register(
          "hollow_portal_frame",
          () ->
              new HollowWorldPortalFrameBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(50.0F, 1200.0F)
                      .sound(SoundType.STONE)
                      .lightLevel(
                          state -> state.getValue(HollowWorldPortalFrameBlock.ACTIVATED) ? 7 : 0)
                      .noOcclusion()));

  // Portal Block - commented out until HollowWorldPortalBlock is implemented
  // public static final RegistryObject<Block> PORTAL =
  // BLOCKS.register(
  // "hollow_portal",
  // () ->
  // new HollowWorldPortalBlock(
  // BlockBehaviour.Properties.of()
  // .mapColor(MapColor.COLOR_PURPLE)
  // .noCollission()
  // .strength(-1.0F, 3600000.0F)
  // .noLootTable()
  // .lightLevel(state -> 11)));

  // Helper method to get the portal block
  // public static HollowWorldPortalBlock portal() {
  // return (HollowWorldPortalBlock) PORTAL.get();
  // }

  // Register all blocks and their items
  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);

    // Register block items - commented out until CHEXItems is properly implemented
    // CHEXItems.ITEMS.register(
    // "hollow_portal_frame", () -> new BlockItem(PORTAL_FRAME.get(), new
    // Item.Properties()));
  }
}
