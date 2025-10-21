# T-227 Alpha Centauri A Dimension Setup

**Goal**

- Create star-surface dimension with space environment, radiation systems, and low gravity for Alpha Centauri A (stellar megastructure).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/alpha_centauri_a.json`
- Space environment and life support systems
- Radiation and gravity mechanics

**Acceptance**

- Star-surface dimension with space environment (no atmosphere)
- Full space suit + oxygen tank requirement for survival
- Radiation exposure damage for unshielded players
- Low gravity (0.4g) - higher jumps, reduced fall damage, dangerous knockbacks
- Radiation fields in magnetosphere and corona (constant damage without shielding)
- Solar flare events (random pulses of light and heat waves with AoE damage)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create star-surface dimension JSON
- [ ] Implement space environment systems
- [ ] Add life support requirements
- [ ] Configure radiation exposure damage
- [ ] Set low gravity mechanics
- [ ] Add solar flare events
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
