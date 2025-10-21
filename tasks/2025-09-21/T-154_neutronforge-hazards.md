# T-154 Neutron Star Forge Hazards & Progression

**Goal**

- Implement Neutron Star Forge hazards: magnetic storms disrupting equipment, radiation bursts, gravity wells pulling players, plasma eruptions, and progression documentation (Accretion/Magnetar/Forge/Graviton/Shelter cores, Sovereign Heart).

**Scope**

- Hazard controllers for magnetic disarms, radiation tick damage, gravity well pulls, plasma eruptions, and suit requirement checks.
- Audio/visual FX (magnetic arcs, plasma flares, gravity distortion) and telemetry logging.
- Progression config updates tying cores/hearts to GT unlocks and suit thresholds.

**Acceptance**

- Hazards trigger per biome with mitigation options (shielding, anchoring) and log outputs; progression documentation refreshed.
- `./gradlew check` passes; balancing notes recorded.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazard/progression systems
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutronforge_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
