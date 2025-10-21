# T-294 Torus World Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Torus World with unique toroidal and gravity-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Forest Guardian (Inner Rim Forest): Golem-like construct with wooden outer shell hiding alloy frame
- Desert Colossus (Outer Rim Desert): Gigantic alloy-armored insect construct
- Spine Overseer (Structural Spine): Massive quadrupedal machine with glowing red optics
- Luminal Titan (Radiant Fields): Towering construct of light and alloy
- Exotic Horror (Null-G Hubs): Shifting creature of fractal alloys ~10 blocks long
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Forest Guardian boss entity
- [ ] Create Desert Colossus with alloy armor
- [ ] Implement Spine Overseer with glowing optics
- [ ] Add Luminal Titan with light and alloy
- [ ] Create Exotic Horror with fractal alloys
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torus_world_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
