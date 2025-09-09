package com.netroaki.chex.registry.blocks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.fluids.CHEXFluids;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXBlocks {

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        CHEX.MOD_ID);

        public static final DeferredRegister<net.minecraft.world.level.block.entity.BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(ForgeRegistries.BLOCK_ENTITY_TYPES, CHEX.MOD_ID);

        // Rocket Assembly and Fuel Processing Blocks
        public static final RegistryObject<Block> ROCKET_ASSEMBLY_TABLE = BLOCKS.register("rocket_assembly_table",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(3.5f)));

        public static final RegistryObject<Block> FUEL_REFINERY = BLOCKS.register("fuel_refinery",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0f)));

        // Fluid blocks
        public static final RegistryObject<LiquidBlock> KEROSENE_FLUID_BLOCK = BLOCKS.register("kerosene_fluid_block",
                        () -> new LiquidBlock(() -> CHEXFluids.KEROSENE.get(),
                                        BlockBehaviour.Properties.copy(Blocks.WATER)));

        public static final RegistryObject<LiquidBlock> RP1_FLUID_BLOCK = BLOCKS.register("rp1_fluid_block",
                        () -> new LiquidBlock(() -> CHEXFluids.RP1.get(),
                                        BlockBehaviour.Properties.copy(Blocks.WATER)));

        public static final RegistryObject<LiquidBlock> LOX_FLUID_BLOCK = BLOCKS.register("lox_fluid_block",
                        () -> new LiquidBlock(() -> CHEXFluids.LOX.get(),
                                        BlockBehaviour.Properties.copy(Blocks.WATER)));

        public static final RegistryObject<LiquidBlock> LH2_FLUID_BLOCK = BLOCKS.register("lh2_fluid_block",
                        () -> new LiquidBlock(() -> CHEXFluids.LH2.get(),
                                        BlockBehaviour.Properties.copy(Blocks.WATER)));

        // Aurelia ringworld wall - unbreakable, black
        public static final RegistryObject<Block> AURELIA_WALL = BLOCKS.register("aurelia_wall",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK)));

        // Register block items
        public static void registerBlockItems() {
                registerBlockItem(ROCKET_ASSEMBLY_TABLE);
                registerBlockItem(FUEL_REFINERY);
                registerBlockItem(AURELIA_WALL);
        }

        private static void registerBlockItem(RegistryObject<Block> block) {
                com.netroaki.chex.registry.items.CHEXItems.ITEMS.register(block.getId().getPath(),
                                () -> new BlockItem(block.get(), new Item.Properties()));
        }
}
