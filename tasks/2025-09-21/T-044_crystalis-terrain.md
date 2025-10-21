# T-044 Crystalis Terrain

**Goal**

- Design and implement the unique crystalline terrain features for the Crystalis dimension, focusing on crystal formations, floating shards, light refraction, and biome colors.

**Scope**

- Create custom crystal block variants and their generation
- Implement floating crystal shards with physics
- Add light refraction and reflection effects
- Configure biome-specific color palettes

**Acceptance Criteria**

- Crystal formations generate naturally in appropriate biomes
- Floating shards have proper physics and visual effects
- Light behaves realistically with the crystal structures
- Biome colors are visually distinct and thematically appropriate
- `./gradlew check` passes without errors

**Technical Requirements**

- Use custom block states for crystal growth stages
- Implement custom block entity for dynamic crystal rendering
- Use particle effects for light refraction
- Configure biome colors in datapacks

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create crystal block variants (small, medium, large, cluster)
- [ ] Implement crystal growth mechanics
- [ ] Add floating shard entities with physics
- [ ] Create light refraction shaders
- [ ] Configure biome color palettes
- [ ] Test generation in different biomes
- [ ] `./gradlew runData`
- [ ] `./gradlew check`
- [ ] Document implementation in `progress/`
- [ ] Open PR referencing this task

**Implementation Notes**

- Use Forge's block state JSON for crystal variants
- Consider using GeckoLib for animated crystal growth
- Implement custom block break particles for crystal blocks
- Add sound effects for crystal interactions
- Ensure compatibility with existing world generation
