# Task 03: Duplicate Method Definitions

**Priority**: MEDIUM  
**Estimated Time**: 20 minutes  
**Files Affected**: 1 file

---

## Problem

`RingworldBiomeProvider.java` has duplicate method definitions causing compilation errors.

## Files with Duplicates

### RingworldBiomeProvider.java

**Location**: `forge/src/main/java/com/netroaki/chex/world/ringworld/RingworldBiomeProvider.java`

**Duplicate Methods**:

1. `addDebugInfo(List<String>, BlockPos, Sampler)` - defined twice
2. `climateSampler()` - defined twice

**Error Lines**:

- Line 404: `method addDebugInfo(List<String>,BlockPos,Sampler) is already defined in class RingworldBiomeProvider`
- Line 419: `method climateSampler() is already defined in class RingworldBiomeProvider`

## Solution Steps

1. **Examine the File**

   ```bash
   grep -n "addDebugInfo\|climateSampler" forge/src/main/java/com/netroaki/chex/world/ringworld/RingworldBiomeProvider.java
   ```

2. **Identify Duplicate Definitions**

   - Find both method definitions
   - Compare their implementations
   - Determine which one to keep

3. **Remove Duplicates**

   - Delete the duplicate method definitions
   - Keep the more complete/correct implementation
   - Ensure method signatures match exactly

4. **Verify Method Logic**
   - Ensure remaining methods have correct logic
   - Check for any missing imports or dependencies

## Expected Fix

Remove one of each duplicate method definition. The file likely has:

```java
// First definition (keep this one)
public void addDebugInfo(List<String> list, BlockPos pos, Sampler sampler) {
    // implementation
}

// Second definition (remove this one)
public void addDebugInfo(List<String> list, BlockPos pos, Sampler sampler) {
    // duplicate implementation
}
```

## Verification

After fixes, run:

```bash
./gradlew compileJava --no-daemon --console=plain 2>&1 | grep -c "error:"
```

Should reduce error count by ~2 errors.

---

**Next Task**: [04_incomplete_world_systems.md](04_incomplete_world_systems.md)
