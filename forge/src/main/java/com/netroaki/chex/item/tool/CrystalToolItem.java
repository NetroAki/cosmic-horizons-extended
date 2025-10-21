package com.netroaki.chex.item.tool;

import com.netroaki.chex.item.material.CrystalToolMaterial;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class CrystalToolItem extends DiggerItem {
  public final CrystalToolMaterial material;
  private final Set<Block> effectiveBlocks;
  private final float attackDamage;
  private final float attackSpeed;

  public CrystalToolItem(
      CrystalToolMaterial material,
      float attackDamage,
      float attackSpeed,
      Properties properties,
      TagKey<Block> effectiveBlocks) {
    super(attackDamage, attackSpeed, material, effectiveBlocks, properties);
    this.material = material;
    this.effectiveBlocks = null; // Not used with TagKey
    this.attackDamage = attackDamage + material.getAttackDamageBonus();
    this.attackSpeed = attackSpeed;
  }

  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

    // Special ability based on material
    if (material == CrystalToolMaterial.ENERGIZED_CRYSTAL) {
      // Chance to strike lightning on hit
      if (attacker.getRandom().nextFloat() < 0.1f) {
        if (attacker.level() instanceof ServerLevel level) {
          BlockPos pos = target.blockPosition();
          if (level.canSeeSky(pos)) {
            // TODO: Fix when strikeLightning method is available
            // level.strikeLightning(target.position());
          }
        }
      }
    }

    return true;
  }

  @Override
  public boolean mineBlock(
      ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
    if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
      stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));

      // Special ability for rare and energized crystals
      if (material != CrystalToolMaterial.CRYSTAL) {
        // Chance to drop extra items
        if (entity.getRandom().nextFloat()
            < (material == CrystalToolMaterial.RARE_CRYSTAL ? 0.15f : 0.25f)) {
          BlockState stateCopy = state.getBlock().defaultBlockState();
          Block.popResource(level, pos, new ItemStack(stateCopy.getBlock().asItem()));
        }
      }
    }
    return true;
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    if (isCorrectToolForDrops(state)) {
      float speed = super.getDestroySpeed(stack, state);
      // Speed bonus when in light
      if (stack.getEntityRepresentation() != null
          && stack
                  .getEntityRepresentation()
                  .level()
                  .getMaxLocalRawBrightness(stack.getEntityRepresentation().blockPosition())
              > 7) {
        speed *= 1.5f;
      }
      return speed;
    }
    return 1.0F;
  }

  @Override
  public boolean isCorrectToolForDrops(BlockState state) {
    return super.isCorrectToolForDrops(state);
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
    return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction)
        || ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction)
        || ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction)
        || ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction);
  }
}
