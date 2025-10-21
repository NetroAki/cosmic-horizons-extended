# T-233 Alpha Centauri A Stellar Avatar Boss

**Goal**

- Implement the main boss encounter for Alpha Centauri A with multi-phase stellar mechanics and unique arena design.

**Scope**

- Boss entity class with multi-phase abilities
- Central solar platform arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Central solar platform surrounded by plasma streams and magnetic fields
- Appearance: Massive entity of pure stellar energy, constantly shifting between plasma and solid forms
- Phase 1 - Solar Phase: Intense heat and light attacks, solar beam projectiles
- Phase 2 - Corona Phase: Plasma stream attacks, magnetic field manipulation
- Phase 3 - Flare Phase: Massive solar flare events, energy storm attacks
- Drops: Stellar Core (photonic GT chain unlock), Solar Blade weapon, Fusion Catalyst (advanced fusion tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Stellar Avatar boss entity
- [ ] Create central solar platform arena
- [ ] Implement Solar Phase mechanics
- [ ] Add Corona Phase with plasma streams
- [ ] Create Flare Phase with energy storms
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_stellar_avatar_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
