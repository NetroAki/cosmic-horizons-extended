# T-321 Hollow World Portal System

**Goal**

- Implement a portal system for entering and exiting the Hollow World dimension.

**Scope**

- `forge/src/main/java/com/netroaki/chex/world/hollow/portal/`
- Portal block and item registration
- Portal frame construction rules
- Interdimensional teleportation logic
- Particle effects and sounds

**Acceptance**

- Players can construct a portal frame using special blocks
- Portal activates when the frame is complete
- Smooth transition between dimensions
- Visual and audio feedback
- `./gradlew check` passes

**Checklist**

- [x] `bash scripts/cloud_bootstrap.sh`
- [x] Create Portal Frame Block
  - Defined block properties with activation state
  - Created model and blockstate JSONs
  - Implemented flint and steel activation
- [x] Implement Portal Block
  - Created custom portal block with axis-based rendering
  - Added particle effects and sounds
  - Implemented entity teleportation
- [x] Add Portal Ignition System
  - Integrated with flint and steel
  - Added cooldown and activation checks
  - Implemented portal validation
- [x] Configure Dimension Travel
  - Created custom teleporter class
  - Handled player data transfer
  - Added safety checks and spawn points
- [x] Add Visual Effects
  - Portal particles with custom animation
  - Activation sound effects
  - Portal ambient sounds
- [x] `./gradlew :forge:runData`
- [x] `./gradlew check`
- [x] Log entry `progress/stepXX_hollow_world_portal.md` + `PROGRESS_PROMPTS.md`
- [x] Open PR referencing this task file
