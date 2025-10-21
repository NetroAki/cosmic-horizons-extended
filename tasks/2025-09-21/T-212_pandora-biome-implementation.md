# T-212 Pandora Biome Implementation

**Goal**

- Implement 5 unique biomes for Pandora with distinct visual characteristics and environmental effects.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pandora_*.json`
- Biome-specific terrain generation and block palettes
- Environmental effects and atmospheric systems

**Acceptance**

- Bioluminescent Forest: Purple grass with cyan glow, spore soil with glowing speckles
- Floating Mountains: Crystal-clad pandorite stone with embedded cyan/emerald crystals
- Ocean Depths: Lumicoral blocks with pulsing cyan nodes, glowing kelp (6-8 blocks tall)
- Volcanic Wasteland: Volcanic pandorite with orange lava veins, ash sand (damages bare feet)
- Sky Islands: Cloudstone with light scattering, skygrass with glowing white flowers
- Each biome has distinct visual characteristics and environmental effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Bioluminescent Forest biome
- [ ] Create Floating Mountains biome
- [ ] Implement Ocean Depths biome
- [ ] Add Volcanic Wasteland biome
- [ ] Create Sky Islands biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
