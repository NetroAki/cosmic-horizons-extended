# T-249 Aqua Mundus Ocean Sovereign Boss

**Goal**

- Implement the main boss encounter for Aqua Mundus with multi-phase underwater and pressure mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Deep ocean trench arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Deep ocean trench with hydrothermal vents
- Appearance: Multi-headed eel with bioluminescent features
- Phase 1 - Sonic Phase: Uses sonic attacks to disorient players, creates pressure waves
- Phase 2 - Whirlpool Phase: Creates massive whirlpools, uses water-based attacks
- Phase 3 - Pressure Phase: Increases pressure, uses crushing attacks and depth-based mechanics
- Drops: Ocean Core (platinum/iridium/palladium chain), Tidal Trident weapon, Deep Sea Essence (pressure tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Ocean Sovereign boss entity
- [ ] Create deep ocean trench arena
- [ ] Implement Sonic Phase mechanics
- [ ] Add Whirlpool Phase with water attacks
- [ ] Create Pressure Phase with crushing mechanics
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_ocean_sovereign_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
