# T-394 Mission Destination Restrictions

**Goal**

- Implement mission destination restrictions with tier-based access control and progression requirements.

**Scope**

- `forge/src/main/java/com/netroaki/chex/launch/restrictions/`
- Mission destination restrictions system
- Tier-based access control

**Acceptance**

- Mission destination restrictions implemented
- Tier-based access control working
- Progression requirements enforced
- Destination validation system
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create mission destination restrictions
- [ ] Implement tier-based access control
- [ ] Add progression requirements
- [ ] Add destination validation
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mission_destination_restrictions.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
