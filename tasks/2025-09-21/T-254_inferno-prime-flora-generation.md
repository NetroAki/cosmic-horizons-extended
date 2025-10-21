# T-254 Inferno Prime Flora Generation

**Goal**

- Implement unique flora generation for Inferno Prime with volcanic, lava, and heat-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and volcanic effects

**Acceptance**

- Magma Spires: tall crystal formations that grow from lava
- Ash Shrubs: hardy plants that grow in volcanic ash
- Lava Vines: plants that grow along lava flows
- Obsidian Flowers: crystalline flowers that bloom in extreme heat
- Fire Moss: moss that glows with internal heat
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Magma Spires with crystal growth
- [ ] Create Ash Shrubs with ash adaptation
- [ ] Add Lava Vines with lava growth
- [ ] Implement Obsidian Flowers with heat bloom
- [ ] Create Fire Moss with internal glow
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
