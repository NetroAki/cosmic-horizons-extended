# T-007 Travel Graph Validation Suite

**Goal**
- Finalize `TravelGraph` loader validation and add `/chex travelGraph validate` command to list unknown IDs or tier issues.

**Scope**
- `common/src/main/java/com/netroaki/chex/core/TravelGraphCore.java`
- `forge/src/main/java/com/netroaki/chex/travel/TravelGraph.java`
- Command integration in `ChexCommands`.

**Acceptance**
- Loader validates tier?planet mappings with clear error output.
- Command prints validation summary and warning list.
- Unit/integration tests added where sensible.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement validation + command
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_travel_graph.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
