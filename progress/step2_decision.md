# Step 2 - Task Selection (2025-09-21)

Decision: Implement the fallback ore block suite referenced by chex-minerals.json5 so CHEX runs without GTCEu. This covers 23 ore blocks + items with assets, loot, and tagging.

Rationale:

- Config + worldgen already reference fallback IDs; missing blocks will break worlds when GTCEu absent.
- Enables immediate gameplay value for Forge-only deployments while larger systems (density functions, endgame planets) remain in progress.

Planned actions (see notes/fallback_ore_plan.md for detail):

1. Register fallback ore blocks/items via helper in CHEXBlocks.
2. Supply blockstates/models/item models + loot tables + lang entries.
3. Add required block/item tags to integrate with mining rules and worldgen.
4. Verify resource loading and ensure worldgen registry resolves fallback states without GTCEu.
