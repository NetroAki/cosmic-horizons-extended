package com.netroaki.chex.registry.items;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.fluids.CHEXFluids;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXItems {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CHEX.MOD_ID);

        // Rocket Components
        public static final RegistryObject<Item> ROCKET_CONTROLLER = ITEMS.register("rocket_controller",
                        () -> new NoduleControllerItem(new Item.Properties().stacksTo(1)));

        public static final RegistryObject<Item> FUEL_GAUGE = ITEMS.register("fuel_gauge",
                        () -> new Item(new Item.Properties().stacksTo(1)));

        // Fuel Buckets
        public static final RegistryObject<Item> KEROSENE_BUCKET = ITEMS.register("kerosene_bucket",
                        () -> new BucketItem(() -> CHEXFluids.KEROSENE.get(), new Item.Properties().stacksTo(1)));

        public static final RegistryObject<Item> RP1_BUCKET = ITEMS.register("rp1_bucket",
                        () -> new BucketItem(() -> CHEXFluids.RP1.get(), new Item.Properties().stacksTo(1)));

        public static final RegistryObject<Item> LOX_BUCKET = ITEMS.register("lox_bucket",
                        () -> new BucketItem(() -> CHEXFluids.LOX.get(), new Item.Properties().stacksTo(1)));

        public static final RegistryObject<Item> LH2_BUCKET = ITEMS.register("lh2_bucket",
                        () -> new BucketItem(() -> CHEXFluids.LH2.get(), new Item.Properties().stacksTo(1)));

        // Space Suits - Tier 1
        public static final RegistryObject<Item> SPACE_SUIT_HELMET_T1 = ITEMS.register("space_suit_helmet_t1",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T1, ArmorItem.Type.HELMET,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_CHESTPLATE_T1 = ITEMS.register("space_suit_chestplate_t1",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T1, ArmorItem.Type.CHESTPLATE,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_LEGGINGS_T1 = ITEMS.register("space_suit_leggings_t1",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T1, ArmorItem.Type.LEGGINGS,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_BOOTS_T1 = ITEMS.register("space_suit_boots_t1",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T1, ArmorItem.Type.BOOTS,
                                        new Item.Properties()));

        // Space Suits - Tier 2
        public static final RegistryObject<Item> SPACE_SUIT_HELMET_T2 = ITEMS.register("space_suit_helmet_t2",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T2, ArmorItem.Type.HELMET,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_CHESTPLATE_T2 = ITEMS.register("space_suit_chestplate_t2",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T2, ArmorItem.Type.CHESTPLATE,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_LEGGINGS_T2 = ITEMS.register("space_suit_leggings_t2",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T2, ArmorItem.Type.LEGGINGS,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_BOOTS_T2 = ITEMS.register("space_suit_boots_t2",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T2, ArmorItem.Type.BOOTS,
                                        new Item.Properties()));

        // Space Suits - Tier 3
        public static final RegistryObject<Item> SPACE_SUIT_HELMET_T3 = ITEMS.register("space_suit_helmet_t3",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T3, ArmorItem.Type.HELMET,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_CHESTPLATE_T3 = ITEMS.register("space_suit_chestplate_t3",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T3, ArmorItem.Type.CHESTPLATE,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_LEGGINGS_T3 = ITEMS.register("space_suit_leggings_t3",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T3, ArmorItem.Type.LEGGINGS,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_BOOTS_T3 = ITEMS.register("space_suit_boots_t3",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T3, ArmorItem.Type.BOOTS,
                                        new Item.Properties()));

        // Space Suits - Tier 4
        public static final RegistryObject<Item> SPACE_SUIT_HELMET_T4 = ITEMS.register("space_suit_helmet_t4",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T4, ArmorItem.Type.HELMET,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_CHESTPLATE_T4 = ITEMS.register("space_suit_chestplate_t4",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T4, ArmorItem.Type.CHESTPLATE,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_LEGGINGS_T4 = ITEMS.register("space_suit_leggings_t4",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T4, ArmorItem.Type.LEGGINGS,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_BOOTS_T4 = ITEMS.register("space_suit_boots_t4",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T4, ArmorItem.Type.BOOTS,
                                        new Item.Properties()));

        // Space Suits - Tier 5
        public static final RegistryObject<Item> SPACE_SUIT_HELMET_T5 = ITEMS.register("space_suit_helmet_t5",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T5, ArmorItem.Type.HELMET,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_CHESTPLATE_T5 = ITEMS.register("space_suit_chestplate_t5",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T5, ArmorItem.Type.CHESTPLATE,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_LEGGINGS_T5 = ITEMS.register("space_suit_leggings_t5",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T5, ArmorItem.Type.LEGGINGS,
                                        new Item.Properties()));

        public static final RegistryObject<Item> SPACE_SUIT_BOOTS_T5 = ITEMS.register("space_suit_boots_t5",
                        () -> new ArmorItem(CHEXArmorMaterials.SPACE_SUIT_T5, ArmorItem.Type.BOOTS,
                                        new Item.Properties()));
}
