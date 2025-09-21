# Step 21 - Planet Registry Overrides (2025-09-21)

## Summary
- Implemented configuration merge pipeline so `PlanetRegistry.registerDiscoveredPlanets` applies `chex-planets.json5` overrides for fuel, suit tags, hazards, and descriptions.
- Reworked `PlanetOverridesCore.load` to support both flat and sectioned JSON layouts and trimmed duplicate Forge copy.
- Added detailed logging for override application counts during discovery reloads.
- Authored JUnit tests validating override parsing and merge behaviour across happy-path and missing-field scenarios.

## Files
- `forge/src/main/java/com/netroaki/chex/registry/PlanetRegistry.java`
- `common/src/main/java/com/netroaki/chex/config/PlanetOverridesCore.java`
- `common/src/test/java/com/netroaki/chex/config/PlanetOverridesCoreTest.java`
- `common/src/test/java/com/netroaki/chex/config/PlanetOverrideMergerTest.java`
- `CHEX_DETAILED_TASKS.md`
- `PROGRESS_PROMPTS.md`

## Testing
- `./gradlew :common:spotlessApply :forge:spotlessApply`
- `./gradlew check`
