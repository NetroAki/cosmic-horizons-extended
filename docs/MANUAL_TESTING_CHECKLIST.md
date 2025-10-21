# Manual Testing Checklist - Cosmic Horizons Extended

**Version**: 1.0  
**Last Updated**: 2025-10-17

## Overview

Comprehensive manual testing checklist for validating CHEX features before release. Test on both dedicated servers and singleplayer.

---

## Pre-Testing Setup

### Environment Preparation

- [ ] **Java Version**: Verify Java 17 installed (`java -version`)
- [ ] **Dependencies**: Confirm GTCEu and Cosmic Horizons installed
- [ ] **Config Files**: Verify all JSON5 configs present and valid
- [ ] **Build**: Run `./gradlew build --no-daemon` (confirm BUILD SUCCESSFUL)
- [ ] **JSON Validation**: Run `python scripts/validate_json.py` (confirm all files valid)

### Test World Creation

- [ ] Create new test world (seed: `test_chex_123`)
- [ ] Enable cheats (for command testing)
- [ ] Set gamemode creative (for initial setup)
- [ ] Set time to day (`/time set day`)

---

## Core Systems Testing

### 1. Mod Initialization

**Objective**: Verify mod loads without crashes.

- [ ] **Server Start**: Launch dedicated server (`./gradlew :forge:runServer`)
  - [ ] Server starts without errors
  - [ ] CHEX initialization logs appear
  - [ ] No missing registry errors
  - [ ] Planet discovery logs present
- [ ] **Client Start**: Launch game client (`./gradlew :forge:runClient`)
  - [ ] Client loads without crashes
  - [ ] Mod appears in mods list
  - [ ] No texture/model errors in log

**Expected Output**:

```
[CHEX] Planet Registry initialized with X planets
[CHEX] Travel Graph loaded with Y routes
[CHEX] Fuel Registry initialized with Z fuel types
```

**Pass Criteria**: ✅ No crashes, all initialization logs present

---

### 2. Planet Discovery System

**Objective**: Verify planets are discovered and registered.

- [ ] **Auto-Discovery**: Check for Cosmic Horizons planets
  - [ ] Run `/chex dumpPlanets`
  - [ ] Verify `run/chex_planets_dump.json` created
  - [ ] Confirm CH planets present (Pandora, Mars, Venus, etc.)
  - [ ] Confirm CHEX planets present (Kepler-452b, Aqua Mundus, etc.)
- [ ] **Planet Properties**: Verify each planet has correct properties
  - [ ] Nodule tier set correctly
  - [ ] Suit tier tag present (if required)
  - [ ] Display name readable
  - [ ] Dimension ID valid

**Test Commands**:

```bash
/chex dumpPlanets
/chex travel 5  # List planets accessible at rocket tier 5
/chex travel 10 # List planets accessible at rocket tier 10
```

**Pass Criteria**: ✅ All planets discovered, properties correct

---

### 3. Player Capability System

**Objective**: Verify player progression data is stored and synced.

- [ ] **Initialization**: New player capability created on join
  - [ ] Join world as new player
  - [ ] Run `/chex unlock rocket 5 @s`
  - [ ] Verify tier unlocked (check with `/chex travel 5`)
- [ ] **Suit Unlock**: Test suit tier progression
  - [ ] Run `/chex unlock suit 1 @s`
  - [ ] Run `/chex unlock suit 3 @s`
  - [ ] Verify suit tiers unlocked
- [ ] **Planet Unlock**: Test planet discovery
  - [ ] Run `/chex unlock planet cosmic_horizons:pandora @s`
  - [ ] Verify planet unlocked
- [ ] **Persistence**: Test data saves correctly
  - [ ] Unlock several tiers/planets
  - [ ] Exit and re-enter world
  - [ ] Verify data persists
- [ ] **Sync**: Test client-server sync
  - [ ] Test on dedicated server
  - [ ] Unlock tier on server
  - [ ] Verify client UI updates (if applicable)

**Test Commands**:

```bash
/chex unlock rocket 5 @s
/chex unlock suit 3 @s
/chex unlock planet cosmic_horizons:pandora @s
```

**Pass Criteria**: ✅ All capabilities save, sync, and persist correctly

---

### 4. Travel and Launch System

**Objective**: Verify players can travel to planets.

