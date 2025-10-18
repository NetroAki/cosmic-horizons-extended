# Task 08: Models and Animations Tracking

**Priority**: LOW  
**Estimated Time**: 15 minutes (setup) + ongoing work  
**Files Affected**: All entities and animated blocks

---

## Problem

The mod needs custom models and animations for entities, but these require specialized 3D modeling work that should be tracked separately from compilation fixes.

## Objective

Create a comprehensive tracking file for all models and animations that need to be created, organized by priority and complexity.

## Models and Animations Needed

### Entity Models (High Priority)

#### Pandora Entities

- **Sporefly** - Flying insect with glowing particles

  - **Model**: Small flying creature with translucent wings
  - **Animation**: Wing flapping, hovering, landing
  - **Textures**: Bioluminescent body, glowing wings
  - **Complexity**: Medium

- **Spore Tyrant** - Large boss entity
  - **Model**: Massive fungal creature with multiple segments
  - **Animation**: Walking, attacking, spore cloud emission
  - **Textures**: Fungal growth patterns, glowing spores
  - **Complexity**: High

#### Arrakis Entities

- **Sandworm Hatchling** - Burrowing creature
  - **Model**: Segmented worm body
  - **Animation**: Burrowing, surface movement, attacking
  - **Textures**: Sandy scales, segmented body
  - **Complexity**: Medium

#### Inferno Prime Entities

- **Ash Crawler** - Small hostile creature

  - **Model**: Quadruped with ash-covered body
  - **Animation**: Walking, jumping, ash particle emission
  - **Textures**: Charred body, glowing ember eyes
  - **Complexity**: Low

- **Fire Wraith** - Flying hostile entity

  - **Model**: Ethereal floating figure
  - **Animation**: Floating, fire trail, attack swoops
  - **Textures**: Semi-transparent fire body
  - **Complexity**: Medium

- **Magma Hopper** - Jumping creature

  - **Model**: Bipedal hopper with magma body
  - **Animation**: Hopping, magma dripping, attacking
  - **Textures**: Molten rock body, glowing cracks
  - **Complexity**: Medium

- **Infernal Sovereign** - Boss entity
  - **Model**: Large demonic figure with wings
  - **Animation**: Flying, ground attacks, fire breath
  - **Textures**: Infernal armor, glowing runes
  - **Complexity**: High

#### Aqua Mundus Entities

- **Ocean Sovereign** - Boss entity
  - **Model**: Multi-headed sea serpent
  - **Animation**: Swimming, head attacks, water spouts
  - **Textures**: Scaly body, bioluminescent patterns
  - **Complexity**: High

#### Stormworld Entities

- **Windrider** - Flying creature
  - **Model**: Large bird with storm clouds
  - **Animation**: Flying, lightning attacks, storm generation
  - **Textures**: Stormy feathers, electrical effects
  - **Complexity**: High

### Block Models (Medium Priority)

#### Animated Blocks

- **Luminous Kelp** - Animated plant

  - **Model**: Waving kelp fronds
  - **Animation**: Gentle swaying motion
  - **Textures**: Glowing kelp with bioluminescence
  - **Complexity**: Low

- **Spore Blossom** - Animated flower
  - **Model**: Large flowering plant
  - **Animation**: Petal opening/closing, spore emission
  - **Textures**: Colorful petals, glowing center
  - **Complexity**: Medium

#### Special Blocks

- **Hydrothermal Vent** - Animated block
  - **Model**: Vent with steam/heat effects
  - **Animation**: Steam emission, heat shimmer
  - **Textures**: Basalt with glowing cracks
  - **Complexity**: Medium

### Particle Effects (Low Priority)

#### Environmental Particles

- **Spore Particles** - Pandora atmosphere
- **Sand Dust** - Arrakis sandstorms
- **Ember Particles** - Inferno Prime heat
- **Frost Particles** - Crystalis cold
- **Luminous Particles** - Aqua Mundus bioluminescence

## Model Creation Workflow

### 1. Planning Phase

- [ ] Define model specifications
- [ ] Create concept art/references
- [ ] Plan animation sequences
- [ ] Determine texture requirements

### 2. Modeling Phase

- [ ] Create base mesh in Blender
- [ ] UV unwrap for texturing
- [ ] Create LOD (Level of Detail) versions
- [ ] Export to Minecraft-compatible format

