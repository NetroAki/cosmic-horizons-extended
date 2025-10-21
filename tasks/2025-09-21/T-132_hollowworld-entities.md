# T-132 Hollow World Entities

**Goal**

- Implement Hollow World entities: Spore Beasts, Lume Bats, Void Phantoms, Chasm Serpents, Shardlings, Prism Stalkers, Root Spiders, Hollow Stalkers, River Serpents, Lume Fish plus mini-bosses (Mycelium Horror, Abyss Wyrm, Crystal Titan, Stalactite Horror, River Leviathan).

**Scope**

- Entity classes, AI, spawn rules, loot tables delivering GT biochemical/void/crystal materials (spore glands, bat oil, void essence, chasm scales, crystal dust, prism pelts, root silk, stalker fangs, serpent oil).
- Mini-boss ability scripting (spore clouds, void shockwaves, light refraction, stalactite collapse, river whirlpools) with arena triggers.
- Support for inverted gravity pathing and void edge avoidance.

**Acceptance**

- Entities spawn and behave per biome requirements (flying in caverns, void-lurking serpents, river apex predators) with drop tables matching GT unlocks.
- `./gradlew check` passes; log outstanding FX/animation tasks.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollowworld_entities.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
