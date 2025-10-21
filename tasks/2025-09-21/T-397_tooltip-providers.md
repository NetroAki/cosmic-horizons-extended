# T-397 Tooltip Providers

**Goal**

- Add tooltip providers summarizing rocket tier, required fuel, destination hazards, and suit requirements for clear player information.

**Scope**

- `forge/src/main/java/com/netroaki/chex/client/tooltips/`
- Tooltip provider system
- Rocket and planet information tooltips

**Acceptance**

- Tooltip providers implemented
- Rocket tier information displayed
- Required fuel information shown
- Destination hazards displayed
- Suit requirements shown
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create tooltip provider system
- [ ] Implement rocket tier tooltips
- [ ] Add fuel requirement tooltips
- [ ] Add destination hazard tooltips
- [ ] Add suit requirement tooltips
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_tooltip_providers.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