- [ ] **Basic Travel**: Test tier-appropriate travel
  - [ ] Unlock rocket tier 5
  - [ ] Run `/chex launch cosmic_horizons:pandora`
  - [ ] Verify teleportation to Pandora
  - [ ] Verify arrival message appears
- [ ] **Tier Restrictions**: Test tier gating
  - [ ] Set rocket tier to 3
  - [ ] Attempt `/chex launch cosmic_horizons:pandora` (requires T5)
  - [ ] Verify denial message appears
- [ ] **Suit Requirements**: Test suit gating
  - [ ] Remove suit tier 1
  - [ ] Attempt travel to Pandora (requires Suit T1)
  - [ ] Verify bounce-back or denial
- [ ] **Fuel Requirements**: Test fuel system (if implemented)
  - [ ] Set fuel to 0
  - [ ] Attempt launch
  - [ ] Verify denial due to insufficient fuel

**Test Commands**:

```bash
/chex unlock rocket 5 @s
/chex unlock suit 1 @s
/chex launch cosmic_horizons:pandora
```

**Pass Criteria**: ✅ All travel mechanics work, gating enforced

---

### 5. Dimension Loading

**Objective**: Verify all custom dimensions load without errors.

- [ ] **Kepler-452b**: Test dimension loading
  - [ ] `/chex launch cosmic_horizons_extended:kepler_452b`
  - [ ] Verify dimension loads (no crash)
  - [ ] Verify biomes generate (check F3 screen)
  - [ ] Check for missing block/texture errors
- [ ] **Aqua Mundus**: Test ocean world
  - [ ] `/chex launch cosmic_horizons_extended:aqua_mundus`
  - [ ] Verify water-covered terrain
  - [ ] Check biome distribution
- [ ] **Inferno Prime**: Test volcanic world
  - [ ] `/chex launch cosmic_horizons_extended:inferno_prime`
  - [ ] Verify ultrawarm environment
  - [ ] Check for lava seas
- [ ] **Alpha Centauri A**: Test star surface
  - [ ] `/chex launch cosmic_horizons_extended:alpha_centauri_a`
  - [ ] Verify high ambient light
  - [ ] Check for ultrawarm effects
- [ ] **Stormworld**: Test extended height
  - [ ] `/chex launch cosmic_horizons_extended:stormworld`
  - [ ] Verify 512-block height
  - [ ] Check layered biomes
- [ ] **Crystalis**: Test crystal world
  - [ ] `/chex launch cosmic_horizons_extended:crystalis`
  - [ ] Verify crystal blocks present
  - [ ] Check for frostbite hazards (if implemented)
- [ ] **Aurelia Ringworld**: Test megastructure
  - [ ] `/chex launch cosmic_horizons_extended:aurelia_ringworld`
  - [ ] Verify ring structure
  - [ ] Check gravity mechanics (if implemented)

**Pass Criteria**: ✅ All dimensions load, no crashes, biomes generate

---

### 6. Block Registration

**Objective**: Verify all custom blocks are registered and functional.

#### Kepler-452b Blocks

- [ ] **Kepler Wood Log**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:kepler_wood_log 64`
  - [ ] Place block, verify rotation (pillar block)
  - [ ] Mine block, verify drop
- [ ] **Kepler Wood Leaves**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:kepler_wood_leaves 64`
  - [ ] Place block, verify transparency
  - [ ] Test decay mechanics (place far from logs)
- [ ] **Kepler Moss**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:kepler_moss 64`
  - [ ] Place block, verify no collision
  - [ ] Test instant-break
- [ ] **Kepler Vines**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:kepler_vines 64`
  - [ ] Place block, verify no collision
  - [ ] Test instant-break
- [ ] **Kepler Blossom**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:kepler_blossom 64`
  - [ ] Place block, verify light emission (light level 3)
  - [ ] Test transparency

#### Aqua Mundus Blocks

- [ ] **Aqua Vent Basalt**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:aqua_vent_basalt 64`
  - [ ] Place block, verify light emission (light level 5)
  - [ ] Mine block, verify drop
- [ ] **Aqua Manganese Nodule**: Give and mine
  - [ ] `/give @s cosmic_horizons_extended:aqua_manganese_nodule 64`
  - [ ] Place block
  - [ ] Mine block, verify XP drop (2-5 XP)
