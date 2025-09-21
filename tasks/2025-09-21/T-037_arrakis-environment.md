# T-037 Arrakis Environmental Systems

**Goal**
- Implement Arrakis environmental effects (heat exhaustion, dust visibility, ambient wind audio, red sky palette).

**Scope**
- Hazard manager, particle systems, skybox.

**Acceptance**
- Effects trigger appropriately and respect suit tiers.
- Audio/visual ambience matches design.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement environmental systems + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_environment.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
