# Deferred Work Implementation Roadmap

**Created**: 2025-10-17  
**Status**: Implementation Planning  
**Estimated Total Effort**: 95-140 hours

---

## Executive Summary

This document outlines the systematic approach to completing all deferred work identified during the v0.5.0 QA audit. The work is organized into **6 major phases** with dependencies clearly marked.

**Current State**: v0.5.0 - Core systems complete, 87% overall completion  
**Target State**: v1.0.0 - All polish systems, fauna, bosses, and special mechanics implemented

---

## Phase 1: Client Code Re-enablement (4-6 hours)

### Objective

Re-enable excluded client-side code and fix all compilation errors.

### Current Exclusions (forge/build.gradle lines 86-103)

```gradle
exclude 'com/netroaki/chex/client/**'
exclude 'com/netroaki/chex/CHEXClient.java'
exclude 'com/netroaki/chex/entity/**'
exclude 'com/netroaki/chex/world/**'
exclude 'com/netroaki/chex/registry/entity/**'
exclude 'com/netroaki/chex/registry/structure/**'
exclude 'com/netroaki/chex/registry/biome/**'
```

### Tasks

1. **Remove client exclusions** (lines 86-87)
   - Remove `exclude 'com/netroaki/chex/client/**'`
   - Remove `exclude 'com/netroaki/chex/CHEXClient.java'`
2. **Fix CHEXClient.java compilation errors**

   - Resolve missing imports
   - Fix deprecated API calls
   - Update event registrations

3. **Validate client loading**
   - Run `./gradlew :forge:runClient`
   - Verify no crashes
   - Test basic UI rendering

### Success Criteria

- ✅ Client code compiles without errors
- ✅ Game client launches successfully
- ✅ No texture/model errors in logs

### Dependencies

- None (foundation for all other client work)

---

## Phase 2: Visual Systems Implementation (20-30 hours)

### 2.1: Skybox/Visual Filters (T-412) - 5-8 hours

**Objective**: Implement custom skyboxes and visual filters per dimension

**Requirements**:

- Sandstorm effects (Arrakis)
- Aurora effects (Alpha Centauri A)
- Void shimmer (Stormworld)
- Solar glare (Alpha Centauri A)
- Config toggles via `chex-visual-filters.json5`

**Implementation**:

1. Create `DimensionSkyRenderer` system
   - Hook into Forge's sky rendering events
   - Per-dimension sky color/texture overrides
2. Create `VisualFilterManager`
   - Load filters from `chex-visual-filters.json5`
   - Apply fog color, density, sky tint per dimension
3. Implement special effects
   - Sandstorm particle overlay (Arrakis)
   - Aurora borealis shader (Alpha Centauri A)
   - Void shimmer post-processing (Stormworld)
   - Solar glare lens flare (Alpha Centauri A)

**Files to Create**:

- `forge/src/main/java/com/netroaki/chex/client/renderer/DimensionSkyRenderer.java`
- `forge/src/main/java/com/netroaki/chex/client/visuals/VisualFilterManager.java`
- `forge/src/main/java/com/netroaki/chex/client/visuals/SandstormEffect.java`
- `forge/src/main/java/com/netroaki/chex/client/visuals/AuroraEffect.java`

---

### 2.2: Particle Effects System (T-413) - 6-10 hours

**Objective**: Implement planet-specific particle effects

**Requirements**:

- Atmospheric particles (dust, spores, embers, etc.)
- Environmental effects (waterfalls, lava drips, etc.)
- Visual feedback for mechanics

**Implementation**:

1. Register custom particle types

   - `SPORE_PARTICLE` (Pandora)
   - `SAND_DUST_PARTICLE` (Arrakis)
   - `EMBER_PARTICLE` (Inferno Prime)
   - `FROST_PARTICLE` (Crystalis)
   - `LUMINOUS_PARTICLE` (Aqua Mundus)

2. Create particle factories
   - `SporeParticle` with drift/glow
   - `SandDustParticle` with wind effects
   - `EmberParticle` with rise/fade
