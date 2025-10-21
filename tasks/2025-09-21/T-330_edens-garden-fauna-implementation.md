# T-330 Eden's Garden Fauna Implementation

**Goal**

- Implement diverse fauna for Eden's Garden with paradise, healing, and harmony-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Garden Keepers: Gentle guardians that tend to all life (drop Keeper's Blessing)
- Healing Spirits: Spirits that provide regeneration and healing (drop Spirit Essence)
- Harmony Birds: Birds that sing songs of peace and harmony (drop Harmony Feathers)
- Peaceful Deer: Gentle deer that provide perfect materials (drop Deer Antlers)
- Tranquil Fish: Fish that swim in perfect harmony (drop Fish Scales)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Garden Keepers
- [ ] Create Healing Spirits
- [ ] Add Harmony Birds
- [ ] Implement Peaceful Deer
- [ ] Create Tranquil Fish
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_edens_garden_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
