# T-312 Neutron Star Forge Biome Implementation

**Goal**

- Implement 5 unique biomes for Neutron Star Forge with neutron star, magnetic, and forge environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/neutron_star_forge_*.json`
- Biome-specific terrain generation and neutron star effects
- Magnetic and forge exploration systems

**Acceptance**

- Accretion Rim: Area where matter falls onto neutron star
- Magnetar Belts: Areas with intense magnetic fields
- Forge Platforms: Areas where matter is forged
- Gravity Wells: Areas with extreme gravity
- Radiation Shelters: Areas protected from radiation
- Each biome has distinct visual characteristics and neutron star effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Accretion Rim biome
- [ ] Create Magnetar Belts biome
- [ ] Implement Forge Platforms biome
- [ ] Add Gravity Wells biome
- [ ] Create Radiation Shelters biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