3. Hook into biome ambient particles
   - Register particles in biome JSONs
   - Configure spawn rates and behaviors

**Files to Create**:

- `forge/src/main/java/com/netroaki/chex/client/particles/CHEXParticles.java` (registry)
- `forge/src/main/java/com/netroaki/chex/client/particles/SporeParticle.java`
- `forge/src/main/java/com/netroaki/chex/client/particles/SandDustParticle.java`
- `forge/src/main/java/com/netroaki/chex/client/particles/EmberParticle.java`

---

### 2.3: Hazard Visual Feedback (T-415) - 3-5 hours

**Objective**: Add screen overlays for environmental hazards

**Requirements**:

- Vignette effects for low oxygen
- Screen frost for frostbite
- Heat distortion for extreme heat
- Radiation static for radiation

**Implementation**:

1. Create `HazardOverlayRenderer`

   - Hook into `RenderGameOverlayEvent`
   - Check player hazard status
   - Render appropriate overlay

2. Create overlay textures
   - `textures/gui/overlay/frostbite.png`
   - `textures/gui/overlay/heat_distortion.png`
   - `textures/gui/overlay/radiation_static.png`
   - `textures/gui/overlay/low_oxygen.png`

**Files to Create**:

- `forge/src/main/java/com/netroaki/chex/client/renderer/HazardOverlayRenderer.java`
- Texture files (4 PNG overlays)

---

### 2.4: Shader Effects (T-414) - 4-6 hours

**Objective**: Custom post-processing shaders for planet effects

**Requirements**:

- Underwater distortion shader (Aqua Mundus)
- Heat wave shader (Inferno Prime)
- Aurora shader (Alpha Centauri A)
- Storm shader (Stormworld)

**Implementation**:

1. Create shader files (.json + .fsh + .vsh)
   - `assets/cosmic_horizons_extended/shaders/post/underwater_distortion.json`
   - `assets/cosmic_horizons_extended/shaders/program/heat_wave.fsh`
2. Create `ShaderManager`
   - Load shaders per dimension
   - Apply post-processing effects

**Files to Create**:

- Shader definition JSONs (4 files)
- Fragment shaders (4 .fsh files)
- Vertex shaders (4 .vsh files)
- `forge/src/main/java/com/netroaki/chex/client/shader/ShaderManager.java`

---

### 2.5: Cinematic Boss Effects (T-416) - 4-6 hours

**Objective**: Boss intro/defeat cinematics

**Requirements**:

- Camera zoom/shake effects
- Slow-motion intro sequences
- Defeat animation triggers
- Boss health bar rendering

**Implementation**:

1. Create `BossCinematicController`

   - Hook into boss spawn events
   - Control camera movement
   - Apply screen effects

2. Create boss-specific cinematics
   - Spore Tyrant: Spore burst intro
   - Worldheart Avatar: Ground shake + light pulse
   - Future bosses: Custom sequences

**Files to Create**:

- `forge/src/main/java/com/netroaki/chex/client/cinematic/BossCinematicController.java`
- `forge/src/main/java/com/netroaki/chex/client/cinematic/CameraController.java`

---

## Phase 3: Fauna Implementation (20-30 hours)

### 3.1: Kepler-452b Fauna - 5-7 hours

**Entities**:

1. **River Grazer** (peaceful, water-dwelling)
   - Spawns in River Valleys biome
   - Grazes on kelp/seagrass
   - Drops: Meat, Hide
2. **Meadow Flutterwing** (ambient, flying)
   - Spawns in Meadowlands biome
   - Passive flying behavior
   - Drops: Feather, Essence
3. **Scrub Stalker** (neutral/hostile)
   - Spawns in Rocky Scrub biome
   - Hunts at night
   - Drops: Claws, Meat

**Per-Entity Implementation** (~2 hours each):

1. Create entity class extending `Animal`/`FlyingMob`/`PathfinderMob`
2. Implement AI goals (pathfinding, behaviors)
3. Register entity type and attributes
4. Create spawn rules JSON
5. Create loot table JSON
6. Create entity model (using AzureLib)
7. Create entity renderer
8. Add localization

