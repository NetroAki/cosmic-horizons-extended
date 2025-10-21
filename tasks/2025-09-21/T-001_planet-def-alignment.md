# T-001 PlanetDef Alignment

**Goal**

- Restore PlanetDef usage across Forge/common so it matches the current record fields (hazards + mineral sets) and compiles cleanly.

**Scope**

- orge/src/main/java/com/netroaki/chex/integration/CosmicHorizonsIntegration.java
- orge/src/main/java/com/netroaki/chex/config/PlanetOverrides.java
- common/src/main/java/com/netroaki/chex/config/PlanetOverrideMerger.java
- Any other
  ew PlanetDef(...) callsites that fail after the change.

**Acceptance**

- All
  ew PlanetDef(...) calls supply hazards + mineral sets as required.
- Overrides merge logic no longer relies on removed record fields and merges optional fuel/description/hazards correctly.
- ./gradlew check passes.

**Checklist**

- [ ] Run ash scripts/cloud_bootstrap.sh
- [ ] Implement code updates and run ./gradlew :common:spotlessApply :forge:spotlessApply
- [ ] ./gradlew check
- [ ] Update progress/stepXX_planetdef.md, append to PROGRESS_PROMPTS.md
- [ ] Open PR referencing this task file