### 3. Animation Phase

- [ ] Create rigging for animated models
- [ ] Animate key sequences (idle, walk, attack, etc.)
- [ ] Export animation data
- [ ] Test in-game

### 4. Texturing Phase

- [ ] Create base textures
- [ ] Add normal maps for detail
- [ ] Create emissive maps for glowing parts
- [ ] Optimize for Minecraft's texture system

### 5. Integration Phase

- [ ] Create model JSON files
- [ ] Set up animation controllers
- [ ] Test in-game performance
- [ ] Optimize for different hardware

## Tools and Software Needed

### 3D Modeling

- **Blender** (Free) - Primary modeling software
- **Blockbench** (Free) - Minecraft-specific modeling
- **Maya/3ds Max** (Paid) - Professional alternatives

### Texturing

- **GIMP** (Free) - Image editing
- **Photoshop** (Paid) - Professional texturing
- **Substance Painter** (Paid) - Advanced texturing

### Animation

- **Blender** - Animation and rigging
- **Maya** - Professional animation
- **Mixamo** (Free) - Pre-made animations

## File Structure for Models

```
forge/src/main/resources/assets/cosmic_horizons_extended/
├── models/
│   ├── entity/
│   │   ├── sporefly.geo.json
│   │   ├── spore_tyrant.geo.json
│   │   ├── ash_crawler.geo.json
│   │   └── ...
│   └── block/
│       ├── luminous_kelp.geo.json
│       ├── spore_blossom.geo.json
│       └── ...
├── animations/
│   ├── entity/
│   │   ├── sporefly.animation.json
│   │   ├── spore_tyrant.animation.json
│   │   └── ...
│   └── block/
│       ├── luminous_kelp.animation.json
│       └── ...
└── textures/
    ├── entity/
    │   ├── sporefly.png
    │   ├── spore_tyrant.png
    │   └── ...
    └── block/
        ├── luminous_kelp.png
        └── ...
```

## Priority Order for Creation

### Phase 1: Essential Entities (Week 1-2)

1. **Sporefly** - Core Pandora fauna
2. **Ash Crawler** - Basic Inferno entity
3. **Sandworm Hatchling** - Arrakis signature creature

### Phase 2: Boss Entities (Week 3-4)

1. **Spore Tyrant** - Pandora boss
2. **Infernal Sovereign** - Inferno boss
3. **Ocean Sovereign** - Aqua Mundus boss

### Phase 3: Advanced Entities (Week 5-6)

1. **Fire Wraith** - Inferno flying entity
2. **Magma Hopper** - Inferno jumping entity
3. **Windrider** - Stormworld entity

### Phase 4: Animated Blocks (Week 7-8)

1. **Luminous Kelp** - Aqua Mundus flora
2. **Spore Blossom** - Pandora flora
3. **Hydrothermal Vent** - Aqua Mundus feature

### Phase 5: Particle Effects (Week 9-10)

1. **Spore Particles** - Pandora atmosphere
2. **Sand Dust** - Arrakis sandstorms
3. **Ember Particles** - Inferno heat effects

## Estimated Time Investment

| Category         | Models | Time per Model | Total Time       |
| ---------------- | ------ | -------------- | ---------------- |
| Simple Entities  | 3      | 4-6 hours      | 12-18 hours      |
| Complex Entities | 4      | 8-12 hours     | 32-48 hours      |
| Boss Entities    | 3      | 12-16 hours    | 36-48 hours      |
| Animated Blocks  | 3      | 2-4 hours      | 6-12 hours       |
| **Total**        | **13** | -              | **86-126 hours** |

## Notes for Modeler

### Technical Requirements

- All models must be optimized for Minecraft's rendering system
- Use quads, not triangles where possible
- Keep polygon count reasonable (under 1000 for entities)
- Use proper UV mapping for texturing
- Create multiple LOD versions for performance

### Style Guidelines

- Maintain consistency with Minecraft's blocky aesthetic
- Use appropriate color palettes for each planet
- Add subtle details without overwhelming the simple style
- Ensure models work well at different scales

### Performance Considerations

- Optimize for mobile/low-end hardware
- Use efficient animation techniques
- Minimize texture memory usage
- Test performance impact in-game

---

**This file serves as a comprehensive guide for creating all models and animations needed for the mod. Work can begin on any entity, but following the priority order ensures the most important content is created first.**