**Files per Entity** (9 files x 3 = 27 files):

- Entity class: `common/src/main/java/com/netroaki/chex/entity/kepler/*.java`
- Model class: `forge/src/main/java/com/netroaki/chex/client/model/*.java`
- Renderer: `forge/src/main/java/com/netroaki/chex/client/renderer/entity/*.java`
- Spawn rules: `data/cosmic_horizons_extended/forge/biome_modifier/kepler_*_spawn.json`
- Loot table: `data/cosmic_horizons_extended/loot_tables/entities/*.json`
- Model JSON: `assets/cosmic_horizons_extended/geo/entity/*.geo.json`
- Texture: `assets/cosmic_horizons_extended/textures/entity/*.png`
- Animation: `assets/cosmic_horizons_extended/animations/entity/*.animation.json`
- Localization entry in `en_us.json`

---

### 3.2: Aqua Mundus Fauna - 8-10 hours

**Entities**:

1. **Luminfish** (peaceful, bioluminescent)

   - Spawns in all ocean biomes
   - Emits light particles
   - Schooling behavior
   - Drops: Glowing Scale, Raw Fish

2. **Hydrothermal Drone** (neutral, vent-dwelling)

   - Spawns near Hydrothermal Vents biome
   - Territorial behavior
   - Heat resistance
   - Drops: Thermal Core, Metal Scrap

3. **Abyss Leviathan** (hostile, deep ocean)

   - Spawns in Abyssal Trenches biome
   - Large size, aggressive
   - Multi-segment model
   - Drops: Leviathan Scale, Abyss Pearl

4. **Tidal Jelly** (peaceful, drifting)
   - Spawns in Shallow Seas biome
   - Slow drift movement
   - Transparent model with glow
   - Drops: Jelly Membrane, Biolume Essence

**Implementation**: Same structure as 3.1, 36 files total (9 per entity x 4)

---

### 3.3: Inferno Prime Fauna - 7-9 hours

**Entities**:

1. **Lava Elemental** (hostile, lava-dwelling)

   - Spawns in Lava Seas/Magma Caverns biomes
   - Swims in lava
   - Fire immunity
   - Drops: Molten Core, Obsidian Shard

2. **Ash Wraith** (hostile, flying)

   - Spawns in Ash Wastes biome
   - Flying mob with ash trail particles
   - Applies wither effect
   - Drops: Ash Essence, Charred Bone

3. **Magma Serpent** (hostile, underground)
   - Spawns in Magma Caverns biome
   - Burrows through stone
   - Spits lava projectiles
   - Drops: Serpent Fang, Magma Heart

**Implementation**: Same structure, 27 files total (9 per entity x 3)

---

## Phase 4: Additional Bosses (15-20 hours)

### 4.1: Verdant Colossus (Kepler-452b) - 5-7 hours

**Concept**: Massive tree-like guardian

**Phases**:

1. **Phase 1** (100-66% HP): Vine whips, summons treants
2. **Phase 2** (66-33% HP): Root trap AOE, healing zones
3. **Phase 3** (33-0% HP): Enrage, permanent Strength III, berserker mode

**Mechanics**:

- Vine Whip: 20 damage, 3-block reach
- Summon Treants: Spawns 4 treant adds (100 HP each)
- Root Trap: 8-block AOE, roots players for 5 seconds
- Healing Zones: Regenerates 5 HP/sec in zones
- Enrage: +50% damage, +30% speed

**Loot**: Verdant Core (progression item)

**Implementation**:

1. Entity class with multi-phase AI
2. Boss model (large, animated)
3. Treant add entity
4. Arena structure JSON
5. Loot table with Verdant Core
6. Boss encounter mechanics
7. Cinematic intro/defeat

**Files**: ~15 files

---

### 4.2: Ocean Sovereign (Aqua Mundus) - 6-8 hours

**Concept**: Multi-headed eel controlling ocean currents

**Phases**:

