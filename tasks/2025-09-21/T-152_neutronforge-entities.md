# T-152 Neutron Star Forge Entities

**Goal**

- Implement Neutron Star Forge entities: Plasma Eels, Accretion Beasts, Magnetar Serpents, Storm Constructs, Forge Drones, Smelter Horrors, Gravity Horrors, Void Leeches, Shelter Guardians, Radiant Wraiths plus mini-bosses (Accretion Leviathan, Magnetar Colossus, Forge Overseer, Graviton Horror, Shelter Sentinel).

**Scope**

- Entity classes, AI, animations, loot tables with GT drops (plasma organs, accreted cores, magnetar scales, storm fragments, alloy chips, molten organs, gravity organs, leech extracts, shield circuits, radiant essence).
- Mini-boss ability scripting (plasma waves, magnetic disarm, drone summons, gravitational collapses, radiation shields) with arena triggers.
- High-gravity movement handling, radiation immunity, and equipment disruption logic.

**Acceptance**

- Entities spawn/behave per biome with gravity/radiation effects and drop tables aligned to GT unlocks.
- `./gradlew check` passes; document outstanding FX/animation items.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutronforge_entities.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
