# T-357 Config Reload Command

**Goal**

- Implement config reload command to refresh mineral distributions at runtime without server restart for easier development and testing.

**Scope**

- `forge/src/main/java/com/netroaki/chex/commands/`
- Config reload command system
- Runtime configuration refresh functionality

**Acceptance**

- Config reload command available in-game
- Mineral distributions refresh without restart
- Command provides feedback on reload status
- Integration with existing config system
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create config reload command
- [ ] Implement mineral distribution refresh
- [ ] Add reload status feedback
- [ ] Integrate with config system
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_config_reload_command.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
