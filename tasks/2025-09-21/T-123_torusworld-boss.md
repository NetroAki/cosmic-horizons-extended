# T-123 Torus Warden Boss Encounter

**Goal**

- Implement the Torus Warden main boss (Structural Defense → Gravity Collapse → Singularity Protocol) with rotating platform arena, gravity shift mechanics, and rewards (Torus Core, Warden’s Ringblade, Null Circuit Core).

**Scope**

- Boss entity behaviour: drone summons, gravity flip scheduling, micro black hole pull, beam attacks.
- Central gyroscopic hub arena structure with moving platforms, collapse cues, and hazard scripting.
- Loot/progression integration linking Torus Core + other drops into GT exotic matter unlocks.

**Acceptance**

- Boss runs all phases reliably with gravity transitions and platform motion matching design doc; rewards granted and recorded in progression configs.
- `./gradlew :forge:runData` (if structures) + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics/arena/loot
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torusworld_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
