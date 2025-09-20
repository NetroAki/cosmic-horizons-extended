package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.blocks.RingworldWallBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry for CHEX block entities
 */
public class CHEXBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, CHEX.MOD_ID);

    public static final RegistryObject<BlockEntityType<RingworldWallBlockEntity>> RINGWORLD_WALL = BLOCK_ENTITIES
            .register("ringworld_wall", () -> BlockEntityType.Builder.of(RingworldWallBlockEntity::new,
                    com.netroaki.chex.registry.blocks.CHEXBlocks.RINGWORLD_WALL.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<com.netroaki.chex.ring.blockentity.ArcSceneryBlockEntity>> ARC_SCENERY = BLOCK_ENTITIES
            .register("arc_scenery",
                    () -> BlockEntityType.Builder.of(com.netroaki.chex.ring.blockentity.ArcSceneryBlockEntity::new,
                            com.netroaki.chex.registry.blocks.CHEXBlocks.ARC_SCENERY.get())
                            .build(null));
}
