# Task 02: Missing Base Classes

**Priority**: HIGH  
**Estimated Time**: 45 minutes  
**Files Affected**: 6 files

---

## Problem

Several files reference base classes that don't exist or are in wrong packages.

## Missing Classes

### 1. AbstractProjectile

**Files**:

- `forge/src/main/java/com/netroaki/chex/entity/projectile/SporeCloudProjectile.java:27`

**Error**: `cannot find symbol: class AbstractProjectile`

**Solution**:

- Check if `AbstractProjectile` exists in common package
- If not, create it or change inheritance to `Projectile` or `Entity`

### 2. StormEntity

**Files**:

- `forge/src/main/java/com/netroaki/chex/entity/stormworld/WindriderEntity.java:28`

**Error**: `cannot find symbol: class StormEntity`

**Solution**:

- Check if `StormEntity` exists in `entity/stormworld/` package
- If not, create it or change inheritance to `PathfinderMob` or `Animal`

### 3. ParticleProvider

**Files**:

- `forge/src/main/java/com/netroaki/chex/client/render/SpiceBlowRenderer.java:303`

**Error**: `cannot find symbol: class ParticleProvider`

**Solution**:

- Import: `import net.minecraft.client.particle.ParticleProvider;`
- Or use: `import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;`

### 4. LightTexture

**Files**:

- `forge/src/main/java/com/netroaki/chex/client/renderer/dimension/LibrarySkyRenderer.java:263`

**Error**: `cannot find symbol: class LightTexture`

**Solution**:

- Import: `import net.minecraft.client.renderer.LightTexture;`

### 5. GeoLayerRenderer

**Files**:

- `forge/src/main/java/com/netroaki/chex/client/renderer/entity/SporeTyrantRenderer.java:75`

**Error**: `cannot find symbol: class GeoLayerRenderer`

**Solution**:

- Import: `import mod.azure.azurelib.renderer.GeoLayerRenderer;`
- Ensure AzureLib dependency is properly configured

### 6. StructureType.StructureFactory

**Files**:

- `forge/src/main/java/com/netroaki/chex/registry/structure/CHEXStructures.java:35`

**Error**: `cannot find symbol: class StructureType.StructureFactory`

**Solution**:

- Import: `import net.minecraft.world.level.levelgen.structure.StructureType;`
- Use: `StructureType.StructureFactory<T>`

## Solution Steps

1. **Check Existing Classes**

   ```bash
   find . -name "AbstractProjectile.java" -type f
   find . -name "StormEntity.java" -type f
   ```

2. **Create Missing Base Classes**

   - Create `AbstractProjectile` extending `Projectile`
   - Create `StormEntity` extending `PathfinderMob`

3. **Fix Import Statements**

   - Add missing imports for standard Minecraft classes
   - Verify AzureLib imports are correct

4. **Test Compilation**
   - Fix one class at a time
   - Verify each fix reduces error count

## Files to Create

### AbstractProjectile.java

```java
package com.netroaki.chex.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public abstract class AbstractProjectile extends Projectile {
    protected AbstractProjectile(EntityType<? extends AbstractProjectile> type, Level level) {
        super(type, level);
    }
}
```

### StormEntity.java

```java
package com.netroaki.chex.entity.stormworld;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class StormEntity extends PathfinderMob {
    protected StormEntity(EntityType<? extends StormEntity> type, Level level) {
        super(type, level);
    }
}
```

## Verification

After fixes, run:

```bash
./gradlew compileJava --no-daemon --console=plain 2>&1 | grep -c "error:"
```

Should reduce error count by ~6-8 errors.

---

**Next Task**: [03_duplicate_methods.md](03_duplicate_methods.md)
