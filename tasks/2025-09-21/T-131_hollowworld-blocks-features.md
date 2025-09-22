# T-131 Hollow World Blocks & Features

**Goal**
- Implement Hollow World block sets (Hollowstone variants, Biolume Moss, Fungal Stone, Voidstone/Void Crystals, Crystal Bark/Prism Stone, Stalactite variants, Riverbed stone) and flora/features (Glowshrooms, Spore Reeds, Shadow Vines, Crystal Trees, Shard Flowers, hanging stalactite forests, biolum river flora).

**Scope**
- Block registrations with emissive/glowing materials, hanging variants, and flowing river assets.
- Configured/placed features for cavern mushrooms, void chasm platforms, crystal groves, stalactite forests, subterranean river vegetation.
- Loot tables/recipes for new blocks aligning with GT processing.

**Acceptance**
- Features populate intended biomes with lighting/hazard behaviour (void crystals glow, stalactites hang, moss emits light).
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks/features + assets
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollowworld_features.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