1. **Phase 1** (100-66% HP): Sonic attacks, jellyfish adds
2. **Phase 2** (66-33% HP): Whirlpool AOE, water spouts
3. **Phase 3** (33-0% HP): Tsunami attack, pressure damage

**Mechanics**:

- Sonic Blast: 25 damage, 10-block range, Nausea II
- Summon Jellyfish: Spawns 6 hostile jellyfish
- Whirlpool: Pulls players toward boss, 5 damage/sec
- Water Spout: Launches players upward
- Tsunami: 50 damage wave attack (telegraphed)

**Loot**: Sovereign Heart (required for Neutron Forge access)

**Implementation**: Similar to 4.1, ~15 files

---

### 4.3: Magma Titan (Inferno Prime) - 4-6 hours

**Concept**: Volcanic colossus made of molten rock

**Phases**:

1. **Phase 1** (100-50% HP): Lava spit, magma cube summons
2. **Phase 2** (50-0% HP): Earthquake AOE, fire walls

**Mechanics**:

- Lava Spit: Projectile, 30 damage, sets fire
- Summon Magma Cubes: Spawns 8 large cubes
- Earthquake: 12-block radius, falling blocks, 20 damage
- Fire Walls: Creates barriers of fire

**Loot**: Titan Core (progression item)

**Implementation**: Similar to 4.1, ~12 files

---

## Phase 5: Special Mechanics (40-60 hours)

### 5.1: Aqua Mundus Pressure/Oxygen System - 10-15 hours

**Objective**: Depth-based pressure and oxygen mechanics

**Mechanics**:

- **Depth Tracking**: Monitor player Y-level in Aqua Mundus
- **Pressure Damage**: Below Y=0, 1 damage/sec per 10 blocks deeper
- **Oxygen Depletion**: Oxygen bar depletes underwater (60 seconds default)
- **Suit Integration**: Suit T2+ provides pressure resistance
- **Air Pockets**: Bubbles/structures provide oxygen refill

**Implementation**:

1. Create `PressureManager` (tick-based system)
2. Create `OxygenCapability` (stores oxygen level)
3. Create oxygen HUD overlay
4. Hook into damage events for pressure
5. Hook into suit checks for resistance

**Files**: ~8 files

---

### 5.2: Inferno Prime Heat Resistance System - 10-15 hours

**Objective**: Extreme heat mechanics beyond suit requirements

**Mechanics**:

- **Heat Levels**: Surface (50°C), Lava Seas (200°C), Magma Caverns (400°C)
- **Heat Damage**: Gradual damage based on heat level
- **Cooling Items**: Ice blocks, water buckets provide temporary cooling
- **Heat-Resistant Gear**: Special armor/potions reduce heat damage
- **Shaded Areas**: Caves/structures provide heat reduction

**Implementation**:

1. Create `HeatManager` (zone-based heat tracking)
2. Create heat resistance attribute
3. Create cooling item system
4. Create heat HUD indicator
5. Hook into damage events

**Files**: ~8 files

---

### 5.3: Alpha Centauri A Solar Flare Events - 10-15 hours

**Objective**: Random solar flare events with telegraphed warnings

**Mechanics**:

- **Flare Warning**: 30-second countdown, screen flash
- **Flare Impact**: Massive AOE radiation burst
- **Safe Zones**: Shelters/structures block radiation
- **Radiation Effects**: Wither III (30s), Mining Fatigue II (60s)
- **Flare Frequency**: Every 10-15 minutes

**Implementation**:

1. Create `SolarFlareManager` (event scheduler)
2. Create warning system (countdown, visuals)
3. Create radiation burst effect
4. Create shelter detection system
5. Create flare visual effects (screen flash, particles)

**Files**: ~10 files

---

### 5.4: Stormworld Dynamic Weather System - 10-15 hours

**Objective**: Layered weather system with lightning, wind, and gravity

**Mechanics**:

- **Layer-Based Weather**: Different weather at different Y-levels
- **Lightning Strikes**: Random strikes, 30 damage, sets fire
- **Wind Forces**: Pushes players/entities horizontally
- **Layered Gravity**: Reduced gravity at higher altitudes
- **Eye Calm**: Eye biome has no weather effects