- [ ] **Aqua Luminous Kelp**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:aqua_luminous_kelp 64`
  - [ ] Place block, verify light emission (light level 8)
  - [ ] Test underwater placement
- [ ] **Aqua Ice Shelf Slab**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:aqua_ice_shelf_slab 64`
  - [ ] Place block, verify properties
  - [ ] Test slipperiness

#### Pandora Blocks

- [ ] **Pandorite Stone**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:pandorite_stone 64`
  - [ ] Place block, mine block
- [ ] **Biolume Moss**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:biolume_moss 64`
  - [ ] Verify light emission
- [ ] **Lumicoral**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:lumicoral 64`
  - [ ] Verify light emission
- [ ] **Spore Soil**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:spore_soil 64`
  - [ ] Test block properties

#### Arrakis Blocks

- [ ] **Arrakite Sandstone (all variants)**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:arrakite_sandstone 64`
  - [ ] `/give @s cosmic_horizons_extended:arrakite_smooth_sandstone 64`
  - [ ] `/give @s cosmic_horizons_extended:arrakite_cut_sandstone 64`
  - [ ] `/give @s cosmic_horizons_extended:arrakite_chiseled_sandstone 64`
  - [ ] Place all variants, verify textures
- [ ] **Spice Node**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:spice_node 64`
  - [ ] Verify properties
- [ ] **Arrakis Flora**: Give and place
  - [ ] `/give @s cosmic_horizons_extended:spice_cactus 64`
  - [ ] `/give @s cosmic_horizons_extended:ice_reeds 64`
  - [ ] `/give @s cosmic_horizons_extended:desert_shrub 64`

**Pass Criteria**: ✅ All blocks give, place, mine, and function correctly

---

### 7. Mineral Generation (GTCEu Integration)

**Objective**: Verify GTCEu ore veins generate on planets.

- [ ] **Mineral Config**: Verify `chex-minerals.json5` valid
  - [ ] Run `python scripts/validate_json.py`
  - [ ] Check for JSON5 syntax errors
- [ ] **Ore Generation**: Test ore spawning
  - [ ] Travel to Arrakis
  - [ ] Run `/chex minerals cosmic_horizons_extended:arrakis`
  - [ ] Verify mineral config displays
  - [ ] Use X-ray or spectator mode to find ore veins
  - [ ] Verify ores present at configured Y-levels
- [ ] **Biome-Specific Ores**: Test biome targeting
  - [ ] Locate specific biome (e.g., Spice Mines)
  - [ ] Search for configured ore vein
  - [ ] Verify ore spawns only in target biome
- [ ] **Reload System**: Test hot-reload
  - [ ] Modify `chex-minerals.json5`
  - [ ] Run `/chex minerals reload`
  - [ ] Generate new chunks
  - [ ] Verify changes applied

**Test Commands**:

```bash
/chex minerals cosmic_horizons_extended:arrakis
/chex minerals reload
/tp @s ~ ~ ~1000  # Teleport to ungenerated chunks
```

**Pass Criteria**: ✅ Ores generate at correct rates and locations

---

### 8. Boss Encounters

**Objective**: Verify boss entities spawn and function.

#### Spore Tyrant

- [ ] **Summoning**: Spawn boss
  - [ ] `/summon cosmic_horizons_extended:spore_tyrant ~ ~ ~`
  - [ ] Verify entity spawns without crash
- [ ] **Combat**: Test boss mechanics
  - [ ] Set gamemode survival
  - [ ] Engage boss, test melee attacks
  - [ ] Verify spore cloud attacks (Poison II)
  - [ ] Verify Sporefly summons
  - [ ] Reduce boss to 50% HP, verify phase 2 transition
  - [ ] Defeat boss
- [ ] **Loot**: Verify drops
  - [ ] Check for Pandoran Heart Seed
  - [ ] Check for Biolume Moss drops
  - [ ] Verify XP orbs

#### Worldheart Avatar

- [ ] **Summoning**: Spawn boss
  - [ ] `/summon cosmic_horizons_extended:worldheart_avatar ~ ~ ~`
  - [ ] Verify entity spawns
