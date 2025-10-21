# T-222 Arrakis Flora Generation

**Goal**

- Implement unique flora generation for Arrakis with desert, underground, and storm-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and environmental effects

**Acceptance**

- Spice Cactus: hardy cactus with glowing red pods (GT chemistry spice extract source)
- Dune Grass: grows in ridges, waving in hot winds
- Spice Caps: mushroom-like growths feeding off ore veins, orange-brown caps with dust particles
- Crystalline Ice Flowers: bloom once per day-night cycle, scatter snowflake-like particles
- Underground Vines: crawl across walls, glowing faintly green
- Storm Crystals: grow after lightning strikes, glowing yellow shards that hum faintly
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Spice Cactus with glowing pods
- [ ] Create Dune Grass with wind animation
- [ ] Add Spice Caps with dust particles
- [ ] Implement Crystalline Ice Flowers with bloom cycle
- [ ] Create Underground Vines with wall crawling
- [ ] Add Storm Crystals with lightning growth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
