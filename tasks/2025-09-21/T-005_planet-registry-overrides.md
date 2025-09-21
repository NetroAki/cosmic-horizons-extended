# T-005 Planet Registry Override Merge

**Goal**
- Extend `PlanetRegistry.registerDiscoveredPlanets` so entries from `config/chex/chex-planets.json5` override fuel, suits, hazards, descriptions, and add unit tests.

**Scope**
- `forge/src/main/java/com/netroaki/chex/registry/PlanetRegistry.java`
- `common/src/test/java/...` (add tests for merge logic)
- Config parsing helpers as needed.

**Acceptance**
- Overrides apply without losing base data; hazards and descriptions merge correctly.
- Unit tests cover at least happy-path + missing-fields cases.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement override merge + tests
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_registry_overrides.md` + add to `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
