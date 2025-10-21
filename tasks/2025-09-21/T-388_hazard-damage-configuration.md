# T-388 Hazard Damage Configuration

**Goal**

- Add configurable damage/mitigation per planet with customizable hazard settings and player protection options.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/config/`
- Hazard damage configuration system
- Planet-specific hazard settings

**Acceptance**

- Configurable damage/mitigation per planet
- Customizable hazard settings
- Player protection options
- Configuration system working
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create hazard damage configuration
- [ ] Add planet-specific settings
- [ ] Implement customizable options
- [ ] Add player protection options
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hazard_damage_configuration.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
