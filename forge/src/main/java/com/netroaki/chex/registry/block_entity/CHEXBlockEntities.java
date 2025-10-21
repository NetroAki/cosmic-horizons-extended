package com.netroaki.chex.registry.block_entity;

import com.netroaki.chex.CHEX;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CHEXBlockEntities {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CHEX.MOD_ID);

  // Crystal Block Entity - commented out until CHEXBlocks is properly implemented
  // public static final RegistryObject<BlockEntityType<CrystalBlockEntity>>
  // CRYSTAL_BLOCK =
  // BLOCK_ENTITIES.register(
  // "crystal_block",
  // () ->
  // BlockEntityType.Builder.of(CrystalBlockEntity::new,
  // CHEXBlocks.CRYSTAL_BLOCK.get())
  // .build(null));

  public static void register(IEventBus eventBus) {
    BLOCK_ENTITIES.register(eventBus);
  }
}
