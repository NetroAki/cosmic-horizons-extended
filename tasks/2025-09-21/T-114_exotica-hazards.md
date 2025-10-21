# T-114 Exotica Hazards & Visuals

**Goal**

- Implement Exotica environmental effects: random spatial distortions/short-range teleports, quantum glade refraction debuffs, fractal gravity fluctuations, colour-cycling skybox, and phasing ambient mobs/particles.

**Scope**

- Hazard systems tied to suit/tech checks (teleport resistance, nausea mitigation) with config knobs.
- Visual/audio layers for chromatic haze, light refraction shaders, resonance hum loops, and biome-specific particle FX.
- Gravity modifier hooks that fluctuate in Fractal Forest zones.

**Acceptance**

- Hazards trigger in appropriate biomes with tuning exposed; players can mitigate via config/suit tier; visuals/audio match design doc references.
- `./gradlew check` passes; capture tuning notes in progress log.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazard/visual systems
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
