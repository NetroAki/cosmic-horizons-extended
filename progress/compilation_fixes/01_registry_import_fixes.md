# Task 01: Registry Import Fixes

**Priority**: HIGH  
**Estimated Time**: 30 minutes  
**Files Affected**: 8 files

---

## Problem

Multiple files are importing from incorrect registry paths. There are duplicate registry files causing import confusion.

## Root Cause

- `CHEXEntities` exists in both `registry/entities/` and `registry/entity/`
- `CHEXItems` exists in both `registry/items/` and `registry/`
- Files are importing from non-existent paths like `com.netroaki.chex.registry.CHEXEntities`

## Files to Fix

### 1. Entity Registry Imports (4 files)

**Files**:

- `forge/src/main/java/com/netroaki/chex/entity/boss/SporeTyrantEntity.java:3`
- `forge/src/main/java/com/netroaki/chex/entity/boss/SporeTyrant.java:3`
- `forge/src/main/java/com/netroaki/chex/entity/projectile/SporeCloudProjectile.java:3`
- `forge/src/main/java/com/netroaki/chex/entity/stormworld/WindriderEntity.java:28`

**Current Import**: `import com.netroaki.chex.registry.CHEXEntities;`  
**Fix To**: `import com.netroaki.chex.registry.entities.CHEXEntities;`

### 2. Biome Registry Imports (4 files)

**Files**:

- `forge/src/main/java/com/netroaki/chex/world/biome/BioluminescentForestBiome.java:3`
- `forge/src/main/java/com/netroaki/chex/world/biome/FloatingIslandsBiome.java:3`
- `forge/src/main/java/com/netroaki/chex/world/biome/variants/CrystalwoodGroveBiome.java:3`
- `forge/src/main/java/com/netroaki/chex/world/biome/variants/LuminousCanopyForestBiome.java:3`
- `forge/src/main/java/com/netroaki/chex/world/biome/variants/SporehazeThicketBiome.java:3`

**Current Import**: `import com.netroaki.chex.registry.CHEXBiomes;`  
**Fix To**: `import com.netroaki.chex.registry.biome.CHEXBiomes;` (if exists) or create it

## Solution Steps

1. **Audit Registry Files**

   ```bash
   find . -name "*CHEXEntities*" -type f
   find . -name "*CHEXBiomes*" -type f
   find . -name "*CHEXItems*" -type f
   ```

2. **Consolidate Duplicate Registries**

   - Decide which registry file to keep (likely the one in `registry/entities/`)
   - Remove or rename the duplicate
   - Update all imports to use the canonical path

3. **Fix Import Statements**

   - Update all files to import from correct registry paths
   - Test compilation after each batch

4. **Create Missing Registries**
   - If `CHEXBiomes` doesn't exist, create it
   - Register all biome types properly

## Verification

After fixes, run:

```bash
./gradlew compileJava --no-daemon --console=plain 2>&1 | grep -c "error:"
```

Should reduce error count by ~8-12 errors.

---

**Next Task**: [02_missing_base_classes.md](02_missing_base_classes.md)