**Implementation**:

1. Create `StormworldWeatherManager` (tick-based)
2. Create lightning strike system
3. Create wind force system (applies velocity)
4. Create layered gravity system
5. Create storm visual/audio effects

**Files**: ~12 files

---

## Phase 6: Testing & Polish (10-15 hours)

### 6.1: Entity Testing

- Spawn all entities in creative mode
- Test AI behaviors
- Verify loot drops
- Test spawn rules in survival

### 6.2: Boss Testing

- Fight each boss multiple times
- Test all phases and mechanics
- Verify loot core drops
- Balance health/damage

### 6.3: Visual Systems Testing

- Visit all dimensions with visual filters
- Test particle effects in all biomes
- Verify shader effects work correctly
- Test hazard overlays

### 6.4: Special Mechanics Testing

- Test pressure system at various depths
- Test heat system in all Inferno biomes
- Trigger solar flares multiple times
- Test storm weather at all layers

### 6.5: Performance Testing

- Profile entity AI performance
- Profile rendering performance
- Check TPS impact of special mechanics
- Optimize hotspots

---

## Implementation Schedule

### Sprint 1: Foundation (Week 1)

- Phase 1: Client code re-enablement
- Phase 2.1-2.2: Skyboxes and particles

### Sprint 2: Visual Polish (Week 2)

- Phase 2.3-2.5: Hazard feedback, shaders, cinematics

### Sprint 3: Kepler Fauna (Week 3)

- Phase 3.1: All Kepler-452b entities

### Sprint 4: Aqua Fauna (Week 4)

- Phase 3.2: All Aqua Mundus entities

### Sprint 5: Inferno Fauna (Week 5)

- Phase 3.3: All Inferno Prime entities

### Sprint 6: Bosses Part 1 (Week 6)

- Phase 4.1: Verdant Colossus

### Sprint 7: Bosses Part 2 (Week 7)

- Phase 4.2-4.3: Ocean Sovereign + Magma Titan

### Sprint 8-9: Special Mechanics (Weeks 8-9)

- Phase 5.1-5.4: All special mechanics

### Sprint 10: Testing & Release (Week 10)

- Phase 6: Comprehensive testing and polish

---

## Success Criteria

### v0.6.0 Release Criteria

- ✅ Client code enabled and stable
- ✅ Visual systems functional (skyboxes, particles, shaders)
- ✅ At least 5 new fauna entities implemented
- ✅ At least 1 new boss implemented

### v0.7.0 Release Criteria

- ✅ All 10 fauna entities implemented
- ✅ All 3 new bosses implemented
- ✅ Boss loot cores integrated into progression

### v1.0.0 Release Criteria

- ✅ All special mechanics implemented
- ✅ All visual systems polished
- ✅ Comprehensive testing complete
- ✅ Performance optimized
- ✅ Ready for public release

---

## Risk Assessment

### High Risk

- **Client code re-enablement**: May reveal deep compilation issues
- **AzureLib entity models**: Requires learning new animation system
- **Shader implementation**: Complex OpenGL knowledge required
- **Multi-phase boss AI**: Complex state management

### Medium Risk

- **Particle effects**: Performance impact if not optimized
- **Special mechanics**: Complex tick-based systems, potential lag
- **Weather system**: Computationally expensive

### Low Risk

- **Skybox rendering**: Well-documented Forge API
- **Hazard overlays**: Simple GUI rendering
- **Basic fauna**: Template exists (Sporefly)

---

## Conclusion

This roadmap represents **95-140 hours** of focused development work across **10 weeks**. Each sprint builds on previous work, with clear success criteria and risk mitigation.

**Recommendation**: Execute sprints sequentially, validating each phase before proceeding. Prioritize high-impact, low-risk work first (visual systems, basic fauna) before tackling complex systems (bosses, special mechanics).

**Current Status**: Roadmap complete, ready for Sprint 1 execution.

---

**End of Roadmap**
