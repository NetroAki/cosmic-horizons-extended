# T-396 Launch Failure Feedback System

**Goal**

- Add launch failure feedback system with clear error messages, failure reasons, and player guidance for launch issues.

**Scope**

- `forge/src/main/java/com/netroaki/chex/launch/feedback/`
- Launch failure feedback system
- Error messages and player guidance

**Acceptance**

- Launch failure feedback system implemented
- Clear error messages provided
- Failure reasons explained
- Player guidance for issues
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create launch failure feedback system
- [ ] Implement clear error messages
- [ ] Add failure reason explanations
- [ ] Add player guidance
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_launch_failure_feedback.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
