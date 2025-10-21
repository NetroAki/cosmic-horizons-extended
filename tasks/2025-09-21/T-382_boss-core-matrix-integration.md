# T-382 Boss Core Matrix Integration

**Goal**

- Integrate boss core matrix (mini-boss cores → material unlocks, main hearts → tier unlocks) with GTCEu progression system.

**Scope**

- `forge/src/main/java/com/netroaki/chex/progression/`
- Boss core matrix integration
- GTCEu progression system integration

**Acceptance**

- Boss core matrix integration implemented
- Mini-boss cores unlock materials
- Main hearts unlock tiers
- GTCEu progression system integration
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create boss core matrix integration
- [ ] Implement mini-boss core unlocks
- [ ] Add main heart tier unlocks
- [ ] Integrate with GTCEu progression
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_boss_core_matrix_integration.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
