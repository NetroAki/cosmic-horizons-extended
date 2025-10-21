# T-420 Configuration Examples

**Goal**

- Author sample configs: `docs/planets_example.json5`, `docs/minerals_example.json5`, `docs/travel_graph_example.json5`, `docs/boss_encounters.json5`, `docs/suit_hazards.json5` with comprehensive examples.

**Scope**

- `docs/` directory
- Configuration example files
- Comprehensive configuration examples

**Acceptance**

- Sample configs created
- planets_example.json5 with planet examples
- minerals_example.json5 with ore distribution examples
- travel_graph_example.json5 with travel progression
- boss_encounters.json5 with boss configuration
- suit_hazards.json5 with hazard configuration
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create planets_example.json5
- [ ] Create minerals_example.json5
- [ ] Create travel_graph_example.json5
- [ ] Create boss_encounters.json5
- [ ] Create suit_hazards.json5
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_configuration_examples.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
