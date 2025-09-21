# T-041 Alpha Centauri Hazards

**Goal**
- Implement extreme light/heat hazards for Alpha Centauri A requiring Suit IV and flare burst events.

**Scope**
- Hazard systems, particle/audio/visual cues.

**Acceptance**
- Hazards activate in star biomes, respond to suit tier.
- Flare events trigger with telegraphed warnings.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazard/flare logic
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
