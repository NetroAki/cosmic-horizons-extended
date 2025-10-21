# T-407 Sound Events Creation

**Goal**

- Create sound events (launch countdown, suit alarm, planet ambience per world) with proper audio integration.

**Scope**

- `forge/src/main/resources/assets/cosmic_horizons_extended/sounds/`
- Sound events system
- Launch, suit, and planet ambience sounds

**Acceptance**

- Sound events created
- Launch countdown sounds working
- Suit alarm sounds working
- Planet ambience per world
- Proper audio integration
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create sound events system
- [ ] Implement launch countdown sounds
- [ ] Add suit alarm sounds
- [ ] Add planet ambience sounds
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_sound_events_creation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
