# T-339 The Infinite Library The Librarian Boss

**Goal**

- Implement the main boss encounter for The Infinite Library with multi-phase knowledge, wisdom, and cosmic mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Center of Infinite Library arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Center of Infinite Library with massive desk and floating books
- Appearance: Wise entity of pure knowledge surrounded by floating books
- Phase 1 - Knowledge Phase: Shares all GT knowledge and unlocks technologies
- Phase 2 - Wisdom Phase: Provides optimization advice and perfect processes
- Phase 3 - Cosmic Phase: Becomes one with universe knowledge
- Drops: Librarian's Blessing (Tier 18 GT Knowledge Mastery), Book of Everything, Wisdom Crown
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design The Librarian boss entity
- [ ] Create center of Infinite Library arena
- [ ] Implement Knowledge Phase mechanics
- [ ] Add Wisdom Phase with optimization advice
- [ ] Create Cosmic Phase with universe knowledge
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_infinite_library_librarian_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
