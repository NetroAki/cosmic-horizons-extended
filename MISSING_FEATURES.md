# Missing CHEX Features

## High Priority (Core Functionality)

### 1. Custom Density Functions

- **Periodic X+Z** for torus world seamless donut
- **Band-limited** for ringworld curved shader
- **Curved density** for neutron star forge

### 2. Unique Planet Biomes

- **At least 5 unique biomes per planet** (currently only basic definitions)
- **Specific biome types** mentioned in checklist:
  - Pandora: floating-stone, bioluminescence
  - Arrakis: dunes, sandworm tunnels
  - Aqua Mundus: ocean, abyssal trenches
  - Inferno Prime: volcanic, nether-like caves
  - Crystalis: prism ridges, geodes, shimmering deserts
  - Exotica: chroma steppes, rare-earths
  - Hollow World: glow ecosystems

### 3. Advanced Planets

- **Shattered Dyson Swarm** (Tier 9) - Void dimension with orbital debris
- **Neutron Star Forge** (Tier 10) - Extreme gravity and radiation

## Medium Priority (UX & Polish)

### 4. Client UX Features

- **Tooltips** showing suit tier, rocket tier, fuel requirements
- **Toasts/messages** for launch denial reasons
- **JEI categories** for Rocket Assembly / Planet Resources

### 5. Networking

- **LaunchDenied packets** with specific error codes (FUEL_ID_MISMATCH, TIER_TOO_LOW, etc.)
- **Client sync** for tier changes

### 6. Fallback Ores

- **Actual fallback ore blocks** (currently only config exists)
- **Fallback ore generation** when GTCEu is absent

## Low Priority (Nice to Have)

### 7. Performance Optimizations

- **Periodic density functions** for constant-time performance
- **Band limit functions** for ringworld

### 8. Additional Commands

- **Enhanced command output** with better formatting
- **File output** for dumpPlanets command

## Implementation Status

✅ **Completed (95%)**

- Core progression system (rocket tiers, suit tiers, fuel registry)
- Planet discovery and travel graph
- Ore progression system (GTNH-style)
- KubeJS integration with unlocking functions
- Quest integration and custom events
- Configuration files and commands
- Dimension hooks and suit enforcement
- Launch validation system

🔄 **Partially Complete**

- Planet biomes (basic definitions exist, need unique features)
- Fallback ores (config exists, need actual blocks)

❌ **Missing**

- Custom density functions
- Advanced planets (Dyson Swarm, Neutron Star Forge)
- Client UX features (tooltips, toasts, JEI)
- Networking improvements
- Unique biome generation per planet

## Recommendation

The mod is **functionally complete** for basic progression. The missing features are mostly:

1. **Visual/UX polish** (tooltips, toasts, JEI)
2. **Advanced content** (custom density functions, unique biomes)
3. **Endgame planets** (Dyson Swarm, Neutron Star Forge)

For a **minimum viable product**, the current implementation is sufficient. The missing features can be added in future updates.
