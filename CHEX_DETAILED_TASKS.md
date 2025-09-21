# CHEX Detailed Task Matrix (2025-09-21)

## 0. Build & Infrastructure

- [x] Ensure Gradle wrappers and module build.gradle files target Java 17 and align Architectury settings across root/common/forge projects.
- [x] Add `.github/workflows/build.yml` running `./gradlew check forge:runData` on push/PR and fail on JSON diff drift.
- [x] Configure Spotless (Java, JSON, Markdown) and wire into `check`; add pre-commit hook invoking `./gradlew spotlessApply`.
- [x] Provide `scripts/setup_dev_env.ps1` bootstrapping Forge MDK cache, copying default configs, and linking documentation.

## 1. Core Runtime & Progression Systems

- [x] Extend `PlanetRegistry.registerDiscoveredPlanets` to merge overrides from `config/chex/chex-planets.json5` (fuel, suit, hazards, descriptions); add unit tests under `common/src/test/java`.
- [ ] Implement `/chex dumpPlanets --reload` calling discovery + registry reload, emitting a structured table and JSON snapshot.
- [ ] Finalize `TravelGraph` loader validating tier ? planet mappings; surface `/chex travelGraph validate` output listing unknown IDs.
- [ ] Implement `PlayerTierCapability` (rocket tier, suit tier, milestone bitset) with sync packets; enforce on dimension entry and launch attempts.
- [ ] Flesh out `FuelRegistry` fallback fluids (kerosene, rp1, lox, lh2, dt_mix, he3_blend, exotic_mix) with buckets, textures, and lang entries.

## 2. Planet Implementation Roadmap

For each planet, deliver: datapack assets, block/item sets, flora, fauna, structures, bosses, audio/visual polish, GTCEu ties.

### 2.1 Pandora (Tier 3 transition)

- [ ] Dimension JSON (`data/.../dimension/pandora.json`) with twilight sky gradient, levitation pockets, and dense atmosphere parameters.
- [ ] Biome JSONs: Bioluminescent Forest, Floating Mountains, Ocean Depths, Volcanic Wasteland, Sky Islands with custom temperatures, fog colors, weather.
- [ ] Blocks: pandorite family (stone/cobbled/bricks/mossy/polished), spore soil, biolume moss, crystal-clad pandorite, lumicoral, volcanic pandorite, ash sand; models, blockstates, loot, recipes.
- [ ] Flora generation: fungal towers with mossy bark + glowing caps, skybark trees with crystal leaves, kelp forests, magma spires, cloudstone islands; implement configured/placed features.
- [ ] Fauna entities: glowbeast, sporeflies (swarm particle mob), sky grazer, cliff hunter; AI, drops (phosphor hides, biolume extract, light membranes).
- [ ] Boss encounters: Spore Tyrant (forest), Cliff Hunter Alpha (floating), Deep-Sea Siren (ocean), Molten Behemoth (volcanic), Sky Sovereign (?) culminating in Worldheart Avatar; design arenas, abilities, loot (cores unlocking GT tiers).
- [ ] Hazards/audio: levitation updrafts, heat aura, spore blindness; ambient particle clouds, biolume hum loop, crystalline wind SFX; twilight skybox.
- [ ] GTCEu integration: ore mapping per biome (bismuthinite, phosphorite, beryllium, nickel, cobalt, tungsten, molybdenum, rare earths); update `chex-minerals.json5` + fallback ores.

### 2.2 Arrakis (Tier 4 desert progression)

- [ ] Dimension JSON with harsh sunlight, sandstorm weather controller, reduced water.
- [ ] Biomes: Great Dunes, Spice Mines (underground), Polar Ice Caps, Sietch Strongholds, Stormlands with dust storm settings and mob spawns.
- [ ] Blocks: arrakite sandstone variants, spice node, crystalline salt, ash stone, dune glass; craft spiral staircase/door blocks for sietches.
- [ ] Features: dune ripple noise, spice geysers, sandworm tunnels (carvers), polar ice formations, storm crystal shards; integrate via biome modifiers.
- [ ] Flora: spice cactus, ice reeds, desert shrubs; add growth logic and harvest drops.
- [ ] Fauna: spice gatherer NPCs, sandworm juvenile, storm hawks; drops tie into GT chemical chain.
- [ ] Boss: Sand Emperor arena triggered during storm, multi-phase burrow mechanics; reward Sand Core unlocking T4 fuels.
- [ ] Environmental systems: heat exhaustion debuff, dust storm visibility reduction, ambient wind audio, red sky palette.

### 2.3 Alpha Centauri A (stellar megastructure)

