# T-152 Neutron Star Forge Entities — Completed

Summary

- Registered Neutron fauna and mini-boss placeholders in `ModEntities.java`:
  - Fauna: `plasma_eel`, `accretion_beast`, `magnetar_serpent`, `storm_construct`, `forge_drone`, `smelter_horror`, `gravity_horror`, `void_leech`, `shelter_guardian`, `radiant_wraith`
  - Mini-bosses: `accretion_leviathan`, `magnetar_colossus`, `forge_overseer`, `graviton_horror`, `shelter_sentinel`
- Added drop items in `ModItems.java`: `plasma_organ`, `accreted_core`, `magnetar_scales`, `storm_fragment`, `alloy_chips`, `molten_organ`, `gravity_organ`, `leech_extract`, `shield_circuit`, `radiant_essence`.
- Created loot tables for all entities in `data/cosmic_horizons_extended/loot_tables/entities/`.
- Wired spawns into Neutron biomes via Forge biome modifiers under `data/cosmic_horizons_extended/forge/biome_modifier/`.

Verification

- All entities registered, loot items present, loot tables reference the correct item IDs, and biome modifiers target the intended Neutron biomes.
- Acceptance satisfied for roster, drops, and spawns; FX/AI can be enhanced in later tasks.
