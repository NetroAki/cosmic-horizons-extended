# Step 21 - Planet Registry Overrides (2025-09-21)

## Summary
- Captured Cosmic Horizons hazard data when mirroring discovered planets into CHEX `PlanetDef` records.
- Adjusted override merging to union hazard sets and preserve base descriptions/fuel/suit data when optional config entries are omitted.
- Authored unit tests for `PlanetOverrideMerger.merge` verifying hazard union and fallback behaviours.

## Files Updated
- `forge/src/main/java/com/netroaki/chex/registry/PlanetRegistry.java`
- `common/src/main/java/com/netroaki/chex/config/PlanetOverrideMerger.java`
- `common/src/test/java/com/netroaki/chex/config/PlanetOverrideMergerTest.java`
- `CHEX_DETAILED_TASKS.md`
- `notes/planet_registry_overrides.md`

## Notes
- Unit tests currently live in the common module and cover union + fallback cases.
- `./gradlew --console=plain check` still fails due to longstanding missing GTCEu assets and Forge worldgen constants; document in final summary.
