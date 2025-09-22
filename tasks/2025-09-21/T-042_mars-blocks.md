# T-042 Mars Blocks

**Goal**
- Implement Martian blocks including red sand, iron oxide, and Mars-specific construction materials.

**Scope**
- `common/src/main/java/com/netroaki/chex/blocks/`
- Block registrations, models, and textures for Mars-specific blocks.

**Acceptance**
- All blocks render correctly with proper models and textures.
- Document block properties in `notes/planet_summary/mars.md`.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step42_mars_blocks.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
