package com.netroaki.chex.item.tool;

import com.netroaki.chex.item.material.CrystalToolMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalPickaxeItem extends CrystalToolItem {
  private static final int VEIN_MINE_RADIUS = 1; // 3x3 area
  private static final int VEIN_MINE_DEPTH = 2; // How deep to mine

  public CrystalPickaxeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
    super(
        (CrystalToolMaterial) tier,
        attackDamage,
        attackSpeed,
        properties,
        BlockTags.MINEABLE_WITH_PICKAXE);
  }

  @Override
  public boolean mineBlock(
      ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
    if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
      // Handle normal block breaking
      stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(entity.getUsedItemHand()));

      // Special ability based on material
      if (material == CrystalToolMaterial.RARE_CRYSTAL
          || material == CrystalToolMaterial.ENERGIZED_CRYSTAL) {
        if (entity.isShiftKeyDown()) {
          // Shift + right-click to activate special ability
          if (entity instanceof Player player) {
            if (player.getFoodData().getFoodLevel() > 0) {
              // Consume hunger for ability
              player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);

              // Play sound effect
              level.playSound(
                  null, pos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);

              // Mine in a 3x3 area
              for (int x = -VEIN_MINE_RADIUS; x <= VEIN_MINE_RADIUS; x++) {
                for (int y = 0; y <= VEIN_MINE_RADIUS; y++) {
                  for (int z = -VEIN_MINE_RADIUS; z <= VEIN_MINE_RADIUS; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the center block

                    BlockPos targetPos = pos.offset(x, y, z);
                    BlockState targetState = level.getBlockState(targetPos);

                    // Only break the same type of block
                    if (targetState.is(state.getBlock())
                        && !level.isEmptyBlock(targetPos)
                        && targetState.getDestroySpeed(level, targetPos) > 0) {

                      // For energized crystal, mine deeper veins
                      if (material == CrystalToolMaterial.ENERGIZED_CRYSTAL) {
                        mineVein(level, targetPos, state.getBlock(), 0);
                      } else {
                        level.destroyBlock(targetPos, true, entity);
                      }
                    }
                  }
                }
              }

              // Add cooldown to prevent spam
              if (material == CrystalToolMaterial.ENERGIZED_CRYSTAL) {
                player.getCooldowns().addCooldown(this, 100); // 5 seconds
              } else {
                player.getCooldowns().addCooldown(this, 200); // 10 seconds
              }
            }
          }
        }
      }
    }
    return true;
  }

  private void mineVein(Level level, BlockPos pos, Block targetBlock, int depth) {
    if (depth >= VEIN_MINE_DEPTH || level.isEmptyBlock(pos)) {
      return;
    }

    BlockState state = level.getBlockState(pos);
    if (state.is(targetBlock)) {
      level.destroyBlock(pos, true);

      // Check adjacent blocks
      for (Direction direction : Direction.values()) {
        mineVein(level, pos.relative(direction), targetBlock, depth + 1);
      }
    }
  }
}
