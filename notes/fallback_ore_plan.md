# Fallback Ore Block Plan

Goal: Provide actual block/item assets for each fallback ore ID referenced in chex-minerals.json5 so CHEX can run without GTCEu installed.

Scope

- Blocks for 23 fallback ores: bauxite, ilmenite, lead, silver, lithium, titanium, vanadium, tungsten, molybdenum, chromium, magnesium, platinum, iridium, palladium, niobium, tantalum, uranium, osmium, beryllium, fluorite, ruby, sapphire, draconium, awakened_draconium.
- Each block should:
  - Use stone-like hardness (3.0f) and require a pickaxe.
  - Drop itself (ore block) when mined with silk touch or correct tool; optionally drop nuggets/dust? For MVP drop block for now and rely on processing chain.
  - Register block item for placement in world.
  - Provide blockstate/model (simple cube_all) and loot table.
  - Tag with minecraft:mineable/pickaxe, orge:ores, and a specific orge:ores/<material> (if tag exists) for compatibility.
- Optional: simple experience drop via DropExperienceBlock to mirror vanilla ores (pending decision during implementation).

Implementation Outline

1. Add helper registration in CHEXBlocks to avoid repetitive code (e.g., a map of fallback ore names ? registry objects). Possibly a method egisterFallbackOre(String name) returning RegistryObject<Block>.
2. Update block item registration loop to include new fallback ore registry objects.
3. Create blockstate/model JSONs under orge/src/main/resources/assets/cosmic_horizons_extended/blockstates/.
4. Create block model JSONs under ssets/.../models/block/ using existing ore texture placeholders (create placeholder textures later if missing).
5. Ensure textures exist or create placeholder textures. (Need to verify current textures directory.)
6. Add item models referencing block models (models/item/).
7. Add loot tables under data/cosmic_horizons_extended/loot_tables/blocks/.
8. Add translations to lang/en_us.json.
9. Add block tags in data/cosmic_horizons_extended/tags/blocks/ and items/ as needed.

Implementation Notes (2025-09-21)

- Registered 24 fallback ores via egisterFallbackOre helper (see CHEXBlocks). Each ore drops 1?3 XP and copies iron ore mining traits.
- Generated blockstates, block and item models, loot tables, and translations. Vanilla textures referenced as placeholders (see
  otes/fallback_ore_textures.md).
- Added pickaxe + forge ore tags so other mods recognize the fallback blocks/items.
- Next polish step: provide bespoke textures and consider forge tag granularity per material if downstream mods expect orge:ores/<name>.
