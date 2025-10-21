# T-255 Inferno Prime Fauna Implementation

**Goal**

- Implement diverse fauna for Inferno Prime with volcanic, lava, and ash-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Ash Crawlers: creatures adapted to ash-covered terrain
- Fire Wraiths: flame-based entities
- Magma Hoppers: creatures that jump between lava pools
- Lava Worms: creatures that swim through molten rock
- Ash Beetles: small creatures that feed on volcanic ash
- Volcanic Drakes: dragon-like creatures adapted to extreme heat
- Basalt Crawlers: creatures with rock-like armor
- Obsidian Spiders: spider-like creatures with crystalline bodies
- Lava Eels: serpentine creatures that swim through molten rock
- Fire Bats: flying creatures that emit sparks
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Ash Crawlers
- [ ] Create Fire Wraiths
- [ ] Add Magma Hoppers with lava jumping
- [ ] Implement Lava Worms
- [ ] Create Ash Beetles and Volcanic Drakes
- [ ] Add Basalt Crawlers and Obsidian Spiders
- [ ] Implement Lava Eels and Fire Bats
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
