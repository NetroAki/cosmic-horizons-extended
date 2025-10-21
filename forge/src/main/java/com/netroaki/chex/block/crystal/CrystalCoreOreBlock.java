package com.netroaki.chex.block.crystal;

import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class CrystalCoreOreBlock extends DropExperienceBlock {
  private final int minXp;
  private final int maxXp;

  public CrystalCoreOreBlock(int minXp, int maxXp) {
    super(
        Properties.of()
            .mapColor(MapColor.COLOR_CYAN)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 5.0F)
            .lightLevel(state -> 7) // Glow in the dark
        );
    this.minXp = minXp;
    this.maxXp = maxXp;
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    // Get the tool used to break the block
    ItemStack tool = builder.getParameter(LootContextParams.TOOL);

    // If silk touch is used, drop the ore block itself
    if (tool != null
        && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
      return Collections.singletonList(new ItemStack(this));
    }

    // Otherwise, drop crystal shards with fortune
    int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
    int count = 1 + builder.getLevel().random.nextInt(2 + fortune);

    // TODO: Fix when CHEXItems is properly implemented
    return Collections.singletonList(new ItemStack(Items.QUARTZ, count));
  }

  @Override
  public int getExpDrop(
      BlockState state,
      net.minecraft.world.level.LevelReader level,
      RandomSource random,
      BlockPos pos,
      int fortuneLevel,
      int silkTouchLevel) {
    return silkTouchLevel == 0 ? random.nextIntBetweenInclusive(minXp, maxXp) : 0;
  }
}
