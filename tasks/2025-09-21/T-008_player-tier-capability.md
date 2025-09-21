# T-008 Player Tier Capability

**Goal**
- Implement `PlayerTierCapability` tracking rocket/suit tiers and milestone flags, with sync packets and enforcement on launches/dimension entry.

**Scope**
- Capability class + storage networking in `forge/src/main/java/com/netroaki/chex/capabilities/`
- Hooks in LaunchHooks, DimensionHooks, networking package.
- Client feedback (toasts/messages) if necessary.

**Acceptance**
- Capability persists tiers + milestones per player.
- Server enforces tier requirements on launches/dimension transitions.
- Packets synchronize to clients; tests or manual verification plan documented.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement capability + sync + enforcement
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_player_tiers.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