- [ ] **Combat**: Test multi-phase fight
  - [ ] Engage boss, test phase 1 (100-66% HP)
  - [ ] Verify Guardian add summons
  - [ ] Test phase 2 (66-33% HP)
  - [ ] Verify rooting vines mechanic
  - [ ] Test phase 3 (33-0% HP)
  - [ ] Verify enrage timer (60 seconds)
  - [ ] Defeat boss (or retreat before explosion)
- [ ] **Loot**: Verify drops
  - [ ] Check for Worldheart Fragment
  - [ ] Verify multiple high-tier drops

**Test Commands**:

```bash
/summon cosmic_horizons_extended:spore_tyrant ~ ~ ~
/summon cosmic_horizons_extended:worldheart_avatar ~ ~ ~
/give @s minecraft:netherite_sword{Enchantments:[{id:"sharpness",lvl:10}]} 1
```

**Pass Criteria**: ✅ Bosses spawn, mechanics work, loot drops correctly

---

### 9. Configuration Hot-Reload

**Objective**: Verify config changes apply without restart.

- [ ] **Planet Overrides**: Test planet reload
  - [ ] Modify `chex-planets.json5`
  - [ ] Change nodule tier for a planet
  - [ ] Run `/chex reload`
  - [ ] Run `/chex dumpPlanets`
  - [ ] Verify changes applied
- [ ] **Mineral Reload**: Test mineral reload
  - [ ] Modify `chex-minerals.json5`
  - [ ] Change rarity or Y-level
  - [ ] Run `/chex minerals reload`
  - [ ] Run `/chex minerals <planetId>`
  - [ ] Verify changes applied
- [ ] **Validation**: Test invalid config handling
  - [ ] Introduce JSON5 syntax error
  - [ ] Run `/chex reload`
  - [ ] Verify error message displayed
  - [ ] Fix syntax error
  - [ ] Re-run `/chex reload`
  - [ ] Verify success

**Test Commands**:

```bash
/chex reload
/chex minerals reload
/chex dumpPlanets
```

**Pass Criteria**: ✅ All configs hot-reload correctly, errors handled gracefully

---

### 10. Command Testing

**Objective**: Verify all `/chex` commands work correctly.

- [ ] **Help Command**: `/chex help`
  - [ ] Verify list of subcommands displayed
- [ ] **Dump Planets**: `/chex dumpPlanets`
  - [ ] Verify JSON file created in `run/`
- [ ] **Travel Command**: `/chex travel <tier>`
  - [ ] Verify list of planets displayed
  - [ ] Test multiple tiers (1, 5, 10)
- [ ] **Launch Command**: `/chex launch <planetId>`
  - [ ] Test valid launch
  - [ ] Test invalid planet ID (error message)
  - [ ] Test tier restriction (denial message)
- [ ] **Unlock Commands**: Test all unlock subcommands
  - [ ] `/chex unlock rocket 5 @s`
  - [ ] `/chex unlock suit 3 @s`
  - [ ] `/chex unlock planet cosmic_horizons:pandora @s`
- [ ] **Minerals Command**: `/chex minerals <planetId>`
  - [ ] Verify mineral config displayed
  - [ ] Test invalid planet ID (error message)
- [ ] **Lag Profiler**: Test performance profiler
  - [ ] `/chex lagprofiler start`
  - [ ] Wait 30 seconds
  - [ ] `/chex lagprofiler stop`
  - [ ] Check `run/chex_lag_profile_*.txt` created
  - [ ] Verify profiling data present
- [ ] **Reload Commands**: Test all reload subcommands
  - [ ] `/chex reload`
  - [ ] `/chex minerals reload`

**Pass Criteria**: ✅ All commands execute correctly, proper error handling

---

## Regression Testing

### 1. Existing Features

- [ ] **Pandora**: Verify no regressions
  - [ ] Travel to Pandora
  - [ ] Verify biomes generate
  - [ ] Verify blocks present
  - [ ] Summon Spore Tyrant, verify mechanics
- [ ] **Arrakis**: Verify no regressions
  - [ ] Travel to Arrakis
  - [ ] Verify sandstorm effects (if applicable)
  - [ ] Verify spice resources present
- [ ] **Crystalis**: Verify no regressions
  - [ ] Travel to Crystalis
  - [ ] Verify crystal blocks present
  - [ ] Test frostbite mechanics (if applicable)

### 2. Build System

