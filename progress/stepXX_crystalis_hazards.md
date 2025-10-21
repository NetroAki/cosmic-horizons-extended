# Crystalis Hazards Implementation Progress

## Current Status

### Hazard Systems

- [x] **Frostbite Effect**

  - [x] Ambient frostbite debuff in cold biomes
  - [x] Tier-based protection from frostbite
  - [x] Visual indicators for frostbite
  - [x] Biome-based activation
  - [x] Protection checks for armor and effects

- [x] **Cryo Geysers**

  - [x] Geyser block with eruption mechanics
  - [x] Player movement effects and knockback
  - [x] Visual and sound effects
  - [x] Water freezing mechanics
  - [x] Particle effects for active/inactive states
  - [x] Configurable encasing radius and toggle (frosted ice ring on eruption)

- [x] **Low-Traction Ice**
- [x] Slippery ice blocks with reduced player control
- [x] Tier-based traction modifiers (reduced sliding for higher suit tiers)
- [x] Particle effects for sliding

- [x] **Snow Blindness**
- [x] Visual effect in blizzard conditions
- [x] Reduced visibility in snowstorms (overlay + vanilla blindness stacking)
- [ ] Goggle item for protection (future item planned)

### Visual & Audio Effects

- [x] **Auroras**

  - [x] Dynamic skybox rendering
  - [x] Color variations based on biome
  - [x] Smooth transitions

- [x] **Blizzards**

  - [x] Snow particle effects (overlay scroll + vignette)
  - [x] Wind sound effects (ambient hooks TBD)
  - [x] Reduced visibility (overlay + blindness)

- [x] **Frozen Overlays**
  - [x] Ice crystal buildup on screen (frost overlay)
  - [ ] Frost shader effects (optional future polish)
  - [ ] Player breath particles (optional future polish)

## Implementation Plan

1. **Core Hazard Systems**

   - [x] Implement frostbite effect and protection
   - [x] Create cryo geyser mechanics
   - [x] Set up low-traction ice physics
   - [x] Implement snow blindness effect

2. **Visual & Audio**

   - [x] Create aurora skybox shader
   - [x] Implement blizzard particle systems
   - [ ] Add environmental sound effects (basic hooks in place)
   - [x] Design screen overlay effects

3. **Integration**
   - [x] Tie hazards to biomes
   - [x] Implement tier-based protection
   - [x] Add configuration options (CrystalisHazardsConfig)
   - [ ] Test all hazard combinations (awaiting gradle run/tests)

## Technical Details

### Frostbite Effect

- Applies slowness and mining fatigue
- Damage over time without protection
- Visual frost particles around player

### Cryo Geysers

- Random eruptions with warning particles
- Ice block creation on eruption
- Area damage and knockback
- Frosted encasing ring on eruption (configurable)

### Aurora System

- Dynamic color gradients
- Smooth animation cycles
- Biome-based variations

### Blizzard Effects

- Wind-affected snow particles
- Screen distortion
- Ambient howling wind sounds

## Next Steps

1. Validate in-game and balance config defaults
2. Optional polish: breath particles and frost shader
3. Add goggle item providing blindness protection
4. Document configuration options (README/config guide)
