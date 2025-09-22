# Fuel Registry Fallbacks Implementation (T-009)

## Overview
This document tracks the implementation of fuel registry fallbacks for the Cosmic Horizons Expanded mod. The goal is to provide fallback fluids when GTCEu is not installed, ensuring the mod remains functional in both GTCEu and non-GTCEu environments.

## Implementation Details

### 1. Fluid Definitions
- [x] Created fluid definitions for all fallback fuels in `CHEXFluids.java`
  - Kerosene (T1)
  - RP-1 (T2)
  - LOX (T3)
  - LH2 (T4)
  - DT Mix (T5)
  - He3 Blend (T6)
  - Exotic Mix (T7+)

### 2. Textures and Assets
- [x] Created placeholder textures for all fluid types
  - Used ImageMagick to generate colored textures for each fuel type
  - Created both still and flowing variants
  - Generated bucket content textures
- [x] Added JSON models for fluid blocks and bucket items
- [x] Added language entries for all fuel names and bucket items

### 3. Fuel Registry Updates
- [x] Enhanced `FuelRegistry` to support fallback fluids
  - Modified `registerFuel` to accept primary and fallback fluid IDs
  - Added automatic fallback to CHEX fluids when GTCEu fluids are not available
  - Improved logging for better debugging

### 4. Integration
- [x] Updated fluid registration to work with Forge's fluid system
- [x] Ensured proper fluid properties (density, viscosity, temperature, etc.)
- [x] Set up proper light levels for exotic fuels (DT Mix, He3 Blend, Exotic Mix)

## Pending Tasks
- [ ] Create data generation for fluid tags
- [ ] Add tests for fallback fluid registration and usage
- [ ] Test in-game with and without GTCEu installed
- [ ] Document the fuel system for modpack developers

## Technical Notes
- All CHEX fluids are registered with the `chex` namespace
- Fallback fluids are automatically used when GTCEu is not present
- Fuel volumes and properties match GTCEu equivalents where applicable
- Textures use distinct colors for easy identification in-game

## Testing Instructions
1. Install the mod without GTCEu and verify:
   - All fuel types are available in creative menu
   - Buckets can be filled and emptied
   - Fluids flow and render correctly
   - Rocket modules accept the correct fuel types

2. Install with GTCEu and verify:
   - GTCEu fluids are used by default
   - CHEX fluids are not registered (to avoid duplication)
   - All fuel functionality works as expected
