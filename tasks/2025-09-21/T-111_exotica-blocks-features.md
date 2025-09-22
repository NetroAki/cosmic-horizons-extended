# T-111 Exotica Blocks & Features

**Goal**
- Implement Exotica block families (Exotite, Quantum Crystal, Chromatic Grass/Prismatic Soil, Resonant Sand/Harmonic Stone, Phase Blocks, Fractal Bark/Recursive Leaves, Prism Rock/Crystal Lattices) and associated world features (Prism Trees, Color Reeds, Echo Cacti, Quantum Saplings, Fractal Trees, Light Vines).

**Scope**
- Block registrations, blockstates/models/loot/recipes with animated/colour-shifting render layers where needed.
- Configured/placed features for flora with distortion behaviours (phase blocks toggling solidity, color-cycling foliage).
- Integration hooks for resonance sounds/particle systems referenced in design doc.

**Acceptance**
- Blocks and features appear in appropriate biomes with expected visuals/behaviours (colour cycling, sound-emitting sand).
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks/features + assets
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_features.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
