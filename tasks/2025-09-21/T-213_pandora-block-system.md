# T-213 Pandora Block System

**Goal**

- Implement comprehensive block system for Pandora with unique materials and visual effects.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Pandorite Family: stone, cobbled, bricks, mossy, polished variants (dark blue base with glowing cyan veins)
- Biolume Moss: purple grass variant with faint cyan glow
- Spore Soil: soft, brown-purple dirt with glowing speckles
- Crystal-Clad Pandorite: stone with embedded cyan/emerald crystals
- Lumicoral Blocks: coral variants with pulsing cyan nodes
- Volcanic Pandorite: cracked black stone with orange lava veins
- Ash Sand: black gritty sand that damages bare feet
- Cloudstone: white, soft-textured stone with light scattering effect
- Skygrass: light green-tinted grass with glowing white flowers
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Pandorite block family
- [ ] Create Biolume Moss and Spore Soil
- [ ] Add Crystal-Clad Pandorite
- [ ] Implement Lumicoral Blocks
- [ ] Create Volcanic Pandorite and Ash Sand
- [ ] Add Cloudstone and Skygrass
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
