# Task 04: Incomplete World Systems

**Priority**: MEDIUM  
**Estimated Time**: 60 minutes  
**Files Affected**: 15+ files

---

## Problem

Several world generation systems are incomplete and causing compilation errors. These represent advanced features that may not be fully implemented.

## Incomplete Systems

### 1. Eden System (5 files)

**Files**:

- `forge/src/main/java/com/netroaki/chex/world/eden/EdenDimension.java:46`
- `forge/src/main/java/com/netroaki/chex/world/eden/entity/EdenCreature.java:75`
- `forge/src/main/java/com/netroaki/chex/world/eden/EdenGardenAccess.java:92`
- `forge/src/main/java/com/netroaki/chex/world/eden/EdenGardenAccess.java:108`
- `forge/src/main/java/com/netroaki/chex/world/eden/entity/LumiflyEntity.java:129`
- `forge/src/main/java/com/netroaki/chex/world/eden/features/flora/CelestialBloomBlock.java:52`

**Issues**: Missing base classes, incomplete implementations

### 2. Ringworld System (10+ files)

**Files**:

- `forge/src/main/java/com/netroaki/chex/world/ringworld/RingworldBiomeProvider.java` (multiple errors)
- `forge/src/main/java/com/netroaki/chex/world/ringworld/RingworldChunkGenerator.java:64`

**Issues**: Complex biome generation, missing dependencies

### 3. Hollow World System (1 file)

**Files**:

- `forge/src/main/java/com/netroaki/chex/world/hollow/HollowWorldDimension.java:25`

**Issues**: Missing dimension setup

### 4. Hazard Systems (2 files)

**Files**:

- `forge/src/main/java/com/netroaki/chex/world/hazard/PandoraHazards.java:4`
- `forge/src/main/java/com/netroaki/chex/world/hazards/PandoraHazardManager.java:4`

**Issues**: Missing hazard framework classes

## Solution Options

### Option A: Exclude Incomplete Systems (Recommended)

**Time**: 15 minutes  
**Approach**: Add exclusions to `forge/build.gradle`

```gradle
// Exclude incomplete world systems
exclude 'com/netroaki/chex/world/eden/**'
exclude 'com/netroaki/chex/world/ringworld/**'
exclude 'com/netroaki/chex/world/hollow/**'
exclude 'com/netroaki/chex/world/hazard/**'
exclude 'com/netroaki/chex/world/hazards/**'
```

**Pros**:

- Quick fix (15 minutes)
- Gets to clean build fast
- Can implement these systems later

**Cons**:

- Loses advanced world generation features
- May need to re-implement later

### Option B: Stub Out Missing Classes

**Time**: 60 minutes  
**Approach**: Create minimal implementations

**Files to Create**:

- `EdenBaseEntity.java` (base class for Eden creatures)
- `HollowWorldBase.java` (base dimension class)
- `HazardManager.java` (hazard system framework)

**Pros**:

- Preserves all systems
- Allows incremental implementation

**Cons**:

- More time consuming
- May create more stub classes

### Option C: Fix Each System Individually

**Time**: 2-3 hours  
**Approach**: Complete each system properly

**Pros**:

- Full implementation
- No technical debt

**Cons**:

- Very time consuming
- May not be needed for v1.0.0

## Recommended Solution: Option A

Add these exclusions to `forge/build.gradle`:

```gradle
// WORLD/REGISTRY SYSTEMS RE-ENABLED - Phase 1 of deferred work
exclude 'com/netroaki/chex/world/library/**' // Keep library system excluded (quest system)
// Exclude incomplete/stub entities and their renderers
exclude 'com/netroaki/chex/client/EntityRenderers.java' // References unimplemented SandEmperorRenderer
exclude 'com/netroaki/chex/client/render/entity/SpiceGathererRenderer.java' // Missing entity
exclude 'com/netroaki/chex/client/render/entity/StormHawkRenderer.java' // Missing entity
exclude 'com/netroaki/chex/client/render/SpiceBlowRenderer.java' // Incomplete particle system
exclude 'com/netroaki/chex/client/screen/LibraryBookScreen.java' // Library system excluded
exclude 'com/netroaki/chex/client/renderer/dimension/LibrarySkyRenderer.java' // Library dimension excluded
// Exclude incomplete world systems
exclude 'com/netroaki/chex/world/eden/**'
exclude 'com/netroaki/chex/world/ringworld/**'
exclude 'com/netroaki/chex/world/hollow/**'
exclude 'com/netroaki/chex/world/hazard/**'
exclude 'com/netroaki/chex/world/hazards/**'
```

## Verification

After exclusions, run:

```bash
./gradlew compileJava --no-daemon --console=plain 2>&1 | grep -c "error:"
```

Should reduce error count by ~15-20 errors.

---

**Next Task**: [05_remaining_symbol_errors.md](05_remaining_symbol_errors.md)