- [ ] Create star-surface dimension with photosphere platforms, corona streams, magnetosphere belt, sunspot fields, solar arrays biomes.
- [ ] Implement extreme light/heat hazard requiring suit IV; add flare burst events.
- [ ] Structures: floating solar collectors, magnetic flux pylons, coronal loops.
- [ ] Entities: plasma wraiths, flare sprites, solar engineer drones; loot powering photonic tech.
- [ ] Boss: Stellar Avatar encounter with flare phases; drop Stellar Core unlocking photonic GT chain.

### 2.4 Kepler-452b (temperate exoworld)

- [ ] Biomes: Temperate Forest (towering trees with braided roots), Highlands (stone spires), River Valleys (broadleaf canopy), Meadowlands (luminescent grasses), Rocky Scrub (succulents).
- [ ] Trees: multi-layer canopy with hanging moss; add vine and blossom variants.
- [ ] Fauna: river grazers, meadow flutterwings, scrub stalkers; tiered drops for GT organics.
- [ ] Boss: Verdant Colossus arena in ancient grove; reward Verdant Core unlocking bio-metal composites.

### 2.5 Aqua Mundus (Tier 5 ocean world)

- [ ] Water-world dimension with Shallow Seas, Kelp Forests, Abyssal Trenches, Hydrothermal Vents, Ice Shelves.
- [ ] Fluid mechanics: high-pressure zones, thermal gradients, oxygen consumption; integrate with suit oxygen systems.
- [ ] Blocks: vent basalt, manganese nodules, luminous kelp fronds, ice shelf slabs.
- [ ] Entities: luminfish schools, hydrothermal drones, abyss leviathan, tidal jelly; implement underwater AI.
- [ ] Boss: Ocean Sovereign (multi-head eel) with sonic and whirlpool attacks; drop Ocean Core enabling platinum/iridium/palladium chain.
- [ ] Audio/visual: bioluminescent water shimmer, deep ambience, volumetric fog.

### 2.6 Inferno Prime (Tier 6 volcanic)

- [ ] Biomes: Lava Seas, Basalt Flats, Obsidian Isles, Ash Wastes, Magma Caverns.
- [ ] Blocks/features: magma geysers, basalt pillars, obsidian flora, ash dunes; falling ash particles.
- [ ] Fauna: ash crawlers, fire wraiths, magma hopper; drops (cinder chitin, volcanic essence).
- [ ] Boss: Infernal Sovereign multi-phase (magma armor, fire rain); drop Inferno Core unlocking niobium/tantalum/uranium.
- [ ] Environmental: heat aura, lava rain, embers, oppressive red sky.

### 2.7 Crystalis (Tier 9 frozen giant)

- [ ] Biomes: Diamond Fields, Frosted Plains, Cryo Geysers, Ice Cliffs, Pressure Depths.
- [ ] Blocks: crystal lattice blocks, cryo ice, frost glass, pressure ice; integrate reflective shaders.
- [ ] Flora/fauna: crystal spires, frost bloom shrubs, snow striders, cryo drakes.
- [ ] Boss: Cryo Monarch with freeze beam, minions; drop Frozen Heart unlocking cryogenics/superconductors.
- [ ] Hazards: constant freeze damage, slippery surfaces, snow blindness; blue aurora sky.

### 2.8 Stormworld (Tier 10 gas giant)

- [ ] Layers: Upper Atmosphere, Storm Bands, Lightning Fields, Eye of the Storm, Metallic Hydrogen Depths.
- [ ] Mechanics: variable gravity, lightning strikes, pressure crush near depths.
- [ ] Blocks/features: storm cloud blocks, charge collectors, lightning rods, hydrogen pools.
- [ ] Entities: tempest serpents, storm titans, aerial behemoths; implement flight AI.
- [ ] Boss: Stormlord Colossus battle with lightning phases; drop Stormheart unlocking exotic superconductors.

### 2.9 Ringworld Megastructure (Tier 11)

- [ ] Implement strip-based chunk generator with wrap hooks; biomes split into Natural Zones (meadow, jungle, desert) and Urban Zones (maintenance tunnels, habitation hubs).
- [ ] Structures: arc scenery anchors, maintenance shafts, command nodes.
- [ ] Entities: guardian drones, habitat fauna, shadow revenants; integrate travel nodes.
- [ ] Boss: Guardian Prime multi-stage in central hub; drop Prime Core unlocking nanomaterials/robotics.

### 2.10 Exotica (Tier 12 surreal world)

- [ ] Biomes: Chroma Steppes, Resonant Dunes, Quantum Glades, Fractal Forest, Prism Canyons with color-cycling shaders.
- [ ] Blocks/features: chroma grass, resonance crystals, fractal trees, prism stone, quantum flora; unique particle effects.
- [ ] Fauna: prism colossus, dune siren, quantum beast, fractal horror, prism seraph; each dropping photonic/quantum components.
- [ ] Boss: Reality Breaker fight with phase-shifting arena; drop Exotic Heart unlocking quantum/phtonics.

