# T-045 Crystalis Resources

**Goal**

- Implement the resource system for the Crystalis dimension, including crystal growth mechanics, unique ores, and specialized tools/armor.

**Scope**

- Design and implement crystal growth mechanics
- Create unique crystal-based ores and resources
- Develop crystal tools and armor sets
- Balance resource distribution and progression

**Acceptance Criteria**

- Crystals grow over time with proper conditions
- Unique ores generate in appropriate biomes
- Tools and armor have unique properties
- Resource distribution supports balanced progression
- `./gradlew check` passes without errors

**Technical Requirements**

- Use Forge's block tick system for growth
- Implement custom tool materials and tiers
- Configure world generation for ores
- Add custom recipes for tools/armor

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement crystal growth mechanics
- [ ] Create crystal core ore and variants
- [ ] Design crystal tools with special abilities
- [ ] Create crystal armor with set bonuses
- [ ] Configure world generation for resources
- [ ] Balance resource distribution
- [ ] `./gradlew runData`
- [ ] `./gradlew check`
- [ ] Document implementation in `progress/`
- [ ] Open PR referencing this task

**Implementation Notes**

- Use Forge's `ITickableBlock` for growth mechanics
- Consider using GeckoLib for animated growth
- Implement custom durability and efficiency for tools
- Add special abilities to armor (e.g., damage reflection, jump boost)
- Ensure compatibility with existing progression systems
