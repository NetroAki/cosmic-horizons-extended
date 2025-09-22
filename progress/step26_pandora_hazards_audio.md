# T-026 Pandora Hazards & Audio Implementation

## Overview
Implemented the various environmental hazards and audio systems for the Pandora dimension to enhance immersion and gameplay challenge. The implementation includes biome-specific hazards, visual effects, and ambient sounds that respond to the player's environment and equipment.

## Hazards Implemented

### 1. Levitation Updrafts
- **Location**: Floating Islands biome
- **Effect**: Players without proper gravity resistance are lifted upward
- **Visuals**: Swirling particle effects with a blue-purple gradient
- **Mechanics**:
  - Strength increases with altitude
  - Can be resisted with Gravity Stabilizer upgrade
  - Creates dynamic movement challenges

### 2. Heat Aura
- **Location**: Volcanic Wastes biome
- **Effect**: Causes periodic fire damage and slowness
- **Visuals**: Heat distortion effect and orange particle embers
- **Mechanics**:
  - Damage scales with exposure time
  - Heat-resistant armor reduces/negates effects
  - Water provides temporary resistance

### 3. Spore Blindness
- **Location**: Bioluminescent Forests
- **Effect**: Applies blindness and nausea in spore-dense areas
- **Visuals**: Green spore particles and screen overlay
- **Mechanics**:
  - Worsens over time without protection
  - Gas masks provide complete immunity
  - Lingers briefly after leaving affected areas

## Audio Systems

### Ambient Soundscapes
1. **Bioluminescent Forest**
   - Soft, ethereal hum from bioluminescent plants
   - Occasional creature calls and rustling leaves
   - Dynamic reverb in dense vegetation

2. **Floating Islands**
   - Wind howling between landmasses
   - Distant rock grinding sounds
   - Subtle crystal resonance

3. **Volcanic Wastes**
   - Deep, rumbling earth sounds
   - Occasional lava bubble pops
   - Distant rock slides

### Sound Categories
- Added new sound categories for better volume control:
  - Pandora Ambience
  - Pandora Hazards
  - Pandora Weather

## Technical Implementation

### Hazard System
- Created `PandoraHazardManager` to handle hazard detection and effects
- Implemented configurable hazard parameters in `CHEXConfig`
- Added client-side visual effects using custom particle systems

### Audio System
- Registered custom sound events in `CHEXSoundEvents`
- Implemented biome-specific sound controllers
- Added distance-based volume attenuation

### Configuration
- Added toggles for all hazards in the config
- Individual volume controls for each sound category
- Performance optimization options

## Testing
- [ ] Verify all hazards trigger in correct biomes
- [ ] Test hazard resistance from equipment
- [ ] Check audio mixing and transitions
- [ ] Verify config options work as intended

## Next Steps
1. Add more variations to ambient sounds
2. Implement weather-specific audio effects
3. Add visual indicators for hazard boundaries
4. Create tutorial hints for new players
