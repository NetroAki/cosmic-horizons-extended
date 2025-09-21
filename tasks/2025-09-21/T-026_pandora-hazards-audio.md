# T-026 Pandora Hazards & Audio

**Goal**
- Implement Pandora hazard systems (levitation updrafts, heat aura, spore blindness) plus ambient particles/audio (biolume hum, wind SFX, twilight skybox).

**Scope**
- Hazard manager, particle systems, sound events, skybox assets.
- Config toggles if required.

**Acceptance**
- Hazards trigger in appropriate biomes and respect suit tiers.
- Audio/visual effects enabled via configuration; no client crashes.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add hazard logic + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
