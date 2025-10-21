# T-286 Exotica Fauna Implementation

**Goal**

- Implement diverse fauna for Exotica with surreal, quantum, and fractal-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Prism Colossus: massive creature with prismatic properties
- Dune Siren: creature that uses sound-based attacks
- Quantum Beast: creature with quantum properties
- Fractal Horror: creature with fractal-based appearance
- Prism Seraph: angelic creature with prismatic wings
- Chroma Butterflies: small creatures that change colors
- Resonance Birds: birds that create harmonic sounds
- Fractal Spiders: creatures that spin fractal webs
- Quantum Fish: aquatic creatures with quantum properties
- Prism Wolves: pack hunters with prismatic fur
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Prism Colossus with prismatic properties
- [ ] Create Dune Siren with sound-based attacks
- [ ] Add Quantum Beast with quantum properties
- [ ] Implement Fractal Horror with fractal appearance
- [ ] Create Prism Seraph with prismatic wings
- [ ] Add Chroma Butterflies and Resonance Birds
- [ ] Implement Fractal Spiders and Quantum Fish
- [ ] Create Prism Wolves with pack behavior
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
