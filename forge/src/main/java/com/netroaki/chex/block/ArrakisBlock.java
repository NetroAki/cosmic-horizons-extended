package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ArrakisBlock extends Block {
    private final boolean causesDamage;
    private final float damageAmount;

    public ArrakisBlock(Properties properties, boolean causesDamage, float damageAmount) {
        super(properties);
        this.causesDamage = causesDamage;
        this.damageAmount = damageAmount;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (causesDamage && !level.isClientSide) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
            if (!entity.fireImmune() && entity.getRandom().nextFloat() < 0.1F) {
                entity.hurt(level.damageSources().hotFloor(), damageAmount);
            }
        }
        super.stepOn(level, pos, state, entity);
    }
}
