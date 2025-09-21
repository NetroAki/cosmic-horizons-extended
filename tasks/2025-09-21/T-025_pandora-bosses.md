# T-025 Pandora Boss Encounters

**Goal**
- Design and implement Pandora boss encounters (Spore Tyrant, Cliff Hunter Alpha, Deep-Sea Siren, Molten Behemoth, Sky Sovereign) including arenas, abilities, loot cores.

**Scope**
- Entity/boss classes, arenas (structures/datapack), loot tables.
- Progression unlock hooks for GT tiers.

**Acceptance**
- Each boss fight playable end-to-end with reward item(s).
- Loot cores integrate with progression gating.
- `./gradlew check` passes; document balance/open issues in notes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics, structures, loot
- [ ] `./gradlew :forge:runData` if structures/datapack changed
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_bosses.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
