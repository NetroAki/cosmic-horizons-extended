package com.netroaki.chex.datagen;

import com.netroaki.chex.registry.CHEXBlocks;
import java.util.Collections;
import java.util.function.BiConsumer;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

public class PandoraLootTableProvider implements LootTableSubProvider {
  
  @Override
  public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
    // Example loot table for Pandora blocks
    // Add more blocks as needed
    // dropSelf(consumer, CHEXBlocks.PANDORITE_BLOCK.get());
  }
  
  protected static void dropSelf(BiConsumer<ResourceLocation, LootTable.Builder> consumer, Block block) {
    consumer.accept(
        block.getLootTable(),
        LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(block))
            )
    );
  }
  
  protected static void dropWithFortune(BiConsumer<ResourceLocation, LootTable.Builder> consumer, Block block, Item item, float min, float max) {
    consumer.accept(
        block.getLootTable(),
        LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(
                        LootItem.lootTableItem(item)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    )
            )
    );
  }
}
