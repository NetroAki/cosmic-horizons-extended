# T-006 /chex dumpPlanets Command

**Goal**

- Implement `/chex dumpPlanets --reload` to run discovery + registry reload and emit a formatted table plus JSON snapshot.

**Scope**

- `forge/src/main/java/com/netroaki/chex/commands/ChexCommands.java`
- Supporting command/registry utilities.
- Logging + output formatting.

**Acceptance**

- Command reloads discovery and writes snapshot to configurable location.
- Console output includes table with planet id, name, tier, suit tag, source.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement command handler + wiring
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dump_planets.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
