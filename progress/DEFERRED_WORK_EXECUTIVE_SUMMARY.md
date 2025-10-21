# Deferred Work: Executive Summary

**Date**: 2025-10-18  
**Project**: Cosmic Horizons Expanded v0.5.0 → v1.0.0  
**Status**: Roadmap Complete, Ready for Execution

---

## Overview

During the v0.5.0 QA audit, **~140 subtasks** were identified as deferred due to complexity, time constraints, or architectural dependencies. These tasks represent the remaining **13%** of the project needed to reach v1.0.0.

**Total Estimated Effort**: **95-140 hours** over **10 weeks**

---

## What's Been Done (v0.5.0)

✅ **Core Systems** (100% complete)

- Planet registry, discovery, and travel graph
- Player progression (rocket tiers, suit tiers, unlock system)
- Fuel registry and launch validation
- Mineral generation and GTCEu integration
- Hazard system framework
- Boss core progression
- Configuration hot-reload (`/chex reload`)
- Comprehensive command system (`/chex` suite)

✅ **Content** (87% complete)

- **9/9 planets** with dimension JSONs and biomes
- **2/5 bosses** (Spore Tyrant, Worldheart Avatar)
- **5/15 fauna** (Sporefly, Sandworm Hatchling, Stormhawk, and 2 Inferno entities)
- **Core blocks** for all planets (ores, building blocks, terrain)
- **Progression items** (boss cores, fuel types)

✅ **Documentation** (100% complete)

- `docs/PLANET_DESIGNS.md` - Planet encyclopedia
- `docs/CONFIGURATION_EXAMPLES.md` - Config guide
- `docs/BOSS_ENCOUNTERS.md` - Boss strategies
- `docs/MANUAL_TESTING_CHECKLIST.md` - QA checklist
- `CHANGELOG.md` - Version history
- `README.md` - User documentation
- `PROJECT_CONTEXT.md` - Technical overview

---

## What Needs to Be Done

### Phase 1: Client Re-enablement (4-6 hours)

❌ **Status**: In Progress (100 errors currently)

**Objective**: Re-enable excluded client code and resolve compilation errors

**Current Exclusions**:

- ✅ Client code (re-enabled, causing errors)
- ✅ Entity code (re-enabled, causing errors)
- ❌ World systems (excluded, blocking client)
- ❌ Entity registry (excluded, blocking entity rendering)
- ❌ Structure registry (excluded, blocking structures)
- ❌ Biome providers (excluded, blocking biome rendering)

**Blockers**:

- Missing world environment handlers (40% of errors)
- Missing ability registry system (10% of errors)
- Missing library system (15% of errors, intentionally excluded)
- Miscellaneous symbol errors (35% of errors)

**Options**:

1. **Full Re-enablement**: Fix all 100 errors now (4-6 hours), unblock everything
2. **Incremental**: Re-enable only what's needed per feature (spread over 10 weeks)

---

### Phase 2: Visual Systems (20-30 hours)

❌ **Skyboxes & Filters** (5-8 hours)

- Custom skyboxes per dimension
- Fog/tint effects
- Sandstorm overlay (Arrakis)
- Aurora effects (Alpha Centauri A)
- Solar glare effects

❌ **Particle Effects** (6-10 hours)

- Spore particles (Pandora)
- Sand dust (Arrakis)
- Embers (Inferno Prime)
- Frost particles (Crystalis)
- Luminous particles (Aqua Mundus)

❌ **Hazard Visual Feedback** (3-5 hours)

- Screen overlays (frostbite, heat, radiation, low oxygen)
- Vignette effects
- Warning indicators

❌ **Shader Effects** (4-6 hours)

- Underwater distortion (Aqua Mundus)
- Heat wave shader (Inferno Prime)
- Aurora shader (Alpha Centauri A)
- Storm shader (Stormworld)

❌ **Boss Cinematics** (4-6 hours)

- Camera zoom/shake
- Slow-motion intros
- Defeat animations
- Boss health bars

---

### Phase 3: Additional Fauna (20-30 hours)

❌ **Kepler-452b** (5-7 hours) - 3 entities

- River Grazer (peaceful, water-dwelling)
- Meadow Flutterwing (ambient, flying)
- Scrub Stalker (neutral/hostile)

❌ **Aqua Mundus** (8-10 hours) - 4 entities

- Luminfish (peaceful, bioluminescent, schooling)
- Hydrothermal Drone (neutral, vent-dwelling)
- Abyss Leviathan (hostile, deep ocean boss-tier)
- Tidal Jelly (peaceful, drifting)

❌ **Inferno Prime** (7-9 hours) - 3 entities

- Lava Elemental (hostile, lava-swimming)
- Ash Wraith (hostile, flying, wither effect)
- Magma Serpent (hostile, burrowing, projectiles)

