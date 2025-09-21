# ðŸ”‘ Implementation Notes

- **Terrablender** is used for biome layering â†’ each planet needs its own dimension with a custom noise generator (terrain control).
- **Oxygen Mod Hook:** Planets are flagged as breathable or not. Players must manage **suit upgrades** (crafted from boss cores + GT parts).
- **Hazard System:** Tie status effects (radiation, frostbite, plasma burn, void collapse) to **dimension effect handlers**, not mobs. This way, survival depends on gear, not just mob killing.
- **Drops â†’ GT Recipes:** Every mob/boss drop should already map into a recipe slot. Example:

  - _Prism Hide_ â†’ used in GT optical fiber recipe.
  - _Cryo Core_ â†’ used to unlock GT liquid helium chain.
  - _Void Essence_ â†’ used as void catalyst in late-tier reactors.

---

#
