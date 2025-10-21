# T-102 Ringworld Entities

**Goal**

- Implement Ringworld fauna + constructs: Glacial Grazers, Polar Drakes, Sand Striders, Dust Phantoms, Float Beasts, Sky Flitters, Shadow Hunters, Fungal Shamblers, Scavengers, Hover Beetles, Security Drones, Alloy Guardians, Light Moths, Solar Drones, Worker Drones, Repair Spiders, Alloy Serpents, Edge Beasts along with mini-boss entities (Ice Warden, Dust Colossus, Meadow Leviathan, Shadow Revenant, Arcology Warlord, Neon Sentinel, Heliolith, Tunnel Overseer, Trench Horror) and travel node integration.

**Scope**

- Entity classes, AI, animations, loot tables, spawn rules for organic + construct mobs across natural/urban zones.
- Mini-boss ability scripts tied to loot core drops aligning with GT unlocks.
- Travel node systems linking maintenance tunnels/arcology hubs per design doc.

**Acceptance**

- Entities spawn only in configured zones with correct behaviour (hover, low-gravity float, shadow stealth, drone patrols).
- Mini-boss encounters drop appropriate cores and integrate with progression; travel nodes operational; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_entities.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
