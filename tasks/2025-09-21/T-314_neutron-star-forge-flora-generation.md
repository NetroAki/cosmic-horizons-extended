# T-314 Neutron Star Forge Flora Generation

**Goal**

- Implement unique flora generation for Neutron Star Forge with neutron star, magnetic, and forge-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and neutron star effects

**Acceptance**

- Neutronium Crystals: crystals that grow in extreme gravity
- Magnetic Moss: moss that grows in magnetic fields
- Forge Vines: plants that grow in forge environments
- Graviton Flowers: flowers that respond to gravity
- Radiation Algae: algae that grows in radiation
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Neutronium Crystals with extreme gravity growth
- [ ] Create Magnetic Moss with magnetic field growth
- [ ] Add Forge Vines with forge environment growth
- [ ] Implement Graviton Flowers with gravity response
- [ ] Create Radiation Algae with radiation growth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
