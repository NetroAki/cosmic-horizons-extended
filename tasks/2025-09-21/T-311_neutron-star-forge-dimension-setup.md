# T-311 Neutron Star Forge Dimension Setup

**Goal**

- Create neutron star dimension with extreme gravity, crushing gravity pulses, and magnetic storm mechanics for Neutron Star Forge (Tier 16-17 endgame).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/neutron_star_forge.json`
- Neutron star environment and extreme gravity systems
- Crushing gravity and magnetic storm mechanics

**Acceptance**

- Neutron star dimension with extreme gravity
- Crushing gravity pulses
- Magnetic storm mechanics
- Radiation tick systems
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create neutron star dimension JSON
- [ ] Implement extreme gravity systems
- [ ] Add crushing gravity pulses
- [ ] Configure magnetic storm mechanics
- [ ] Set radiation tick systems
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
