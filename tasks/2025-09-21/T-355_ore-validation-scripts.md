# T-355 Ore Validation Scripts

**Goal**

- Add scripts validating tag/block IDs for all ore distributions to ensure proper GTCEu integration and prevent missing registries.

**Scope**

- `scripts/validate_ore_distributions.py`
- Ore distribution validation system
- Block ID and tag validation tools

**Acceptance**

- Script validates all ore block IDs exist in GTCEu
- Tag validation ensures proper ore categorization
- Missing registry detection and reporting
- Integration with build process
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create ore validation script
- [ ] Add block ID validation
- [ ] Implement tag validation
- [ ] Add missing registry detection
- [ ] Integrate with build process
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ore_validation_scripts.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
