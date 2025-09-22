package com.netroaki.chex.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class ArrakisBlockProperties {
    // Base properties for Arrakis blocks
    public static final BlockBehaviour.Properties ARRAKITE_SANDSTONE_PROPERTIES = BlockBehaviour.Properties
        .of(Material.STONE, MaterialColor.SAND)
        .strength(0.8f, 0.8f)
        .sound(SoundType.STONE);
        
    public static final BlockBehaviour.Properties SPICE_NODE_PROPERTIES = BlockBehaviour.Properties
        .of(Material.SAND, MaterialColor.COLOR_ORANGE)
        .strength(0.5f)
        .lightLevel(state -> 5)
        .sound(SoundType.SAND);
        
    public static final BlockBehaviour.Properties CRYSTALLINE_SALT_PROPERTIES = BlockBehaviour.Properties
        .of(Material.ICE, MaterialColor.QUARTZ)
        .strength(0.5f)
        .friction(0.98f)
        .sound(SoundType.GLASS);
        
    public static final BlockBehaviour.Properties ASH_STONE_PROPERTIES = BlockBehaviour.Properties
        .of(Material.STONE, MaterialColor.COLOR_GRAY)
        .strength(1.5f, 6.0f)
        .sound(SoundType.STONE);
        
    public static final BlockBehaviour.Properties DUNE_GLASS_PROPERTIES = BlockBehaviour.Properties
        .of(Material.GLASS, MaterialColor.SAND)
        .strength(0.3f)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .isValidSpawn((state, world, pos, type) -> false)
        .isRedstoneConductor((state, world, pos) -> false)
        .isSuffocating((state, world, pos) -> false)
        .isViewBlocking((state, world, pos) -> false);
        
    public static final BlockBehaviour.Properties SIETCHROCK_PROPERTIES = BlockBehaviour.Properties
        .of(Material.STONE, MaterialColor.TERRACOTTA_BROWN)
        .strength(2.0f, 10.0f)
        .sound(SoundType.STONE);
}
