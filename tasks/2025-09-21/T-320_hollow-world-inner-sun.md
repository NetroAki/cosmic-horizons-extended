# T-320 Hollow World Inner Sun Mechanics

**Goal**

- Implement the inner sun mechanics for the Hollow World dimension, including light emission, heat effects, and day/night cycle simulation.

**Scope**

- `forge/src/main/java/com/netroaki/chex/world/hollow/InnerSunManager.java`
- `forge/src/main/java/com/netroaki/chex/world/hollow/HollowWorldDimension.java`
- Light and heat effects system
- Day/night cycle simulation

**Acceptance**

- Inner sun provides directional light from the center of the Hollow World
- Heat effects on players and entities
- Dynamic light levels based on distance from the center
- Day/night cycle with smooth transitions
- `./gradlew check` passes

**Checklist**

- [x] `bash scripts/cloud_bootstrap.sh`
- [x] Create InnerSunManager class
  - Implemented with day/night cycle simulation
  - Added heat effects based on distance from center
  - Integrated with Forge event system
- [x] Implement light emission logic
  - Dynamic light levels based on time of day
  - Smooth transitions between day and night
  - Distance-based light attenuation
- [x] Add heat effects system
  - Heat damage at high intensities
  - Status effects in extreme heat
  - Distance-based heat calculation
- [x] Create day/night cycle simulation
  - 20-minute day/night cycle
  - Smooth transitions between phases
  - Configurable intensity thresholds
- [x] Integrate with HollowWorldDimension
  - Added light level calculations
  - Custom sky and fog rendering
  - Teleportation handling
- [x] Add configuration options
  - Adjustable day/night cycle length
  - Configurable heat damage thresholds
  - Toggleable effects
- [x] `./gradlew :forge:runData`
- [x] `./gradlew check`
- [x] Log entry `progress/stepXX_hollow_world_inner_sun.md` + `PROGRESS_PROMPTS.md`
- [x] Open PR referencing this task file
