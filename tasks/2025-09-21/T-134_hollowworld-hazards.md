# T-134 Hollow World Hazards & Atmospherics

**Goal**
- Implement Hollow World hazards: darkness patches extinguishing light, void chasm instakill zones, crystal grove radiation bursts, inverted gravity cues, and biolum ambience.

**Scope**
- Hazard controllers for darkness debuffs, void boundary checks, radiation pulses, and inverted gravity effects.
- Lighting/particle/audio work (biolum glow, void hum, crystal resonance) plus skybox adjustments.
- Config/progression updates documenting suit requirements and core unlock mapping (Fungal/Void/Crystal/Stalactite/River/Hollow Heart).

**Acceptance**
- Hazards operate in correct biomes with mitigation options (suits, equipment) and logging for balancing; ambience matches design doc.
- `./gradlew check` passes; update notes with tuning data.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazard/atmosphere systems
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollowworld_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
