# T-144 Shattered Dyson Swarm Hazards & Progression

**Goal**

- Implement Dyson Swarm hazards: timed solar radiation bursts, debris collisions, vacuum exposure checks, zero-G navigation aids, and document progression hooks (Solar/Node/Scaffold/Radiant/Relay cores, Apex Core).

**Scope**

- Hazard controllers for radiation pulses, debris spawn/destruction, and suit requirement checks.
- Zero-G movement assistance modules and optional thruster integration.
- Progression config updates and documentation linking cores/hearts to GT unlocks.

**Acceptance**

- Hazards operate on schedule with mitigation pathways (shield modules, shelters) and feed telemetry; progression docs updated.
- `./gradlew check` passes; note balancing data in progress log.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazard/progression systems
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dysonswarm_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
