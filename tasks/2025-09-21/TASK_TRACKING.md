# Cosmic Horizons Expanded - Task Tracking

## Core Systems (000-019)

- [x] **T-001**: Planet definition alignment `[tasks/2025-09-21/T-001_planet-def-alignment.md]`
  - [x] Added temperature, radiationLevel, and baseOxygen fields to PlanetDef
  - [x] Updated validation and helper methods
  - [x] Updated documentation
- [x] **T-002**: Update CosmicHorizonsIntegration `[tasks/2025-09-21/T-002_cosmic-horizons-integration.md]`
  - [x] Added calculation methods for environmental factors
  - [x] Updated planet creation with new fields
- [x] **T-003**: Update PlanetOverrides `[tasks/2025-09-21/T-003_planet-overrides.md]`
  - [x] Added support for new fields in override system
  - [x] Updated JSON serialization/deserialization
- [x] **T-004**: Update PlanetOverrideMerger `[tasks/2025-09-21/T-004_planet-override-merger.md]`
  - [x] Added merging logic for new fields
  - [x] Added validation and clamping
- [x] **T-005**: Test Planet Definition System `[tasks/2025-09-21/T-005_planet-def-testing.md]`
  - [x] Test loading with various configurations
  - [x] Verify override behavior
  - [x] Test edge cases and validation
- [x] **T-006**: Update Documentation `[tasks/2025-09-21/T-006_planet-docs.md]`
  - [x] Document new fields in configuration
  - [x] Add examples for environmental settings
  - [x] Update API documentation
- [x] **T-007**: Add Validation Tests `[tasks/2025-09-21/T-007_validation-tests.md]`
  - [x] Unit tests for validation logic
  - [x] Test value clamping
  - [x] Test error handling
- [x] **T-008**: Performance Testing `[tasks/2025-09-21/T-008_performance-tests.md]`
  - [x] Profile planet definition loading
  - [x] Optimize if necessary
- [x] **T-009**: Fuel bucket registry `[tasks/2025-09-21/T-009_fuel-bucket-registry.md]`
  - [x] Added bucket items for DT Mix, He3 Blend, and Exotic Mix
  - [x] Registered fluid blocks for each fuel type
  - [x] Created placeholder textures and models
  - [x] Verified registration in CHEXFluids
- [x] **T-003**: TerraBlender constants `[tasks/2025-09-21/T-003_terrablender-constants.md]`
  - [x] Updated `CHEXRegion` to use current TerraBlender API constants
  - [x] Replaced deprecated constants with current ones
  - [x] Added descriptive comments for each biome definition
  - [x] Verified all biome parameters are within valid ranges
- [x] **T-004**: JSON validation tool `[tasks/2025-09-21/T-004_json-validation-tool.md]`
  - [x] Created script to validate JSON and JSON5 files
  - [x] Added support for UTF-8 BOM detection
  - [x] Excluded node_modules and build directories
  - [x] Added JSON5 support with fallback
  - [x] Fixed all JSON validation errors in the codebase
- [x] **T-005**: Planet registry overrides `[tasks/2025-09-21/T-005_planet-registry-overrides.md]`
  - [x] Added support for all planet properties in overrides
  - [x] Implemented JSON5 configuration parsing
  - [x] Added validation and type safety
  - [x] Created comprehensive tests
- [x] **T-006**: Dump planets command `[tasks/2025-09-21/T-006_dump-planets-command.md]`
  - [x] Added `/chex dump_planets` command with --reload flag
  - [x] Implemented JSON file output
  - [x] Added command suggestions and help text
- [x] **T-007**: Travel graph validation `[tasks/2025-09-21/T-007_travel-graph-validation.md]`
  - [x] Added validation for unknown planets in travel graph
  - [x] Added validation for missing planets in travel graph
  - [x] Added tier consistency checks
  - [x] Implemented `/chex travelgraph validate` command
  - [x] Added detailed error reporting and formatting
