# Planet Registry Overrides - Plan

## Objective
Implement configuration-driven overrides for discovered planets so `chex-planets.json5` can adjust suit tags, fuels, hazards, and descriptions. Ensure behaviour is validated with unit tests.

## Key Tasks
- Fix `PlanetOverridesCore.load` so it parses both flat and sectioned JSON5 layouts.
- Ensure `PlanetRegistry.registerDiscoveredPlanets` applies overrides when available and capture override usage for logging/debugging.
- Add JUnit tests covering override parsing and merge behaviour (happy path + missing fields).
- Clean up duplicate `PlanetOverridesCore` implementations to avoid stale behaviour.

## Testing Plan
- Run `./gradlew :common:spotlessApply :forge:spotlessApply` for formatting.
- Execute `./gradlew check` to run compilation and new tests.
