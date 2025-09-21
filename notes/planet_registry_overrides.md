# Planet Registry Overrides Plan

## Context
- Task: T-005 (Planet registry override merge) from `CHEX_DETAILED_TASKS.md`.
- Goal: Ensure discovered Cosmic Horizons planets can be overridden via `config/chex/chex-planets.json5` without losing base data, especially hazard flags and descriptive text.
- References: `PROJECT_CONTEXT.md` (override config), `PlanetOverridesCore` parsing utilities, and `PlanetOverrideMerger` helper used by `PlanetRegistry`.

## Implementation Outline
1. Extend the discovery pipeline so hazards declared by Cosmic Horizons are captured when mirroring into CHEX `PlanetDef` records.
2. Update the override merger to union hazard tags instead of replacing them, preserving vanilla/discovered data while still allowing new hazard IDs from configuration.
3. Ensure other override fields (fuel, suit tier/tag, description) respect config values while falling back to the base discovery info when absent.
4. Add focused unit tests in `common` module that exercise `PlanetOverrideMerger.merge` with representative override entries, covering hazard union and description fallback cases.
5. Run Spotless + Gradle check to validate formatting and compilation.
