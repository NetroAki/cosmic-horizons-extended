# T-257 Inferno Prime Infernal Sovereign Boss

**Goal**

- Implement the main boss encounter for Inferno Prime with multi-phase volcanic and fire mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Massive lava chamber arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Massive lava chamber with floating platforms
- Appearance: Colossal entity with magma armor and fire-based attacks
- Phase 1 - Magma Armor Phase: Heavily armored, uses ground-based attacks
- Phase 2 - Fire Rain Phase: Rains fire from above, creates lava pools
- Phase 3 - Lava Wave Phase: Creates massive lava waves, uses area attacks
- Drops: Inferno Core (niobium/tantalum/uranium unlock), Molten Blade weapon, Volcanic Essence (heat tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Infernal Sovereign boss entity
- [ ] Create massive lava chamber arena
- [ ] Implement Magma Armor Phase mechanics
- [ ] Add Fire Rain Phase with fire attacks
- [ ] Create Lava Wave Phase with area attacks
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_infernal_sovereign_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
