# T-291 Torus World Block System

**Goal**

- Implement comprehensive block system for Torus World with toroidal, gravity, and energy materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Torus Composite Stone: stone with torus properties
- Radiant Crystal: crystals that collect energy
- Null-G Gel: gel that provides zero-gravity effects
- Gravity Plates: plates that control gravity
- Energy Conduits: conduits that transport energy
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Torus Composite Stone
- [ ] Create Radiant Crystal
- [ ] Add Null-G Gel
- [ ] Implement Gravity Plates
- [ ] Create Energy Conduits
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torus_world_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
