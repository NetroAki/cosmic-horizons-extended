# T-141 Shattered Dyson Swarm Blocks & Structures

**Goal**

- Implement Dyson Swarm block set (Dyson Panels/Damaged Panels, Node Fragments, Circuit Blocks, Scaffold Struts, Rotary Nodes, Shadow Panels, Irradiated Stone, Relay Nodes, Signal Struts) and structural pieces (floating panel arrays, broken node clusters, scaffold lattice segments, shadow wedges, relay towers) per design doc.

**Scope**

- Block registrations with reflective/emissive materials, animated circuitry, and radiation emissivity.
- Structure templates/processors for debris fields, panel stacks, relay towers, shielded shadow shelters; placement integration.
- Optional photonic fungi/ambient feature hooks referenced in doc.

**Acceptance**

- Blocks render with required materials; structures spawn in intended biomes with rotation/spacing validated.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks/structures + assets
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dysonswarm_structures.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
