# T-242 Release Packaging

**Goal**

- Prepare release packaging workflow bundling configs/docs, mod metadata, screenshots, changelog, license for Modrinth/CurseForge.

**Scope**

- Build scripts, release docs, asset collection.

**Acceptance**

- Packaging process documented/automated; artifacts ready for upload; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement packaging script/docs
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_release_packaging.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
