# T-223 Arrakis Fauna Implementation

**Goal**

- Implement diverse fauna for Arrakis with desert, underground, polar, and storm-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Sand Striders: camel-sized quadrupeds with elongated legs and armored hides, water-storing humps
- Sand Burrowers: worm-like mobs that erupt from sand in ambush, segmented armored bodies with ringed teeth
- Miner Beetles: squat armored bugs that chew sandstone, glow faintly around mandibles
- Spice Crawlers: centipede-like predators with glowing orange stripes, swarm players
- Ice Stalkers: 2-block-tall lupine predators with crystalline horns, icy breath attack
- Frost Spinners: spider-like mobs with translucent icy legs, create frost webs that slow players
- Raider NPCs: humanoid mobs wearing desert-worn clothing, wielding scavenged weapons
- Cave Beasts: blind quadrupeds that roam abandoned tunnels, sniffing players
- Storm Drakes: serpentine 5-block-long creatures with glowing yellow eyes and crackling lightning
- Sand Shades: humanoid silhouettes made of blowing sand, appear/disappear in storm gusts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Sand Striders with water storage
- [ ] Create Sand Burrowers with ambush mechanics
- [ ] Add Miner Beetles with sandstone chewing
- [ ] Implement Spice Crawlers with swarm behavior
- [ ] Create Ice Stalkers with breath attacks
- [ ] Add Frost Spinners with web creation
- [ ] Implement Raider NPCs and Cave Beasts
- [ ] Create Storm Drakes and Sand Shades
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
