# T-317 Neutron Star Forge Forge Star Sovereign Boss

**Goal**

- Implement the main boss encounter for Neutron Star Forge with multi-phase gravity, magnetic, and forge mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Central forge arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Central forge with extreme gravity
- Appearance: Colossal entity of neutron star material
- Phase 1 - Gravity Manipulation Phase: Controls gravity fields, uses gravity-based attacks
- Phase 2 - Magnetic Phase: Uses magnetic fields, creates magnetic storms
- Phase 3 - Forge Phase: Uses forge systems, creates matter-forging effects
- Drops: Sovereign Heart (neutronium/exotic reactor tech unlock), Sovereign Blade weapon, Neutron Essence (neutron star tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Forge Star Sovereign boss entity
- [ ] Create central forge arena
- [ ] Implement Gravity Manipulation Phase mechanics
- [ ] Add Magnetic Phase with magnetic storms
- [ ] Create Forge Phase with matter-forging effects
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_sovereign_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
