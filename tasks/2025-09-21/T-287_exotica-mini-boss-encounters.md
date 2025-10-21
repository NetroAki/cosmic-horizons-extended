# T-287 Exotica Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Exotica with unique surreal and reality-bending mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Prism Colossus (Chroma Steppes): Giant crystalline humanoid reflecting beams of rainbow light
- Dune Siren (Resonant Dunes): Humanoid made of resonating sand and stone with voice shockwave attacks
- Quantum Beast (Quantum Glades): Wolf-like predator constantly phasing and splitting into copies
- Fractal Horror (Fractal Forest): Eldritch entity resembling a mirrored predator with recursive limbs
- Prism Seraph (Prism Canyons): Angelic crystalline entity with rainbow wings
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Prism Colossus boss entity
- [ ] Create Dune Siren with voice shockwave attacks
- [ ] Implement Quantum Beast with phasing mechanics
- [ ] Add Fractal Horror with recursive limbs
- [ ] Create Prism Seraph with rainbow wings
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
