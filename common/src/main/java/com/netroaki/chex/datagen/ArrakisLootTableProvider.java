package com.netroaki.chex.datagen;

import com.netroaki.chex.registry.blocks.ArrakisBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArrakisLootTableProvider extends LootTableProvider {
    public ArrakisLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
            new SubProviderEntry(ArrakisBlockLoot::new, LootContextParamSets.BLOCK)
        ));
    }

    private static class ArrakisBlockLoot extends BlockLootSubProvider {
        protected ArrakisBlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            // Arrakite Sandstone
            dropSelf(ArrakisBlocks.ARRAKITE_SANDSTONE.get());
            dropSelf(ArrakisBlocks.CHISELED_ARRAKITE_SANDSTONE.get());
            dropSelf(ArrakisBlocks.SMOOTH_ARRAKITE_SANDSTONE.get());
            
            // Spice Node - drops spice melange item
            add(ArrakisBlocks.SPICE_NODE.get(), createOreDrop(
                ArrakisBlocks.SPICE_NODE.get(),
                CHEXItems.SPICE_MELANGE.get()
            ));
            
            // Crystalline Salt
            dropSelf(ArrakisBlocks.CRYSTALLINE_SALT.get());
            
            // Ash Stone
            dropSelf(ArrakisBlocks.ASH_STONE.get());
            
            // Dune Glass
            dropSelf(ArrakisBlocks.DUNE_GLASS.get());
            
            // Sietchrock
            dropSelf(ArrakisBlocks.SIETCHROCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ArrakisBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .collect(Collectors.toList());
        }
    }
}
