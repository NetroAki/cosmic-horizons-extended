# T-211 Boss Core Matrix Integration

**Goal**
- Integrate boss core matrix (mini cores unlock materials, main hearts unlock tiers) into progression configs and JEI documentation.

**Scope**
- Progression configs, loot tables, JEI data, documentation notes.

**Acceptance**
- Core matrix fully described and wired to progression gates.
- JEI pages reflect unlocks; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Update configs/JEI docs for core matrix
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_boss_core_matrix.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
