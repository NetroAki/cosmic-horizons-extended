package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import com.netroaki.chex.registry.items.CHEXItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Creative mode tabs for CHEX mod items and blocks
 */
public class CHEXCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, CHEX.MOD_ID);

    // Main CHEX tab
    public static final RegistryObject<CreativeModeTab> CHEX_TAB = CREATIVE_MODE_TABS.register("chex_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.chex_tab"))
                    .icon(() -> new ItemStack(CHEXItems.ROCKET_CONTROLLER.get()))
                    .displayItems((parameters, output) -> {
                        // Rocket Components
                        output.accept(CHEXItems.ROCKET_CONTROLLER.get());
                        output.accept(CHEXItems.FUEL_GAUGE.get());

                        // Blocks
                        output.accept(CHEXBlocks.ROCKET_ASSEMBLY_TABLE.get());
                        output.accept(CHEXBlocks.FUEL_REFINERY.get());
                        // World placeholder blocks
                        output.accept(CHEXBlocks.ARRAKIS_SAND.get());
                        output.accept(CHEXBlocks.ARRAKIS_ROCK.get());
                        output.accept(CHEXBlocks.INFERNO_STONE.get());
                        output.accept(CHEXBlocks.INFERNO_ASH.get());
                        output.accept(CHEXBlocks.CRYSTALIS_CRYSTAL.get());
                        output.accept(CHEXBlocks.CRYSTALIS_CLEAR.get());
                        output.accept(CHEXBlocks.AQUA_MUNDUS_STONE.get());
                        output.accept(CHEXBlocks.AQUA_DARK_PRISM.get());
                        output.accept(CHEXBlocks.PANDORA_GRASS.get());
                        output.accept(CHEXBlocks.PANDORA_BLOOM.get());
                        output.accept(CHEXBlocks.NEUTRON_STAR_BASALT.get());
                        output.accept(CHEXBlocks.NEUTRON_STAR_PLATE.get());

                        // Fuel Items
                        output.accept(CHEXItems.KEROSENE_BUCKET.get());
                        output.accept(CHEXItems.RP1_BUCKET.get());
                        output.accept(CHEXItems.LOX_BUCKET.get());
                        output.accept(CHEXItems.LH2_BUCKET.get());
                    })
                    .build());

    // Space Suits tab
    public static final RegistryObject<CreativeModeTab> SPACE_SUITS_TAB = CREATIVE_MODE_TABS.register("space_suits_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.space_suits_tab"))
                    .icon(() -> new ItemStack(CHEXItems.SPACE_SUIT_HELMET_T1.get()))
                    .displayItems((parameters, output) -> {
                        // Tier 1 Space Suits
                        output.accept(CHEXItems.SPACE_SUIT_HELMET_T1.get());
                        output.accept(CHEXItems.SPACE_SUIT_CHESTPLATE_T1.get());
                        output.accept(CHEXItems.SPACE_SUIT_LEGGINGS_T1.get());
                        output.accept(CHEXItems.SPACE_SUIT_BOOTS_T1.get());

                        // Tier 2 Space Suits
                        output.accept(CHEXItems.SPACE_SUIT_HELMET_T2.get());
                        output.accept(CHEXItems.SPACE_SUIT_CHESTPLATE_T2.get());
                        output.accept(CHEXItems.SPACE_SUIT_LEGGINGS_T2.get());
                        output.accept(CHEXItems.SPACE_SUIT_BOOTS_T2.get());

                        // Tier 3 Space Suits
                        output.accept(CHEXItems.SPACE_SUIT_HELMET_T3.get());
                        output.accept(CHEXItems.SPACE_SUIT_CHESTPLATE_T3.get());
                        output.accept(CHEXItems.SPACE_SUIT_LEGGINGS_T3.get());
                        output.accept(CHEXItems.SPACE_SUIT_BOOTS_T3.get());

                        // Tier 4 Space Suits
                        output.accept(CHEXItems.SPACE_SUIT_HELMET_T4.get());
                        output.accept(CHEXItems.SPACE_SUIT_CHESTPLATE_T4.get());
                        output.accept(CHEXItems.SPACE_SUIT_LEGGINGS_T4.get());
                        output.accept(CHEXItems.SPACE_SUIT_BOOTS_T4.get());

                        // Tier 5 Space Suits
                        output.accept(CHEXItems.SPACE_SUIT_HELMET_T5.get());
                        output.accept(CHEXItems.SPACE_SUIT_CHESTPLATE_T5.get());
                        output.accept(CHEXItems.SPACE_SUIT_LEGGINGS_T5.get());
                        output.accept(CHEXItems.SPACE_SUIT_BOOTS_T5.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
        CHEX.LOGGER.info("CHEX creative mode tabs registered");
    }
}