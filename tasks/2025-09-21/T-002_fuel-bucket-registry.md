# T-002 Fuel Bucket & Fluid Registry Fixes

**Goal**
- Add missing DT Mix / He3 Blend / Exotic Mix bucket items and fluid blocks so CHEXFluids compiles and fluids can be used in recipes.

**Scope**
- orge/src/main/java/com/netroaki/chex/registry/items/CHEXItems.java
- orge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java
- orge/src/main/java/com/netroaki/chex/registry/fluids/CHEXFluids.java
- Item/block JSON models + lang if needed.

**Acceptance**
- Bucket items exist for DT Mix, He3 Blend, Exotic Mix.
- Corresponding liquid blocks are registered and referenced by CHEXFluids.
- Any required textures/models/lang entries stubbed or noted.
- ./gradlew check passes.

**Checklist**
- [ ] ash scripts/cloud_bootstrap.sh
- [ ] Implement registrations, add placeholder assets if required
- [ ] ./gradlew :common:spotlessApply :forge:spotlessApply
- [ ] ./gradlew check
- [ ] Document in progress/stepXX_fuel_buckets.md and PROGRESS_PROMPTS.md
- [ ] Open PR referencing this task file
