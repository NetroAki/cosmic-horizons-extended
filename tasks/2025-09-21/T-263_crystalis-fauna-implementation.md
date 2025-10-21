# T-263 Crystalis Fauna Implementation

**Goal**

- Implement diverse fauna for Crystalis with frozen, crystal, and cold-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Snow Striders: creatures adapted to icy terrain
- Cryo Drakes: dragon-like creatures adapted to cold
- Ice Wraiths: ghostly entities made of ice
- Crystal Beetles: small creatures with crystalline armor
- Frost Wolves: pack hunters adapted to cold environments
- Ice Spiders: spider-like creatures with ice-based attacks
- Crystal Birds: flying creatures with crystalline feathers
- Frost Giants: large humanoid creatures adapted to cold
- Ice Elementals: creatures made of pure ice energy
- Crystal Fish: aquatic creatures in frozen lakes
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Snow Striders
- [ ] Create Cryo Drakes
- [ ] Add Ice Wraiths
- [ ] Implement Crystal Beetles
- [ ] Create Frost Wolves with pack behavior
- [ ] Add Ice Spiders with ice attacks
- [ ] Implement Crystal Birds and Frost Giants
- [ ] Create Ice Elementals and Crystal Fish
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
