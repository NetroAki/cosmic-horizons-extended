# T-422 Change Management

**Goal**

- Maintain `CHANGELOG.md` capturing discovery automation and fallback ores, document upcoming planet releases and features, add migration guides for configuration changes, create version compatibility matrix, document breaking changes and deprecations.

**Scope**

- `CHANGELOG.md`, `docs/changes/`
- Change management documentation
- Version compatibility and migration guides

**Acceptance**

- CHANGELOG.md maintained
- Discovery automation and fallback ores documented
- Upcoming planet releases documented
- Migration guides for configuration changes
- Version compatibility matrix
- Breaking changes and deprecations documented
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Maintain CHANGELOG.md
- [ ] Document discovery automation
- [ ] Document fallback ores
- [ ] Document upcoming releases
- [ ] Add migration guides
- [ ] Create compatibility matrix
- [ ] Document breaking changes
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_change_management.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
