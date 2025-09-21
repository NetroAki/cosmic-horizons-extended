# T-003 TerraBlender Constant Updates

**Goal**
- Update CHEXRegion to use the current TerraBlender climate constants for 1.20.1 (e.g., MID_INLAND, Depth.HIGH, etc.) so the class compiles and the overlay matches the new API.

**Scope**
- orge/src/main/java/com/netroaki/chex/worldgen/region/CHEXRegion.java
- Any related helper builders if compilation errors surface.

**Acceptance**
- All deprecated/removed constants (HALF_INLAND, Depth.HIGH_OFFSET, etc.) replaced with valid alternatives.
- Builder chains still cover intended climate ranges (document any behavioural adjustments in notes).
- ./gradlew check passes.

**Checklist**
- [ ] ash scripts/cloud_bootstrap.sh
- [ ] Update constants + comments as needed
- [ ] ./gradlew :common:spotlessApply :forge:spotlessApply
- [ ] ./gradlew check
- [ ] Log notes in progress/stepXX_terrablender.md and append to PROGRESS_PROMPTS.md
- [ ] Open PR referencing this task file
