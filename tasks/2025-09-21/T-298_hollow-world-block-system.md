# T-298 Hollow World Block System

**Goal**

- Implement comprehensive block system for Hollow World with cavern, void, and crystal materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Glowstone Fungi: fungi that provide light
- Void Stone: stone with void properties
- Stalactite Variants: various stalactite types
- River Biolum Flora: glowing plant life in rivers
- Crystal Formations: massive crystal structures
- `./gradlew check` passes

**Checklist**

- [x] `bash scripts/cloud_bootstrap.sh`
- [x] Implement Glowstone Fungi
  - Added glowing effect with light level 15
  - Created custom model and textures
  - Set appropriate properties for a fungus-like block
- [x] Create Void Stone
  - Added void-like properties with subtle glow
  - Created dark, mysterious texture
  - Set appropriate hardness and resistance
- [x] Add Stalactite Variants
  - Created directional stalactite model
  - Added appropriate collision shapes
  - Set up block properties for natural generation
- [x] Implement River Biolum Flora
  - Added Luminous Reed with glowing properties
  - Created water-adjacent plant behavior
  - Set up proper rendering for translucent plants
- [x] Create Crystal Formations
  - Added translucent crystal blocks
  - Implemented light emission
  - Created visually appealing model
- [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollow_world_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
