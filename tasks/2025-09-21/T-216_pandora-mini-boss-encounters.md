# T-216 Pandora Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Pandora with unique mechanics and arena designs.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Spore Tyrant (Forest): ~6 blocks tall fungal treant, mossy bark with glowing eyes, root tendrils
- Cliff Hunter Alpha (Floating): 3-block-tall, crystal-plated armor, shockwave roar, jumps between islands
- Deep-Sea Siren (Ocean): Humanoid aquatic predator, translucent skin with cyan tattoos, kelp-hair
- Molten Behemoth (Volcanic): Gigantic quadruped with obsidian armor plates, molten skull head
- Storm Roc (Sky): Colossal bird (6-8 block wingspan) with cloud-vapor wings sparking lightning
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Spore Tyrant boss entity
- [ ] Create Cliff Hunter Alpha with shockwave mechanics
- [ ] Implement Deep-Sea Siren aquatic boss
- [ ] Add Molten Behemoth volcanic boss
- [ ] Create Storm Roc aerial boss
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
