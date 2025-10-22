# Models & Animations Tracker

Status snapshot for placeholder coverage and upcoming custom asset work. Keep this file updated as you replace stand-ins with final art or wire up bespoke animations.

## Entity Overview

| Planet/Theme | Entity | Placeholder Texture | Placeholder Model | Placeholder GeoJSON | Notes / Next Steps |
| --- | --- | --- | --- | --- | --- |
| Pandora | Sporefly | ✅ (`textures/entity/sporefly.png`) | ✅ (`models/entity/sporefly.json`) | ✅ (`models/entity/sporefly.geo.json`) | Needs custom wing rig + hover animation. |
| Pandora | Spore Tyrant | ✅ | ✅ | ✅ | Requires multi-part boss model, attack + spore emission anims. |
| Pandora | Sporeflies (swarm) | ✅ | ✅ | ✅ | Replace cube placeholder with multi-body sprite sheet. |
| Arrakis | Sandworm Hatchling | ✅ | ✅ | ✅ | Model should support burrow/in-sand animation. |
| Inferno Prime | Ash Crawler | ✅ | ✅ | ✅ | Final asset should crawl/climb, add ember FX. |
| Inferno Prime | Fire Wraith | ✅ | ✅ | ✅ | Needs semi-transparent mesh, cloak billow animation. |
| Inferno Prime | Magma Hopper | ✅ | ✅ | ✅ | Implement squash & stretch hop cycle. |
| Inferno Prime | Infernal Sovereign | ✅ | ✅ | ✅ | Large boss rig, winged flight + ground combos. |
| Aqua Mundus | Ocean Sovereign | ✅ | ✅ | ✅ | Multi-head serpent, segmented swimming anims. |
| Stormworld | Windrider | ✅ | ✅ | ✅ | Bird/storm hybrid; wing flaps + lightning charge. |
| Generic | Glowbeast | ✅ | ✅ | ✅ | Simple flying mammal; add idle sway + ambient glow. |

> ✅ = placeholder generated via `scripts/create_placeholder_textures.ps1`

## Animated Block / Feature Tracking

| Block / Feature | Placeholder Texture | Placeholder Model | Animation Placeholder | Notes |
| --- | --- | --- | --- | --- |
| Luminous Kelp | ✅ (`textures/block/aqua_luminous_kelp.png`) | Cube (`models/block/aqua_luminous_kelp.json`) | ❌ | Build bending frond geo + gentle sway animation. |
| Kepler Blossom | ✅ | Cube | ❌ | Add petals + open/close anim with particle burst. |
| Hydrothermal Vent | ✅ (reuses basalt tint) | Cube | ❌ | Needs layered model + steam particles. |
| Spore Soil / Spore Blocks | ✅ | Cube | ❌ | Final asset should pulse/glow periodically. |

## Particle & FX Wishlist

| Effect | Placeholder | Notes |
| --- | --- | --- |
| Pandora Spore Haze | ❌ | Create lightweight particle emitter tied to spore blocks. |
| Arrakis Sand Dust | ❌ | Hook weather system to spawn directional dust. |
| Inferno Embers | ❌ | Tie to ash crawler + ambient biome. |
| Crystalis Frost Drift | ❌ | Use low-opacity sprite drifting upward. |
| Aqua Mundus Biolume Sparkles | ❌ | Add around luminous kelp + ocean sovereign. |

## Recommended Next Steps

1. Prioritise **Sporefly**, **Ash Crawler**, and **Sandworm Hatchling** for bespoke models/animations (core gameplay touchpoints).  
2. Once final models exist, update the `models/entity/*.json` and remove the cube placeholders.  
3. Coordinate with particle FX pass so animated blocks/entities emit matching visuals.  
4. Keep this tracker in sync with progress—tick off placeholders as they are replaced with production assets. |
