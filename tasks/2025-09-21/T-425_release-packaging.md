# T-425 Release Packaging

**Goal**

- Prepare release packaging task bundling configs/docs, create mod metadata for Modrinth/CurseForge, add screenshots and promotional materials, create changelog and release notes, set up automated release pipeline.

**Scope**

- `build/release/`
- Release packaging system
- Mod distribution preparation

**Acceptance**

- Release packaging task created
- Configs/docs bundled
- Mod metadata for Modrinth/CurseForge
- Screenshots and promotional materials
- Changelog and release notes
- Automated release pipeline
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create release packaging task
- [ ] Bundle configs/docs
- [ ] Create mod metadata
- [ ] Add screenshots and materials
- [ ] Create changelog and release notes
- [ ] Set up automated pipeline
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_release_packaging.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