### 2.11 Torus World (Tier 13 gravity tech)

- [ ] Toroidal generator ensuring wrap-around terrain (inner rim forest, outer rim desert, structural spine, radiant fields, null-g hubs).
- [ ] Blocks: torus composite stone, radiant crystal, null-g gel.
- [ ] Entities: forest guardian, desert colossus, spine overseer, luminal titan, exotic horror.
- [ ] Boss: Torus Warden central hub event unlocking gravity tech.

### 2.12 Hollow World (Tier 14 megacavern)

- [ ] Biomes: Bioluminescent Caverns, Void Chasms, Crystal Groves, Stalactite Forest, Subterranean Rivers with vertical layering.
- [ ] Blocks/features: glowstone fungi, void stone, stalactite variants, river biolum flora.
- [ ] Entities: mycelium horror, abyss wyrm, crystal titan, stalactite horror, river leviathan.
- [ ] Boss: Hollow Tyrant in central cavern; reward Hollow Heart unlocking void catalysts/pressure reactors.

### 2.13 Shattered Dyson Swarm (Tier 15 orbital debris)

- [ ] Biomes: Panel Fields, Broken Node Clusters, Scaffold Rings, Shadow Wedges, Relay Lattices.
- [ ] Mechanics: zero-g sections, radiation bursts, solar flare cycles.
- [ ] Blocks/features: solar panel segments, relay nodes, scaffold girders, shadow plating.
- [ ] Entities: solar warden, node horror, scaffold titan, radiant abomination, signal overlord.
- [ ] Boss: Dyson Apex delivering Apex Core for stellar power systems.

### 2.14 Neutron Star Forge (Tier 16-17 endgame)

- [ ] Biomes: Accretion Rim, Magnetar Belts, Forge Platforms, Gravity Wells, Radiation Shelters.
- [ ] Mechanics: crushing gravity pulses, magnetic storms, radiation ticks; require max-tier suit.
- [ ] Blocks/features: neutronium crust, magnetar coils, forge reactor blocks, graviton pillars.
- [ ] Entities: accretion leviathan, magnetar colossus, forge overseer, graviton horror, shelter sentinel.
- [ ] Boss: Forge Star Sovereign with gravity manipulation phases; drop Sovereign Heart unlocking neutronium/exotic reactor tech.

## 3. GTCEu & Mineral Systems

- [ ] Align `chex-minerals.json5` with per-biome ore tables from planet designs; add scripts validating tag/block IDs.
- [ ] Add processing recipes for fallback ore blocks (smelting, maceration, chemical chains) mirroring GTCEu.
- [ ] Implement config reload command to refresh mineral distributions at runtime without server restart.

## 4. Combat, Boss Core Matrix, and Progression

- [ ] Build boss controller managing spawn triggers, multi-phase mechanics, loot cores/hearts, and GT tier unlock mapping.
- [ ] Integrate boss core matrix (mini-boss cores ? material unlocks, main hearts ? tier unlocks) into progression configs and JEI pages.
- [ ] Implement suit hazard system (vacuum, thermal, radiation, corrosive, high pressure) with configurable damage/mitigation per planet.
- [ ] Extend launch validation to consider cargo mass multipliers, fuel quality, and mission destination restrictions.

## 5. Client UX, Audio, and Visuals

- [ ] Add tooltip providers summarizing rocket tier, required fuel, destination hazards, and suit requirements.
- [ ] Implement denial/toast UX (`chex.message.launch.failed.*`, travel success) with localized text and icons.
- [ ] Register JEI categories (Rocket Assembly, Planet Resources) with recipes, progression hints, and planet resource tables.
- [ ] Create sound events (launch countdown, suit alarm, planet ambience per world) and link to dimension ambience controllers.
- [ ] Develop skyboxes/visual filters (sandstorms, aurora, void shimmer, solar glare) toggleable via config.

## 6. Documentation & Tooling

- [ ] Update `PROJECT_CONTEXT.md`, `Checklist`, `TB_STRATEGY.md` after implementing planet milestones.
- [ ] Author sample configs: `docs/planets_example.json5`, `docs/minerals_example.json5`, `docs/travel_graph_example.json5` for pack creators.
- [ ] Refresh README with installation steps, configuration overview, progression ladder, fallback behaviour, QA matrix.
- [ ] Maintain `CHANGELOG.md` capturing discovery automation, fallback ores, and upcoming planet releases.

## 7. QA & Release Preparation

- [ ] Script smoke test launching dedicated server, verifying discovery logs, missing registries, and config parsing.
- [ ] Assemble manual QA checklist covering T1?T17 progression (rocket tiers, suits, fuels, planet hazards, GTCEu presence/absence).
- [ ] Prepare release packaging task bundling configs/docs and mod metadata for Modrinth/CurseForge (screenshots, changelog, license).
