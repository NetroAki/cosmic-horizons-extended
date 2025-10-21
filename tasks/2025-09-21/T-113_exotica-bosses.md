# T-113 Exotica Boss Encounters

**Goal**

- Implement Exotica boss suite: biome mini-bosses (Prism Colossus, Dune Siren, Quantum Beast, Fractal Horror, Prism Seraph) and the multi-phase Reality Breaker fight (Fractal Division → Quantum Distortion → Prismatic Collapse) with loot cores/hearts per design doc.

**Scope**

- Entity/boss classes, arena structures (resonant dunes amphitheatre, quantum glade arenas, fractal forest dome, prism canyon terraces) and scripted abilities (laser refractors, resonance shockwaves, teleporting clones, prismatic spears).
- Loot table integration delivering Prism/Resonant/Quantum/Fractal/Seraph cores plus Exotic Heart, Fractablade, Breaker Core progression hooks.
- Progression wiring into boss core matrix + GT unlock config.

**Acceptance**

- Each mini-boss encounter functions with arena hazards and drops the correct core; Reality Breaker runs all phases with spatial distortion effects and rewards.
- `./gradlew :forge:runData` (for structures) + `./gradlew check` pass; document outstanding FX tuning if any.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics/arenas/loot
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_bosses.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
