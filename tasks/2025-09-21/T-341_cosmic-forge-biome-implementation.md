# T-341 The Cosmic Forge Biome Implementation

**Goal**

- Implement 5 unique biomes for The Cosmic Forge with creation, crafting, and mastery environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/cosmic_forge_*.json`
- Biome-specific terrain generation and creation effects
- Crafting and mastery exploration systems

**Acceptance**

- Forge Chambers: Massive chambers with ultimate crafting stations
- Material Vaults: Vaults containing infinite materials of all types
- Creation Altars: Altars where anything can be created
- Essence Wells: Wells containing pure essence of all materials
- Master Workshops: Workshops with perfect crafting conditions
- Each biome has distinct visual characteristics and creation effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Forge Chambers biome
- [ ] Create Material Vaults biome
- [ ] Implement Creation Altars biome
- [ ] Add Essence Wells biome
- [ ] Create Master Workshops biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cosmic_forge_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
