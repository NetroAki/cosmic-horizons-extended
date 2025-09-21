# Planet Registry Overrides - Analysis (2025-09-21)

## Context
- Task reference: CHEX_DETAILED_TASKS.md §1 bullet 1, tasks/2025-09-21/T-005_planet-registry-overrides.md.
- Scope: ensure `PlanetRegistry.registerDiscoveredPlanets` merges override data from `config/chex/chex-planets.json5` and provide unit coverage.
- Key docs reviewed: PROJECT_CONTEXT.md (override system summary), PROGRESS_PROMPTS.md entries 15-19, notes/planet_summary/_Progression_Context.md for tier expectations.

## Current State Review
- `PlanetRegistry` already loads overrides via `loadPlanetOverrides()` and applies them through `applyOverrides(...)` when ingesting Cosmic Horizons discovery snapshots.
- `PlanetOverrideMerger` handles name/tier/suit/fuel/description/hazards but currently replaces hazard sets when an override is present instead of merging.
- No unit tests exist under `common/src/test/java` to verify override behaviour; regression coverage is zero.

## Action Plan
1. Update `PlanetRegistry.applyOverrides` so override hazard entries are merged with the discovered base hazards rather than replacing them entirely. Preserve deterministic ordering (LinkedHashSet) while keeping lowercase normalisation from `PlanetDef`.
2. Extend override handling to treat empty override strings/arrays as "no change" so config authors can omit fields safely.
3. Introduce new unit tests in `common` validating that:
   - Overrides for suit tag, fuel, description, and hazards merge correctly for discovered planets.
   - Empty override values do not erase discovered data.
   - Hazard overrides merge with base hazards while respecting case normalisation.
4. Wire tests through Gradle (`./gradlew check`) once implemented; update task matrix checkbox and append summary to PROGRESS_PROMPTS.md + new `progress/` step entry.

## Open Questions
- None at this time; override merging requirements are explicit in the task brief.
