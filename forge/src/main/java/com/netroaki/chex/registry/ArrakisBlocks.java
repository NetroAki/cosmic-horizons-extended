package com.netroaki.chex.registry;

import com.netroaki.chex.block.ArrakisBlock;
import com.netroaki.chex.block.ArrakisPlantBlock;
import com.netroaki.chex.block.SpiceCactusBlock;
import com.netroaki.chex.block.IceReedBlock;
import com.netroaki.chex.CHEX;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArrakisBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);

    // Arrakite Blocks
    public static final RegistryObject<Block> ARAKITE_SANDSTONE = registerBlock("arakite_sandstone",
        () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)
            .strength(0.8f, 0.8f)
            .sound(SoundType.STONE)
            .mapColor(MapColor.SAND)));

    public static final RegistryObject<Block> ARAKITE_SANDSTONE_CHISELED = registerBlock("chiseled_arakite_sandstone",
        () -> new Block(BlockBehaviour.Properties.copy(ARAKITE_SANDSTONE.get())));

    public static final RegistryObject<Block> ARAKITE_SANDSTONE_SMOOTH = registerBlock("smooth_arakite_sandstone",
        () -> new Block(BlockBehaviour.Properties.copy(ARAKITE_SANDSTONE.get())
            .friction(0.8f)));

    // Spice Blocks
    public static final RegistryObject<Block> SPICE_NODE = registerBlock("spice_node",
        () -> new ArrakisBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE)
            .strength(3.0f, 3.0f)
            .lightLevel(state -> 7)
            .sound(SoundType.STONE)
            .mapColor(MapColor.COLOR_ORANGE),
            false, 0.0f));

    public static final RegistryObject<Block> SPICE_CRYSTAL = registerBlock("spice_crystal",
        () -> new ArrakisBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)
            .strength(1.5f, 1.5f)
            .lightLevel(state -> 5)
            .sound(SoundType.AMETHYST)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .mapColor(MapColor.COLOR_RED),
            false, 0.0f);
            
    // Crystalline Salt
    public static final RegistryObject<Block> CRYSTALLINE_SALT_ORE = registerBlock("crystalline_salt_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
            .strength(3.0f, 3.0f)
            .sound(SoundType.AMETHYST)
            .mapColor(MapColor.QUARTZ)));
            
    public static final RegistryObject<Block> CRYSTALLINE_SALT_BLOCK = registerBlock("crystalline_salt_block",
        () -> new Block(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK)
            .strength(0.8f, 0.8f)
            .sound(SoundType.AMETHYST)
            .lightLevel(state -> 3)
            .mapColor(MapColor.QUARTZ)));
            
    // Ash Stone
    public static final RegistryObject<Block> ASH_STONE = registerBlock("ash_stone",
        () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
            .strength(1.5f, 6.0f)
            .sound(SoundType.STONE)
            .mapColor(MapColor.COLOR_GRAY)));
            
    public static final RegistryObject<Block> POLISHED_ASH_STONE = registerBlock("polished_ash_stone",
        () -> new Block(BlockBehaviour.Properties.copy(ASH_STONE.get())
            .friction(0.9f)));
            
    public static final RegistryObject<StairBlock> ASH_STONE_STAIRS = registerBlock("ash_stone_stairs",
        () -> new StairBlock(() -> ASH_STONE.get().defaultBlockState(), 
            BlockBehaviour.Properties.copy(ASH_STONE.get())));
            
    public static final RegistryObject<SlabBlock> ASH_STONE_SLAB = registerBlock("ash_stone_slab",
        () -> new SlabBlock(BlockBehaviour.Properties.copy(ASH_STONE.get())));
            
    // Flora
    public static final RegistryObject<Block> SPICE_CACTUS = registerBlock("spice_cactus",
        () -> new SpiceCactusBlock(BlockBehaviour.Properties.copy(Blocks.CACTUS)
            .mapColor(MapColor.COLOR_ORANGE)
            .noOcclusion()
            .randomTicks()
            .strength(0.4F)
            .sound(SoundType.WOOL)));
            
    public static final RegistryObject<Block> ICE_REEDS = registerBlock("ice_reeds",
        () -> new IceReedBlock(BlockBehaviour.Properties.copy(Blocks.SUGAR_CANE)
            .mapColor(MapColor.ICE)
            .noOcclusion()
            .randomTicks()
            .noCollission()
            .instabreak()
            .sound(SoundType.WET_GRASS)));
            
    public static final RegistryObject<Block> DESERT_SHRUB = registerBlock("desert_shrub",
        () -> new ArrakisPlantBlock(BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH)
            .mapColor(MapColor.SAND)
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)));
            
    // Environment Blocks
    public static final RegistryObject<Block> DUNE_GLASS = registerBlock("dune_glass",
        () -> new ArrakisBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)
            .strength(0.5f, 0.5f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isViewDropping((state, level, pos) -> false),
            true, 1.0f));
            
    public static final RegistryObject<Block> DUNE_GLASS_PANE = registerBlock("dune_glass_pane",
        () -> new IronBarsBlock(BlockBehaviour.Properties.copy(DUNE_GLASS.get())
            .noOcclusion()
            .strength(0.3f, 0.3f)));
            
    // Sietch Structures
    public static final RegistryObject<Block> SIETCH_STONE = registerBlock("sietch_stone",
        () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)
            .strength(2.0f, 6.0f)
            .sound(SoundType.STONE)
            .mapColor(MapColor.TERRACOTTA_BROWN)));
            
    public static final RegistryObject<Block> SIETCH_STONE_CARVED = registerBlock("sietch_stone_carved",
        () -> new Block(BlockBehaviour.Properties.copy(SIETCH_STONE.get())
            .mapColor(MapColor.TERRACOTTA_ORANGE)));
            
    public static final RegistryObject<Block> SIETCH_STONE_MOSAIC = registerBlock("sietch_stone_mosaic",
        () -> new Block(BlockBehaviour.Properties.copy(SIETCH_STONE.get())
            .sound(SoundType.DEEPSLATE_TILES)));


    public static final RegistryObject<Block> CRYSTALLINE_SALT = registerBlock("crystalline_salt",
        () -> new ArrakisBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK)
            .strength(0.8f, 0.8f)
            .sound(SoundType.AMETHYST)
            .mapColor(MapColor.QUARTZ),
            true, 0.5f));

    // Utility Blocks
    public static final RegistryObject<Block> CONDENSATION_COLLECTOR = registerBlock("condensation_collector",
        () -> new ArrakisBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)
            .strength(2.0f, 2.0f)
            .sound(SoundType.METAL)
            .mapColor(MapColor.METAL),
            false, 0.0f));
            
    // Flora
    public static final RegistryObject<Block> SPICE_CACTUS = registerBlock("spice_cactus",
        () -> new SpiceCactusBlock(BlockBehaviour.Properties.copy(Blocks.CACTUS)
            .mapColor(MapColor.COLOR_RED)
            .noOcclusion()
            .randomTicks()
            .strength(0.4F)
            .sound(SoundType.WOOL)));
            
    public static final RegistryObject<Block> DESERT_SHRUB = registerBlock("desert_shrub",
        () -> new ArrakisPlantBlock(BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH)
            .mapColor(MapColor.SAND)
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)));
            
    public static final RegistryObject<Block> SPICE_BLOOM = registerBlock("spice_bloom",
        () -> new FlowerBlock(MobEffects.REGENERATION, 8, 
            BlockBehaviour.Properties.copy(Blocks.POPPY)
                .mapColor(MapColor.COLOR_RED)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)));
                
    public static final RegistryObject<Block> POTTED_SPICE_BLOOM = BLOCKS.register("potted_spice_bloom",
        () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, 
            SPICE_BLOOM,
            BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, java.util.function.Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }
}
