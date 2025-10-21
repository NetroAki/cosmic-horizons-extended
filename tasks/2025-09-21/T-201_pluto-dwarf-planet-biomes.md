# T-201 Pluto Dwarf Planet Biomes

**Goal**

- Add 5+ new biomes (Ice Plains, Cryovolcanoes, Nitrogen Lakes, Methane Snow, Frozen Craters) to enhance Pluto [EXISTING] with diverse dwarf planet environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pluto_*.json`
- Biome-specific terrain generation and cryogenic effects
- Dwarf planet-specific environmental systems

**Acceptance**

- Ice Plains: Vast frozen areas with unique ice formations
- Cryovolcanoes: Volcanic features that erupt ice and frozen materials
- Nitrogen Lakes: Liquid nitrogen bodies with unique properties
- Methane Snow: Snow-like precipitation of frozen methane
- Frozen Craters: Impact craters filled with frozen materials
- Each biome provides unique resources and challenges
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Ice Plains biome
- [ ] Create Cryovolcanoes with ice eruptions
- [ ] Implement Nitrogen Lakes with unique properties
- [ ] Add Methane Snow precipitation
- [ ] Create Frozen Craters with impact features
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pluto_dwarf_planet_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
