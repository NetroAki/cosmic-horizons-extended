# T-244 Aqua Mundus Biome Implementation

**Goal**

- Implement 5 unique biomes for Aqua Mundus with ocean, deep sea, and hydrothermal environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/aqua_mundus_*.json`
- Biome-specific terrain generation and underwater effects
- Ocean and deep sea exploration systems

**Acceptance**

- Shallow Seas: Light-filled waters with abundant life
- Kelp Forests: Dense underwater forests with towering kelp
- Abyssal Trenches: Deep, dark trenches with unique life forms
- Hydrothermal Vents: Hot, mineral-rich vent systems
- Ice Shelves: Frozen underwater ice formations
- Each biome has distinct visual characteristics and underwater effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Shallow Seas biome
- [ ] Create Kelp Forests biome
- [ ] Implement Abyssal Trenches biome
- [ ] Add Hydrothermal Vents biome
- [ ] Create Ice Shelves biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
