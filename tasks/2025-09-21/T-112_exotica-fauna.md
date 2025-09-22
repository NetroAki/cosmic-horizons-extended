# T-112 Exotica Fauna Implementation

**Goal**
- Implement Exotica fauna roster: Chroma Grazers, Spectral Hares, Tone Beasts, Sonic Drifters, Phase Stalkers, Echo Wisps, Fractal Crawlers, Pattern Serpents, Prism Serpents, Rainbow Mantises with biome-appropriate behaviours and GT-focused drops.

**Scope**
- Entity registrations, AI/animation controllers, spawn rules, and loot tables for listed mobs.
- Behaviour hooks for phasing/teleportation, resonance sound pulses, low-gravity movement, and colour-shift shaders per design doc.
- Drop integration to GT photonics/quantum chains (prism hides, spectral fur, resonant bones, light essence, fractal dust, prismatic wings).

**Acceptance**
- Each fauna type spawns in its intended biome with signature behaviour (phase blink, resonance knockback, colour shimmer) and drops mapped to GT recipes.
- `./gradlew check` passes; note any animation TODOs in progress log.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement fauna + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_fauna.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
