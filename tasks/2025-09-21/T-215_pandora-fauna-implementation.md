# T-215 Pandora Fauna Implementation

**Goal**

- Implement diverse fauna for Pandora with unique behaviors, appearances, and drop mechanics.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Glowbeast: Quadruped ~2 blocks tall, deer-like stance, translucent skin with cyan veins, crystal antlers
- Sporeflies: Tiny swarm entities, glowing insect particles, explode into spore clouds on hit
- Sky Grazer: 3-block-long manta ray that floats, semi-transparent cyan wings with glowing veins
- Cliff Hunter: 2-block-tall predator with crystal armor, crystalline talons glowing at tips
- Luminfish: small glowing fish with light-trailing fins
- Abyss Leviathan: 8-10 block long eel with glowing red eyes, armored head, dark scales
- Ash Crawlers: beetles with obsidian shells and ember eyes, crawl in packs
- Fire Wraiths: smoky flame apparitions with ember cores, float above lava
- Cloud Serpent: 10-block-long dragon-like serpent with translucent wings, crackling electricity
- Sky Fluffballs: spherical floating mobs covered in glowing fiber
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Glowbeast entity
- [ ] Create Sporeflies swarm mechanics
- [ ] Add Sky Grazer floating behavior
- [ ] Implement Cliff Hunter predator
- [ ] Create Luminfish and Abyss Leviathan
- [ ] Add Ash Crawlers and Fire Wraiths
- [ ] Implement Cloud Serpent and Sky Fluffballs
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