- [ ] **Gradle Check**: Run `./gradlew check --no-daemon`
  - [ ] Verify BUILD SUCCESSFUL
  - [ ] No Spotless violations
- [ ] **JSON Validation**: Run `python scripts/validate_json.py`
  - [ ] Verify all files valid
- [ ] **Compilation**: Run `./gradlew build --no-daemon`
  - [ ] Verify no compilation errors
  - [ ] Check JAR file created (`forge/build/libs/`)

---

## Performance Testing

### 1. Server Performance

- [ ] **Server Start Time**: Measure startup time
  - [ ] Record server start duration
  - [ ] Compare to baseline (< 2 minutes acceptable)
- [ ] **Chunk Generation**: Test world gen performance
  - [ ] Fly 10,000 blocks in new dimension
  - [ ] Monitor TPS (`/forge tps`)
  - [ ] Verify TPS stays > 15
- [ ] **Lag Profiler**: Profile server performance
  - [ ] `/chex lagprofiler start`
  - [ ] Perform typical activities (mining, combat, travel)
  - [ ] `/chex lagprofiler stop`
  - [ ] Review profiling report
  - [ ] Identify any methods > 50ms

### 2. Client Performance

- [ ] **Frame Rate**: Monitor FPS
  - [ ] Travel to all dimensions
  - [ ] Record average FPS per dimension
  - [ ] Verify FPS > 30 (acceptable), > 60 (good)
- [ ] **Memory Usage**: Monitor RAM usage
  - [ ] Check F3 screen
  - [ ] Verify memory usage < 4GB (typical), < 6GB (acceptable)
- [ ] **Texture Loading**: Check for missing textures
  - [ ] Visit all dimensions
  - [ ] Place all custom blocks
  - [ ] Verify no pink/black checkerboard textures

---

## Integration Testing

### 1. GTCEu Integration

- [ ] **Ore Processing**: Test GTCEu machines
  - [ ] Mine custom ores (manganese nodule, spice node)
  - [ ] Process ores in GTCEu machines
  - [ ] Verify recipes work correctly
- [ ] **Ore Veins**: Verify GTCEu worldgen
  - [ ] Use GTCEu prospector tool
  - [ ] Scan for ore veins on custom planets
  - [ ] Verify veins detected

### 2. Cosmic Horizons Integration

- [ ] **Planet Discovery**: Verify CH planets discovered
  - [ ] Run `/chex dumpPlanets`
  - [ ] Verify CH planets in list (Mars, Venus, etc.)
- [ ] **Travel Compatibility**: Test CH travel system
  - [ ] Use CH travel GUI (if applicable)
  - [ ] Verify CHEX planets appear in CH system

---

## Final Checks

- [ ] **No Console Errors**: Review server/client logs
  - [ ] Check for ERRORs (none acceptable)
  - [ ] Check for WARNs (none critical)
- [ ] **No Missing Registries**: Verify all registrations
  - [ ] Check for "Missing registry" errors (none acceptable)
- [ ] **No Crashes**: Confirm stability
  - [ ] No crashes during testing (0 crashes acceptable)
- [ ] **Documentation Accuracy**: Verify docs match implementation
  - [ ] Review PLANET_DESIGNS.md
  - [ ] Review CONFIGURATION_EXAMPLES.md
  - [ ] Review BOSS_ENCOUNTERS.md

---

## Test Report Template

```markdown
# CHEX Manual Testing Report

**Date**: YYYY-MM-DD  
**Tester**: [Name]  
**Version**: [Mod Version]  
**Environment**: [Singleplayer / Dedicated Server]

## Summary

- **Total Tests**: X
- **Passed**: Y
- **Failed**: Z
- **Overall Status**: [PASS / FAIL]

## Failed Tests (if any)

1. **Test Name**: [Description]
   - **Expected**: [Expected behavior]
   - **Actual**: [Actual behavior]
   - **Steps to Reproduce**: [List steps]
   - **Severity**: [Critical / High / Medium / Low]

## Notes

[Any additional observations or recommendations]

## Verdict

[READY FOR RELEASE / NEEDS FIXES]
```

---

## References

- **Test Scripts**: `scripts/testing/` (if applicable)
- **Bug Reports**: Submit to GitHub Issues
- **Performance Profiles**: `run/chex_lag_profile_*.txt`

---

**End of Document**
