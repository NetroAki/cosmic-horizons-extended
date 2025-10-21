# T-230 Alpha Centauri A Flora Generation

**Goal**

- Implement unique flora generation for Alpha Centauri A with stellar, plasma, and magnetic-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and stellar effects

**Acceptance**

- Sunshards: crystalline growths sprouting from rock, refracting light beams
- Plasma Vines: energy-based plant life that grows along plasma streams
- Magnetic Moss: grows on magnetite surfaces, glows with magnetic energy
- Solar Blooms: flower-like energy formations that pulse with solar radiation
- Corona Ferns: delicate energy structures that sway in solar winds
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Sunshards with light refraction
- [ ] Create Plasma Vines with energy growth
- [ ] Add Magnetic Moss with magnetic glow
- [ ] Implement Solar Blooms with radiation pulse
- [ ] Create Corona Ferns with wind animation
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
