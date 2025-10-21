# T-380 Boss Health Scaling System

**Goal**

- Add boss health scaling based on player count and difficulty for balanced boss encounters across different group sizes.

**Scope**

- `forge/src/main/java/com/netroaki/chex/boss/scaling/`
- Boss health scaling system
- Player count and difficulty scaling

**Acceptance**

- Boss health scaling system implemented
- Player count scaling working
- Difficulty scaling working
- Balanced boss encounters
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create boss health scaling system
- [ ] Implement player count scaling
- [ ] Add difficulty scaling
- [ ] Balance boss encounters
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_boss_health_scaling.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