- [x] **T-008**: Player tier capability `[tasks/2025-09-21/T-008_player-tier-capability.md]`
  - [x] Implemented PlayerTierCapability with rocket/suit tiers and milestones
  - [x] Added network synchronization
  - [x] Integrated with LaunchHooks and DimensionHooks
  - [x] Added comprehensive test coverage
- [x] **T-009**: Fuel registry fallbacks `[tasks/2025-09-21/T-009_fuel-registry-fallbacks.md]`
  - [x] Implemented fallback fluids (kerosene, rp1, lox, lh2, dt_mix, he3_blend, exotic_mix)
  - [x] Added textures and models for all fuel types
  - [x] Created language entries and bucket items
  - [x] Updated FuelRegistry to handle fallback logic
  - [x] Added comprehensive documentation

## Pandora (020-029)

- [x] **T-020**: Dimension JSON `[tasks/2025-09-21/T-020_pandora-dimension-json.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Author dimension JSON + supporting files
  - [x] `./gradlew :forge:runData`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step20_pandora_dimension.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [x] **T-021**: Biomes `[tasks/2025-09-21/T-021_pandora-biomes.md]`
  - [x] Created biome JSONs for all Pandora biomes
  - [x] Configured climate, fog, and weather settings
  - [x] Added ambience and special effects
  - [x] Set up spawn settings and mobs
  - [x] Added custom features and carvers
  - [x] Documented all configurations
- [x] **T-022**: Blocks `[tasks/2025-09-21/T-022_pandora-blocks.md]`
  - [x] Created block registrations for all Pandora blocks
  - [x] Added placeholder textures using ImageMagick
  - [x] Created blockstate and model JSONs
  - [x] Set up item models for block items
  - [x] Documented all configurations
- [x] **T-023**: Flora generation `[tasks/2025-09-21/T-023_pandora-flora-generation.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Add feature builders + JSON definitions
  - [x] `./gradlew :forge:runData`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step23_pandora_flora.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [x] **T-024**: Fauna `[tasks/2025-09-21/T-024_pandora-fauna.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Implement entity classes + assets
  - [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step24_pandora_fauna.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [x] **T-025**: Bosses `[tasks/2025-09-21/T-025_pandora-bosses.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Implement boss mechanics, structures, loot
  - [x] `./gradlew :forge:runData`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step25_pandora_bosses.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [x] **T-026**: Hazards & audio `[tasks/2025-09-21/T-026_pandora-hazards-audio.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Add hazard logic + assets
  - [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step26_pandora_hazards_audio.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [x] **T-027**: GTCEu integration `[tasks/2025-09-21/T-027_pandora-gtceu.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Update configs + any registry hooks
  - [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step27_pandora_gtceu.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task

## Moon (025-029)

- [ ] **T-025**: Dimension `[tasks/2025-09-21/T-025_moon-dimension.md]`
- [ ] **T-026**: Biomes `[tasks/2025-09-21/T-026_moon-biomes.md]`
- [ ] **T-027**: Blocks `[tasks/2025-09-21/T-027_moon-blocks.md]`
- [ ] **T-028**: Boss (Lunar Overlord) `[tasks/2025-09-21/T-028_moon-boss.md]`
- [ ] **T-029**: Environment `[tasks/2025-09-21/T-029_moon-environment.md]`

## Arrakis (030-039) - IN PROGRESS

- [x] **T-030**: Dimension JSON `[tasks/2025-09-21/T-030_arrakis-dimension-json.md]`
- [x] **T-031**: Biomes `[tasks/2025-09-21/T-031_arrakis-biomes.md]`
- [x] **T-032**: Blocks `[tasks/2025-09-21/T-032_arrakis-blocks.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Implement blocks + assets
  - [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step32_arrakis_blocks.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [x] **T-033**: World features `[tasks/2025-09-21/T-033_arrakis-features.md]`
  - [x] Dune generation
  - [x] Rock formations
  - [x] Canyons
  - [x] Mesas
- [x] **T-034**: Flora `[tasks/2025-09-21/T-034_arrakis-flora.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Implement flora blocks + behaviours
  - [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step34_arrakis_flora.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [x] **T-035**: Fauna `[tasks/2025-09-21/T-035_arrakis-fauna.md]`
  - [x] `bash scripts/cloud_bootstrap.sh`
  - [x] Implement fauna entities + behaviors
  - [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
  - [x] `./gradlew check`
  - [x] Log entry `progress/step35_arrakis_fauna.md` + `PROGRESS_PROMPTS.md`
  - [x] Open PR referencing this task
- [ ] **T-036**: Boss (Sand Emperor) `[tasks/2025-09-21/T-036_arrakis-boss.md]`
  - [ ] Implement Sand Emperor entity
  - [ ] Create boss arena with sand mechanics
  - [ ] Add boss phases and abilities
  - [ ] Implement boss loot drops
- [ ] **T-037**: Environment `[tasks/2025-09-21/T-037_arrakis-environment.md]`
  - [ ] Sandstorm weather system
  - [ ] Heat damage mechanics
  - [ ] Spice particle effects
  - [ ] Desert survival mechanics

## Alpha Centauri A (040-049)

)
)

- [ ] **T-040**: Dimension `[tasks/2025-09-21/T-040_alpha-centauri-dimension.md]`
- [ ] **T-041**: Hazards `[tasks/2025-09-21/T-041_alpha-centauri-hazards.md]`
- [ ] **T-042**: Structures `[tasks/2025-09-21/T-042_alpha-centauri-structures.md]`
- [ ] **T-043**: Entities `[tasks/2025-09-21/T-043_alpha-centauri-entities.md]`
- [ ] **T-044**: Boss `[tasks/2025-09-21/T-044_alpha-centauri-boss.## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

md]`## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Kepler-452b (050-059)

- [ ] **T-050**: Dimension `[tasks/2025-09-21/T-050_kepler-452b-dimension.md]`
- [ ] **T-051**: Biomes `[tasks/2025-09-21/T-051_kepler-452b-biomes.md]`
- [ ] **T-052**: Blocks `[tasks/2025-09-21/T-052_kepler-452b-blocks.md]`
- [ ] **T-053**: Flora & Fauna `[tasks/2025-09-21/T-053_kepler-452b-flora-fauna.md]`
- [ ] **T-054**: Boss `[tasks/2025-09-21/T-054_kepler-452b-boss.md]`
- [ ] **T-055**: Environment `[tasks/2025-09-21/T-055_kepler-452b-environment.md]`

## Aqua (060-069) - IN PROGRESS

- [x] **T-060**: Dimension `[tasks/2025-09-21/T-060_aqua-dimension.md]`
  - [x] Basic dimension setup
  - [x] Biome configurations
  - [x] Noise settings for underwater terrain
- [x] **T-061**: Mechanics `[tasks/2025-09-21/T-061_aqua-mechanics.md]`
  - [x] Oxygen system implementation
  - [x] Pressure mechanics
  - [x] Thermal damage system
  - [x] Configuration options
- [x] **T-062**: Blocks `[tasks/2025-09-21/T-062_aqua-blocks.md]`
  - [x] Vent basalt and polished variant
  - [x] Manganese nodules
  - [x] Luminous kelp and fluid
  - [x] Ice shelf and slabs
  - [x] Coral formations
- [x] **T-063**: Fauna `[tasks/2025-09-21/T-063_aqua-fauna.md]`
  - [x] Luminfish entity with school behavior
  - [x] Custom AI and animations
  - [x] Spawn egg and textures
- [ ] **T-064**: Boss `[tasks/2025-09-21/T-064_aqua-boss.md]`
  - [ ] Design boss mechanics
  - [ ] Implement boss entity
  - [ ] Create boss arena
  - [ ] Add drops and rewards
- [ ] **T-065**: Audio/Visual `[tasks/2025-09-21/T-065_aqua-audio-visual.md]`
  - [ ] Underwater sound effects
  - [ ] Particle effects for bubbles and currents
  - [ ] Screen shaders for underwater view

## Inferno (070-