---

### Phase 4: Additional Bosses (15-20 hours)

❌ **Verdant Colossus** (Kepler-452b) - 5-7 hours

- 3-phase tree guardian
- Summons treant adds
- Root trap AOE mechanic
- Drops: Verdant Core

❌ **Ocean Sovereign** (Aqua Mundus) - 6-8 hours

- Multi-headed eel boss
- Sonic attacks, whirlpool AOE
- Tsunami attack
- Drops: Sovereign Heart (unlocks Neutron Forge)

❌ **Magma Titan** (Inferno Prime) - 4-6 hours

- Volcanic colossus
- Lava spit, earthquake AOE
- Fire wall barriers
- Drops: Titan Core

---

### Phase 5: Special Mechanics (40-60 hours)

❌ **Aqua Mundus Pressure/Oxygen** (10-15 hours)

- Depth-based pressure damage
- Oxygen depletion underwater
- Suit integration for resistance
- Air pocket system

❌ **Inferno Prime Heat Resistance** (10-15 hours)

- Zone-based heat levels
- Heat damage system
- Cooling items (ice blocks, water)
- Heat-resistant gear
- Shaded area detection

❌ **Alpha Centauri Solar Flares** (10-15 hours)

- Random flare events (10-15 min intervals)
- 30-second warning countdown
- Massive radiation burst
- Shelter detection for safety

❌ **Stormworld Dynamic Weather** (10-15 hours)

- Layered weather by Y-level
- Lightning strike system
- Wind force mechanics
- Layered gravity (reduced at altitude)
- Eye biome calm zone

---

### Phase 6: Testing & Polish (10-15 hours)

❌ **Entity Testing** (2-3 hours)

- Spawn testing
- AI behavior validation
- Loot drop verification

❌ **Boss Testing** (3-4 hours)

- Multi-phase mechanics testing
- Balance adjustments
- Core drop verification

❌ **Visual Systems Testing** (2-3 hours)

- Skybox/particle testing across all dimensions
- Shader performance validation

❌ **Special Mechanics Testing** (2-3 hours)

- Pressure, heat, flare, weather system testing

❌ **Performance Optimization** (1-2 hours)

- TPS profiling
- Entity AI optimization
- Rendering optimization

---

## Release Milestones

### v0.6.0 (After Sprints 1-2)

- ✅ Client code stable
- ✅ Visual systems functional
- ✅ 5+ new fauna entities
- ✅ 1+ new boss

**Estimated**: 2-3 weeks

---

### v0.7.0 (After Sprints 3-7)

- ✅ All 10 fauna entities
- ✅ All 3 new bosses
- ✅ Boss progression integrated

**Estimated**: 6-7 weeks

---

### v1.0.0 (After Sprints 8-10)

- ✅ All special mechanics
- ✅ All visual polish
- ✅ Comprehensive testing
- ✅ Performance optimized
- ✅ Ready for public release

**Estimated**: 10 weeks

---

## Risk Assessment

### 🔴 High Risk

- Client re-enablement (deep compilation issues possible)
- AzureLib entity models (new animation system)
- Shader implementation (OpenGL expertise required)
- Multi-phase boss AI (complex state management)

### 🟡 Medium Risk

- Particle effects (performance impact if not optimized)
- Special mechanics (complex tick systems, potential lag)
- Weather system (computationally expensive)

### 🟢 Low Risk

- Skybox rendering (well-documented API)
- Hazard overlays (simple GUI rendering)
- Basic fauna (template exists)

---

## Recommendation

### Option A: Full Steam Ahead

Complete all 95-140 hours systematically over 10 weeks. Requires dedicated development time and tolerance for debugging sessions.

**Best for**: Users who want v1.0.0 and can commit to regular development sprints

---

### Option B: High-Impact Features First

Prioritize visual polish (skyboxes, particles) and 1-2 bosses. Skip complex special mechanics for now.

**Reduced scope**: 40-50 hours (4-5 weeks)  
**Delivers**: v0.6.0 with significant polish upgrade

**Best for**: Users who want visible improvements without full 10-week commitment

---

### Option C: Incremental Releases

Release v0.5.1, v0.5.2, etc. with small feature additions (1 fauna per release, 1 visual system per release).

**Delivers**: Steady stream of updates without large time blocks

**Best for**: Users who want to maintain momentum but have limited time per session

---

## Current Status

**Phase 1 Status**: IN PROGRESS

- Client code re-enabled (causing 100 errors)
- Entity code re-enabled (causing errors)
- Roadmap documentation complete
- Awaiting user decision on approach

**Next Steps**:

1. **User selects approach** (A, B, or C)
2. **Begin systematic implementation**
3. **Report progress after each sprint**

---

**End of Executive Summary**
