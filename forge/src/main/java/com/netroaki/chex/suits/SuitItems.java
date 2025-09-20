package com.netroaki.chex.suits;

import com.netroaki.chex.CHEX;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Suit items for CHEX hazard protection system
 * Creates 5 suit sets (helmet, chestplate, leggings, boots) for each tier
 */
public class SuitItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CHEX.MOD_ID);

    // Suit I - Basic Space Suit (Vacuum protection)
    public static final RegistryObject<Item> SUIT_I_HELMET = ITEMS.register("suit_i_helmet",
            () -> new SuitItem(SuitTiers.SUIT_I, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_I_CHESTPLATE = ITEMS.register("suit_i_chestplate",
            () -> new SuitItem(SuitTiers.SUIT_I, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_I_LEGGINGS = ITEMS.register("suit_i_leggings",
            () -> new SuitItem(SuitTiers.SUIT_I, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_I_BOOTS = ITEMS.register("suit_i_boots",
            () -> new SuitItem(SuitTiers.SUIT_I, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // Suit II - Advanced Space Suit (Vacuum + Cryogenic)
    public static final RegistryObject<Item> SUIT_II_HELMET = ITEMS.register("suit_ii_helmet",
            () -> new SuitItem(SuitTiers.SUIT_II, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_II_CHESTPLATE = ITEMS.register("suit_ii_chestplate",
            () -> new SuitItem(SuitTiers.SUIT_II, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_II_LEGGINGS = ITEMS.register("suit_ii_leggings",
            () -> new SuitItem(SuitTiers.SUIT_II, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_II_BOOTS = ITEMS.register("suit_ii_boots",
            () -> new SuitItem(SuitTiers.SUIT_II, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // Suit III - Enhanced Space Suit (Vacuum + Cryogenic + Acid)
    public static final RegistryObject<Item> SUIT_III_HELMET = ITEMS.register("suit_iii_helmet",
            () -> new SuitItem(SuitTiers.SUIT_III, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_III_CHESTPLATE = ITEMS.register("suit_iii_chestplate",
            () -> new SuitItem(SuitTiers.SUIT_III, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_III_LEGGINGS = ITEMS.register("suit_iii_leggings",
            () -> new SuitItem(SuitTiers.SUIT_III, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_III_BOOTS = ITEMS.register("suit_iii_boots",
            () -> new SuitItem(SuitTiers.SUIT_III, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // Suit IV - Superior Space Suit (Vacuum + Cryogenic + Acid + Radiation)
    public static final RegistryObject<Item> SUIT_IV_HELMET = ITEMS.register("suit_iv_helmet",
            () -> new SuitItem(SuitTiers.SUIT_IV, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_IV_CHESTPLATE = ITEMS.register("suit_iv_chestplate",
            () -> new SuitItem(SuitTiers.SUIT_IV, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_IV_LEGGINGS = ITEMS.register("suit_iv_leggings",
            () -> new SuitItem(SuitTiers.SUIT_IV, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_IV_BOOTS = ITEMS.register("suit_iv_boots",
            () -> new SuitItem(SuitTiers.SUIT_IV, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // Suit V - Ultimate Space Suit (All hazards)
    public static final RegistryObject<Item> SUIT_V_HELMET = ITEMS.register("suit_v_helmet",
            () -> new SuitItem(SuitTiers.SUIT_V, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_V_CHESTPLATE = ITEMS.register("suit_v_chestplate",
            () -> new SuitItem(SuitTiers.SUIT_V, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_V_LEGGINGS = ITEMS.register("suit_v_leggings",
            () -> new SuitItem(SuitTiers.SUIT_V, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SUIT_V_BOOTS = ITEMS.register("suit_v_boots",
            () -> new SuitItem(SuitTiers.SUIT_V, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    /**
     * Custom suit item that extends ArmorItem with hazard protection info
     */
    public static class SuitItem extends ArmorItem {
        private final SuitTiers suitTier;

        public SuitItem(SuitTiers suitTier, Type type, Properties properties) {
            super(new SuitArmorMaterial(suitTier), type, properties);
            this.suitTier = suitTier;
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
            super.appendHoverText(stack, level, tooltip, flag);

            // Add suit tier info
            tooltip.add(Component.literal("Suit Tier: " + suitTier.getDisplayName())
                    .withStyle(ChatFormatting.GOLD));

            // Add hazard protection info
            tooltip.add(Component.literal("Protection:").withStyle(ChatFormatting.YELLOW));
            for (SuitTiers.HazardType hazard : suitTier.getProtectedHazards()) {
                tooltip.add(Component.literal("  • " + hazard.getDisplayName())
                        .withStyle(ChatFormatting.GREEN));
            }

            // Add usage info
            tooltip.add(Component.literal("Required for hazardous environments")
                    .withStyle(ChatFormatting.GRAY));
        }

        public SuitTiers getSuitTier() {
            return suitTier;
        }
    }

    /**
     * Armor material for suits
     */
    public static class SuitArmorMaterial implements ArmorMaterial {
        private final SuitTiers suitTier;

        public SuitArmorMaterial(SuitTiers suitTier) {
            this.suitTier = suitTier;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case BOOTS -> 195;
                case LEGGINGS -> 225;
                case CHESTPLATE -> 240;
                case HELMET -> 165;
            };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (suitTier) {
                case SUIT_I -> switch (type) {
                    case BOOTS -> 1;
                    case LEGGINGS -> 2;
                    case CHESTPLATE -> 3;
                    case HELMET -> 2;
                };
                case SUIT_II -> switch (type) {
                    case BOOTS -> 2;
                    case LEGGINGS -> 3;
                    case CHESTPLATE -> 4;
                    case HELMET -> 3;
                };
                case SUIT_III -> switch (type) {
                    case BOOTS -> 3;
                    case LEGGINGS -> 4;
                    case CHESTPLATE -> 5;
                    case HELMET -> 4;
                };
                case SUIT_IV -> switch (type) {
                    case BOOTS -> 4;
                    case LEGGINGS -> 5;
                    case CHESTPLATE -> 6;
                    case HELMET -> 5;
                };
                case SUIT_V -> switch (type) {
                    case BOOTS -> 5;
                    case LEGGINGS -> 6;
                    case CHESTPLATE -> 7;
                    case HELMET -> 6;
                };
            };
        }

        @Override
        public int getEnchantmentValue() {
            return suitTier.getTier() * 2;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.LEATHER); // Placeholder
        }

        @Override
        public String getName() {
            return "chex_suit_" + suitTier.getId();
        }

        @Override
        public float getToughness() {
            return suitTier.getTier() * 0.5f;
        }

        @Override
        public float getKnockbackResistance() {
            return suitTier.getTier() * 0.1f;
        }
    }
}
