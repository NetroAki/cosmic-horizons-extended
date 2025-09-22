# T-027 Moon Blocks

**Goal**
- Implement lunar blocks including regolith, moonstone, and space construction materials.

**Scope**
- `common/src/main/java/com/netroaki/chex/blocks/`
- Block registrations, models, and textures for Moon-specific blocks.

**Acceptance**
- All blocks render correctly with proper models and textures.
- Document block properties in `notes/planet_summary/moon.md`.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step27_moon_blocks.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
