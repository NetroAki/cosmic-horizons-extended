# GTCEu Integration Guide

## Current Status

The Cosmic Horizons Extended (CHEX) mod is designed to work both with and without GregTech CEu (GTCEu). Currently, GTCEu is set to `compileOnly` to avoid mixin conflicts during development.

## Integration Modes

### Mode 1: Development Mode (Current)

- **GTCEu Status**: `compileOnly`
- **Benefits**:
  - No mixin conflicts
  - Fast development and testing
  - All CHEX features work with fallback systems
- **Limitations**:
  - GTCEu ores won't generate (uses fallback ores instead)
  - No direct GTCEu integration

### Mode 2: Full Integration Mode

- **GTCEu Status**: `runtimeOnly`
- **Benefits**:
  - Full GTCEu ore generation
  - Complete mineral integration
  - Access to GTCEu materials and recipes
- **Limitations**:
  - May have mixin conflicts with certain Forge versions
  - Requires compatible GTCEu version

## How to Switch Modes

### To Enable Full GTCEu Integration:

1. Open `build.gradle`
2. Find these lines:

   ```gradle
   // GregTech CEu Modern – compileOnly for development, can be switched to runtimeOnly for testing
   compileOnly fg.deobf("com.gregtechceu.gtceu:gtceu-${mc_version}:${gtm_version}")

   // Uncomment the line below and comment the line above to test with GTCEu at runtime
   // runtimeOnly fg.deobf("com.gregtechceu.gtceu:gtceu-${mc_version}:${gtm_version}")
   ```

3. Change them to:

   ```gradle
   // GregTech CEu Modern – runtimeOnly for full integration
   // compileOnly fg.deobf("com.gregtechceu.gtceu:gtceu-${mc_version}:${gtm_version}")

   // Uncomment the line below and comment the line above to test with GTCEu at runtime
   runtimeOnly fg.deobf("com.gregtechceu.gtceu:gtceu-${mc_version}:${gtm_version}")
   ```

4. Run `./gradlew.bat clean build runClient`

### To Return to Development Mode:

1. Open `build.gradle`
2. Change the lines back to:

   ```gradle
   // GregTech CEu Modern – compileOnly for development, can be switched to runtimeOnly for testing
   compileOnly fg.deobf("com.gregtechceu.gtceu:gtceu-${mc_version}:${gtm_version}")

   // Uncomment the line below and comment the line above to test with GTCEu at runtime
   // runtimeOnly fg.deobf("com.gregtechceu.gtceu:gtceu-${mc_version}:${gtm_version}")
   ```

3. Run `./gradlew.bat clean build runClient`

## Troubleshooting

### If you get mixin errors with GTCEu:

- **CONFIRMED ISSUE**: GTCEu 7.1.4 is NOT compatible with Forge 47.4.1
- **Error**: `GuiGraphicsMixin` mixin fails because method `m_280405_` doesn't exist
- **Solution**: Wait for GTCEu to release a version compatible with Forge 47.4.1+
- **Alternative**: Use a different Forge version that's compatible with GTCEu 7.1.4

### If you get dependency resolution errors:

- Make sure you're using the correct GTCEu version
- Check that all Maven repositories are properly configured
- Try running `./gradlew.bat clean` before building

## Current GTCEu Version

- **Version**: 7.1.4
- **Forge Compatibility**: ❌ **NOT COMPATIBLE** with Forge 47.4.1
- **Maven Repository**: maven.gtceu.com
- **Status**: Mixin conflicts prevent runtime integration
- **Java Versions Tested**: Java 17.0.12, Java 21.0.8 - both fail with same mixin error

## Recommended Solutions

### Option 1: Wait for GTCEu Update (Recommended)

- Monitor GTCEu releases for Forge 47.4.1+ compatibility
- Check GTCEu GitHub issues for compatibility updates
- Use development mode until compatibility is achieved

### Option 2: Use Compatible Forge Version

- Downgrade to Forge version compatible with GTCEu 7.1.4
- Check GTCEu documentation for supported Forge versions
- Update `forge_version` in `build.gradle`

### Option 3: Use Development Mode (Current)

- Keep GTCEu as `compileOnly` for development
- Use fallback ore system for mineral generation
- Switch to runtime when compatibility is achieved

## Fallback System

Even without GTCEu, CHEX provides:

- Complete mineral generation using vanilla blocks
- All rocket tiers and progression systems
- Space suit requirements and validation
- Planet discovery and travel systems
- Launch validation and commands

The fallback system ensures CHEX works perfectly in any modpack, with or without GTCEu.
