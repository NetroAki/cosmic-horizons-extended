# T-217 Pandora Worldheart Avatar Boss

**Goal**

- Implement the main boss encounter for Pandora with multi-phase mechanics and unique arena design.

**Scope**

- Boss entity class with multi-phase abilities
- Central floating island arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Central floating island fused from Bioluminescent Forest and Floating Mountain biomes
- Appearance: ~12-block tall entity, lower body = massive root system, upper body = humanoid torso of bark fused with crystal
- Phase 1 - Root Phase: slams ground with roots, shockwaves + sporeling summons
- Phase 2 - Flight Phase: levitates tree-body, floating crystal islands orbit and hurl as projectiles
- Phase 3 - Corruption Phase: arena fills with dense spores, vision limited, summons elite Spore Tyrants
- Drops: Pandoran Heartseed (T3 Rocket Fuel Plant recipes), Sporeblade weapon, Biolume Core (GT electronics)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Worldheart Avatar boss entity
- [ ] Create central floating island arena
- [ ] Implement Root Phase mechanics
- [ ] Add Flight Phase with crystal projectiles
- [ ] Create Corruption Phase with spore effects
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_worldheart_avatar_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
