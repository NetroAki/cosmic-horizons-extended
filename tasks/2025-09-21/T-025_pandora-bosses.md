# T-025 Pandora Boss Encounters

**Goal**
- Design and implement Pandora boss encounters (Spore Tyrant, Cliff Hunter Alpha, Deep-Sea Siren, Molten Behemoth, Storm Roc) culminating in the **Worldheart Avatar** main fight with bespoke arenas, abilities, and loot cores.

**Scope**
- Entity/boss classes for all mini-bosses plus the Worldheart Avatar.
- Arena structures/datapack layouts per biome encounter and the central floating-island finale.
- Loot tables and GT progression hooks (Biolume Core, Pandoran Heartseed, weapon drops).

**Acceptance**
- Each boss fight plays end-to-end with tuned mechanics and reward drops (spore, cliff, ocean, volcanic cores, Heartseed weapon set).
- Pandoran Heartseed unlock + Biolume Core wiring into GT tier gating documented.
- `./gradlew check` passes; document balance/open issues in notes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics, structures, loot
- [ ] `./gradlew :forge:runData` if structures/datapack changed
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_bosses.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
