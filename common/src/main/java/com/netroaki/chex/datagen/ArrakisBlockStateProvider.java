package com.netroaki.chex.datagen;

import com.netroaki.chex.registry.blocks.ArrakisBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ArrakisBlockStateProvider extends BlockStateProvider {
    public ArrakisBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Arrakite Sandstone
        simpleBlockWithItem(ArrakisBlocks.ARRAKITE_SANDSTONE.get());
        simpleBlockWithItem(ArrakisBlocks.CHISELED_ARRAKITE_SANDSTONE.get());
        simpleBlockWithItem(ArrakisBlocks.SMOOTH_ARRAKITE_SANDSTONE.get());
        
        // Spice Node
        simpleBlockWithItem(ArrakisBlocks.SPICE_NODE.get(),
            models().cubeAll("spice_node",
                modLoc("block/spice_node")).renderType("cutout"));
        
        // Crystalline Salt
        simpleBlockWithItem(ArrakisBlocks.CRYSTALLINE_SALT.get(),
            models().cubeAll("crystalline_salt",
                modLoc("block/crystalline_salt")).renderType("translucent"));
        
        // Ash Stone
        simpleBlockWithItem(ArrakisBlocks.ASH_STONE.get());
        
        // Dune Glass
        simpleBlockWithItem(ArrakisBlocks.DUNE_GLASS.get(),
            models().cubeAll("dune_glass",
                modLoc("block/dune_glass")).renderType("translucent"));
        
        // Sietchrock
        simpleBlockWithItem(ArrakisBlocks.SIETCHROCK.get());
    }
    
    private void simpleBlockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }
    
    private void simpleBlockWithItem(Block block, ModelFile model) {
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }
}
