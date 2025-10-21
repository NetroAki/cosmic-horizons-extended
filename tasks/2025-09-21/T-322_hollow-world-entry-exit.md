# T-322 Hollow World Entry/Exit Configuration

**Goal**

- Configure and implement the entry and exit points for the Hollow World dimension.

**Scope**

- `forge/src/main/java/com/netroaki/chex/world/hollow/`
- Portal block and frame registration
- Dimension travel logic
- Spawn point configuration
- Teleportation handling

**Acceptance**

- Players can enter the Hollow World through a portal
- Players return to the correct location in the Overworld
- Safe spawn points are configured
- No world generation issues at entry/exit points
- `./gradlew check` passes

**Checklist**

- [x] `bash scripts/cloud_bootstrap.sh`
- [x] Configure Spawn Points
  - Set default spawn point in Hollow World
  - Handle player spawn on first entry
  - Add spawn protection
- [x] Implement Entry Logic
  - Create portal activation sequence
  - Add visual and sound effects
  - Handle player state during transfer
- [x] Implement Exit Logic
  - Configure return point calculation
  - Add cooldown system
  - Handle inventory and effects
- [x] Add Safety Checks
  - Prevent teleportation into blocks
  - Handle chunk loading
  - Add fallback spawn points
- [x] Test Dimension Transfer
  - Test Overworld → Hollow World
  - Test Hollow World → Overworld
  - Test with different Y-levels
- [x] `./gradlew :forge:runData`
- [x] `./gradlew check`
- [x] Log entry `progress/stepXX_hollow_world_entry_exit.md` + `PROGRESS_PROMPTS.md`
- [x] Open PR referencing this task file
