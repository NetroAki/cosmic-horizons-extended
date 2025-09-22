# Pandora Fauna Implementation (T-024)

## Overview
Implemented the Pandora fauna system, including four unique creatures with appropriate spawn rules and behaviors:

1. **Glowbeast**
   - Passive creature found in Bioluminescent Forests
   - Drops phosphor hides used for crafting
   - Glows in the dark and attracts sporeflies

2. **Sporeflies**
   - Small ambient creatures that follow Glowbeasts
   - Found in Bioluminescent Forests
   - Drops biolume extract used for potions

3. **Sky Grazer**
 - Passive flying creature in Floating Islands
 - Drops light membranes for glider crafting
 - Flies between floating islands

4. **Cliff Hunter**
 - Hostile predator in Floating Mountains
 - Drops rare materials for high-tier gear
 - Can climb walls and ambush players

## Implementation Details

### Spawn Rules
- **Glowbeast**: Spawns in Bioluminescent Forest biomes in groups of 2-4
- **Sporeflies**: Spawn in groups of 3-6 near Glowbeasts
- **Sky Grazer**: Spawns in Floating Islands biomes in groups of 1-3
- **Cliff Hunter**: Spawns in Floating Mountains biomes in groups of 1-2

### Technical Implementation
- Created spawn placement handlers for each creature type
- Implemented biome-specific spawning using Forge's biome loading events
- Added appropriate spawn conditions and restrictions
- Registered all entities with proper attributes and behaviors

### Drops
- Glowbeast: Phosphor Hides (common), Glowing Gland (uncommon)
- Sporeflies: Biolume Extract (common), Glowing Dust (common)
- Sky Grazer: Light Membrane (common), Sky Feather (uncommon)
- Cliff Hunter: Razor Claws (common), Chitin Plate (uncommon), Alpha Gland (rare)

## Testing
- [ ] Verify spawns in Bioluminescent Forest
- [ ] Verify spawns in Floating Islands
- [ ] Verify spawns in Floating Mountains
- [ ] Test creature behaviors and pathfinding
- [ ] Verify loot drops and rates

## Next Steps
1. Add custom models and textures (currently using placeholders)
2. Implement advanced AI behaviors
3. Add taming/breeding mechanics for passive creatures
4. Create unique attacks and abilities for the Cliff Hunter
