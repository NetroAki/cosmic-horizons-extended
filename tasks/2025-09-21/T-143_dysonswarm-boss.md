# T-143 Dyson Apex Boss Encounter

**Goal**

- Implement the Dyson Apex boss (Solar Flare → Overload → Collapse) with zero-G arena, plasma arcs, collapsing panels, and rewards (Apex Core, Dyson Spear, Quantum Lens).

**Scope**

- Boss entity behaviour: plasma bolt barrages, EMP debuffs, platform destruction scheduling.
- Shattered node arena structure with zero-G movement rules and dynamic panel collapse.
- Loot/progression integration hooking Apex Core and other drops into GT stellar power unlocks.

**Acceptance**

- Encounter executes all phases with zero-G mechanics and dynamic panel hazards; rewards granted and progression wiring validated.
- `./gradlew :forge:runData` (if structures) + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics/arena/loot
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dysonswarm_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
