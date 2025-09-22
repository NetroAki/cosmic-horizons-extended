package com.netroaki.chex.item;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.fluid.ModFluids;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AquaItems {
    public static final DeferredRegister<Item> AQUA_ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, CHEX.MOD_ID);
    
    // Bucket for Luminous Kelp fluid
    public static final RegistryObject<Item> LUMINOUS_KELP_BUCKET = AQUA_ITEMS.register("luminous_kelp_bucket",
        () -> new BucketItem(ModFluids.SOURCE_LUMINOUS_KELP,
            new Item.Properties()
                .craftRemainder(Items.BUCKET)
                .stacksTo(1)
                .tab(CHEX.TAB)
        )
    );
    
    // Diving Helmet
    public static final RegistryObject<ArmorItem> DIVING_HELMET = AQUA_ITEMS.register("diving_helmet",
        () -> new DivingArmorItem(ArmorMaterials.TURTLE, EquipmentSlot.HEAD,
            new Item.Properties().tab(CHEX.TAB).durability(250)
        )
    );
    
    // Diving Suit
    public static final RegistryObject<ArmorItem> DIVING_CHESTPLATE = AQUA_ITEMS.register("diving_chestplate",
        () -> new DivingArmorItem(ArmorMaterials.TURTLE, EquipmentSlot.CHEST,
            new Item.Properties().tab(CHEX.TAB).durability(300)
        )
    );
    
    // Diving Leggings
    public static final RegistryObject<ArmorItem> DIVING_LEGGINGS = AQUA_ITEMS.register("diving_leggings",
        () -> new DivingArmorItem(ArmorMaterials.TURTLE, EquipmentSlot.LEGS,
            new Item.Properties().tab(CHEX.TAB).durability(300)
        )
    );
    
    // Diving Boots
    public static final RegistryObject<ArmorItem> DIVING_BOOTS = AQUA_ITEMS.register("diving_boots",
        () -> new DivingArmorItem(ArmorMaterials.TURTLE, EquipmentSlot.FEET,
            new Item.Properties().tab(CHEX.TAB).durability(275)
        )
    );
    
    // Oxygen Tank
    public static final RegistryObject<Item> OXYGEN_TANK = AQUA_ITEMS.register("oxygen_tank",
        () -> new Item(new Item.Properties()
            .tab(CHEX.TAB)
            .stacksTo(1)
            .durability(1000)
        )
    );

    // Luminfish Spawn Egg
    public static final RegistryObject<ForgeSpawnEggItem> LUMINFISH_SPAWN_EGG = AQUA_ITEMS.register("luminfish_spawn_egg",
        () -> new ForgeSpawnEggItem(
            () -> ModEntities.LUMINFISH.get(),
            0x00AA88,  // Primary color (teal)
            0x88FFEE,  // Secondary color (light cyan)
            new Item.Properties().tab(CHEX.TAB)
        )
    );
    
    // Manganese Nodule
    public static final RegistryObject<Item> MANGANESE_NODULE = AQUA_ITEMS.register("manganese_nodule",
        () -> new Item(new Item.Properties().tab(CHEX.TAB))
    );
    
    // Luminous Kelp Item
    public static final RegistryObject<Item> LUMINOUS_KELP = AQUA_ITEMS.register("luminous_kelp",
        () -> new ItemNameBlockItem(com.netroaki.chex.block.AquaBlocks.LUMINOUS_KELP.get(),
            new Item.Properties().tab(CHEX.TAB)
        )
    );
    
    public static void register(IEventBus eventBus) {
        AQUA_ITEMS.register(eventBus);
    }
    
    // Custom Diving Armor class for special effects
    public static class DivingArmorItem extends ArmorItem {
        public DivingArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
            super(material, slot, properties);
        }
    }
}
