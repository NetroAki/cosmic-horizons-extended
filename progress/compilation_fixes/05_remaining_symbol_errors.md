# Task 05: Remaining Symbol Errors

**Priority**: LOW  
**Estimated Time**: 30 minutes  
**Files Affected**: 10-15 files

---

## Problem

After fixing the major categories, there will be remaining "cannot find symbol" errors that need individual attention.

## Common Remaining Issues

### 1. Missing Imports

**Pattern**: `cannot find symbol: class X`  
**Solution**: Add missing import statements

**Common Missing Imports**:

- `java.util.UUID`
- `java.util.List`
- `net.minecraft.world.entity.ai.attributes.AttributeSupplier`
- `net.minecraft.world.entity.ai.goal.Goal`
- `net.minecraft.world.entity.ai.control.FlyingMoveControl`

### 2. Wrong Package References

**Pattern**: `package X does not exist`  
**Solution**: Fix package names or create missing packages

### 3. Method Signature Mismatches

**Pattern**: `cannot find symbol: method X`  
**Solution**: Update method calls to match current API

### 4. Generic Type Issues

**Pattern**: `cannot find symbol: type parameter X`  
**Solution**: Fix generic type declarations

## Files Likely to Have Remaining Errors

### Entity Files

- `GlowbeastRenderer.java` - Model class references
- `SporeflyRenderer.java` - Model class references
- `SporeTyrantRenderer.java` - AzureLib integration

### World Generation Files

- `SandEmperorArenaStructure.java` - Structure API usage
- `PandoraAmbienceManager.java` - Biome references
- `DesertSurvivalHandler.java` - Event handling

### Registry Files

- `CHEXStructures.java` - Structure type registration

## Solution Steps

1. **Run Compilation**

   ```bash
   ./gradlew compileJava --no-daemon --console=plain 2>&1 | grep "error:" > remaining_errors.txt
   ```

2. **Categorize Remaining Errors**

   - Group by file
   - Identify patterns
   - Prioritize by frequency

3. **Fix Systematically**

   - Start with most common errors
   - Fix one file at a time
   - Test compilation after each fix

4. **Common Fixes**

   **Missing UUID Import**:

   ```java
   import java.util.UUID;
   ```

   **Missing List Import**:

   ```java
   import java.util.List;
   ```

   **Missing AttributeSupplier Import**:

   ```java
   import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
   ```

   **Missing Goal Import**:

   ```java
   import net.minecraft.world.entity.ai.goal.Goal;
   ```

## Expected Outcome

After completing Tasks 01-05:

- Error count should drop from 100 to 0-10 errors
- Remaining errors should be minor import/typo issues
- Build should be close to clean

## Verification

After all fixes, run:

```bash
./gradlew check --no-daemon --console=plain
```

Should result in: `BUILD SUCCESSFUL`

---

**Next Task**: [06_clean_build_verification.md](06_clean_build_verification.md)
