# T-211 Pandora Dimension Setup

**Goal**

- Create dimension JSON with twilight sky gradient, spore-filled atmosphere, and environmental hazard zones for Pandora (Tier 3 transition planet).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/pandora.json`
- Atmospheric and environmental systems
- Gravity and hazard zone configuration

**Acceptance**

- Dimension JSON with twilight sky gradient (violet → indigo → teal aurora bands)
- Permanent twilight atmosphere with spore-filled air
- Levitation updraft zones in floating areas (fall damage risk)
- Heat damage aura zones in volcanic areas
- Gravity set to 1.0 (Earth-like)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create dimension JSON with twilight sky gradient
- [ ] Implement spore-filled atmosphere system
- [ ] Add levitation updraft zones
- [ ] Configure heat damage aura zones
- [ ] Set gravity to 1.0
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
