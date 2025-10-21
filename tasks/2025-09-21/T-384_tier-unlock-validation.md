# T-384 Tier Unlock Validation System

**Goal**

- Implement tier unlock validation system to ensure proper progression and prevent bypassing required boss defeats.

**Scope**

- `forge/src/main/java/com/netroaki/chex/progression/validation/`
- Tier unlock validation system
- Progression validation tools

**Acceptance**

- Tier unlock validation system implemented
- Proper progression validation
- Boss defeat requirements enforced
- Progression bypass prevention
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create tier unlock validation system
- [ ] Implement progression validation
- [ ] Add boss defeat requirements
- [ ] Prevent progression bypassing
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_tier_unlock_validation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
