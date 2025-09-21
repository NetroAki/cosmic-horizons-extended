# Cosmic Horizons Extended (CHEX) - Project Context & Features

## Project Overview

**Cosmic Horizons Extended (CHEX)** is a Minecraft 1.20.1 mod that extends the Cosmic Horizons mod with additional planets, biomes, and GregTech CEu integration. The project follows the Cosmic Horizons addon support conventions and uses Architectury for multi-loader compatibility.

## Project Structure

```
Cosmic_Horizons_Expanded/
├── common/                    # Loader-neutral code
│   ├── build.gradle          # Common module build config
│   └── src/main/java/        # Shared Java code
├── forge/                    # Forge-specific implementation
│   ├── build.gradle          # Forge module build config
│   └── src/main/             # Forge resources and code
├── build.gradle              # Root build configuration
├── settings.gradle           # Gradle project settings
└── Architecturary            # Loom usage guide
```

## Implemented Features

### 1. Planet System

**Consolidated Planets**: Pandora and Arrakis consolidated into single planets with multiple biomes each.

#### Pandora (5 Biomes)

1. **Bioluminescent Forest** (`pandora_bioluminescent_forest`)

   - Moderate temperature, high humidity
   - Features: Fungal towers, biolume moss patches, spore soil
   - Flora: Biolume moss, spore soil patches
   - Blocks: Pandorite variants, biolume moss, lumicoral

2. **Floating Mountains** (`pandora_floating_mountains`)

   - Moderate temperature, moderate humidity
   - Features: Pandorite outcrops, floating stone formations
   - Flora: Crystal clusters
   - Blocks: Pandorite stone, cobbled, bricks, mossy, polished

3. **Ocean Depths** (`pandora_ocean_depths`)

   - Cool, very humid, deep underwater
   - Features: Lumicoral clusters, kelp patches
   - Flora: Ocean kelp, lumicoral formations
   - Blocks: Lumicoral blocks

4. **Volcanic Wasteland** (`pandora_volcanic_wasteland`)

   - Hot, dry, volcanic terrain
   - Features: Magma spires, volcanic brick clusters
   - Flora: Volcanic formations
   - Blocks: Volcanic brick variants

5. **Sky Islands** (`pandora_sky_islands`)
   - Cool, dry, high altitude
   - Features: Cloudstone flora, polished plates
   - Flora: Cloudstone formations
   - Blocks: Polished pandorite variants

#### Arrakis (5 Biomes)

1. **Great Dunes** (`arrakis_great_dunes`)

   - Hot, dry desert
   - Features: Sand patches, spice cactus
   - Flora: Spice cactus patches
   - Blocks: Arrakite sandstone variants

2. **Spice Mines** (`arrakis_spice_mines`)

   - Underground spice extraction
   - Features: Spice brick clusters, red spice caps
   - Flora: Red spice caps
   - Blocks: Spice brick formations

3. **Polar Ice Caps** (`arrakis_polar_ice_caps`)

   - Cold, frozen regions
   - Features: Packed ice accents, polar formations
   - Flora: Ice formations
   - Blocks: Packed ice, arrakis salt

4. **Sietch Strongholds** (`arrakis_sietch_strongholds`)

   - Rocky, fortified settlements
   - Features: Sietch rock plates, defensive formations
   - Flora: Rocky formations
   - Blocks: Arrakis rock variants

5. **Stormlands** (`arrakis_stormlands`)
   - Extreme weather conditions
   - Features: Storm ash clusters, crystal shards
   - Flora: Storm crystal formations
   - Blocks: Storm ash, crystal shards

### 2. Custom Block System

#### Pandora Blocks

- **Pandorite Stone**: Base stone variant
- **Pandorite Cobbled**: Cobblestone variant
- **Pandorite Bricks**: Brick variant
- **Pandorite Mossy**: Moss-covered variant
- **Pandorite Polished**: Polished variant
- **Spore Soil**: Special soil for Pandora flora
- **Biolume Moss**: Glowing moss blocks
- **Lumicoral Block**: Underwater coral blocks

#### Arrakis Blocks

- **Arrakite Sandstone**: Base sandstone variant
- **Arrakite Sandstone Cut**: Cut variant
- **Arrakite Sandstone Chiseled**: Chiseled variant
- **Arrakite Sandstone Smooth**: Smooth variant
- **Arrakis Salt**: Salt block variant
- **Spice Node**: Glowing spice source blocks

### 3. World Generation System

#### TerraBlender Integration

- **Region Registration**: CHEX region with parameter points
- **Biome Placement**: Strategic biome placement based on climate parameters
- **Surface Rules**: Custom top blocks for each biome
- **Overlay System**: Configurable overlay to preserve original CH biomes

#### Configured Features

- **20+ Configured Features**: Unique terrain and flora generation
- **Biome Modifiers**: Automatic feature placement in biomes
- **Placed Features**: Strategic placement with proper density
- **Surface Generation**: Custom surface block placement

### 4. Configuration System

#### Planet Overrides (`chex-planets.json5`)

- **Rocket Tier Requirements**: Per-planet rocket tier needs
- **Suit Tag Requirements**: Required suit protection levels
- **Custom Names**: Override planet display names
- **Hot Reloading**: Runtime configuration updates

#### Mineral Distribution (`chex-minerals.json5`)

- **GTCEu Integration**: GregTech mineral distribution
- **Tier-based Mining**: Different mineral tiers per planet
- **Biome-specific Ores**: Ore generation tied to specific biomes
- **Vein Configuration**: Custom ore vein settings

### 5. Command System

#### Admin Commands

- **`/chex reload`**: Reload configuration files
- **`/chex launch [planet]`**: In-game planet travel
- **`/chex_audit_cosmic_data`**: Audit cosmic data integrity
- **`/chex_snapshot_config`**: Export current configuration

### 6. GTCEu Integration

#### Mineral Progression

- **Tier-based Distribution**: MV, HV, EV tier minerals
- **Biome-specific Ores**: Different ores per biome type
- **Progression Gates**: Boss cores unlock higher tiers
- **Tech Tree Integration**: Seamless GTCEu progression

## Documentation Structure

### Core Configuration Files

- **`forge/build.gradle`**: Forge module build configuration
- **`common/build.gradle`**: Common module build configuration
- **`build.gradle`**: Root project build configuration
- **`settings.gradle`**: Gradle project settings

### World Generation Files

- **`forge/src/main/resources/data/cosmic_horizons_extended/dimension/`**: Dimension definitions
- **`forge/src/main/resources/data/cosmic_horizons_extended/worldgen/`**: World generation features
- **`forge/src/main/resources/data/cosmic_horizons_extended/forge/biome_modifier/`**: Biome modifiers
- **`forge/src/main/resources/data/chex/tags/worldgen/biome/`**: Biome tags

### Configuration Files

- **`chex-planets.json5`**: Planet override configuration
- **`chex-minerals.json5`**: Mineral distribution configuration
- **`forge/src/main/resources/assets/cosmic_horizons_extended/lang/en_us.json`**: Localization

### Code Structure

- **`forge/src/main/java/com/netroaki/chex/`**: Main mod package
- **`forge/src/main/java/com/netroaki/chex/registry/`**: Block, item, biome registrations
- **`forge/src/main/java/com/netroaki/chex/commands/`**: Command implementations
- **`forge/src/main/java/com/netroaki/chex/config/`**: Configuration management
- **`forge/src/main/java/com/netroaki/chex/worldgen/`**: World generation code

### TerraBlender Integration

- **`forge/src/main/java/com/netroaki/chex/worldgen/region/CHEXRegion.java`**: Region definition
- **`forge/src/main/java/com/netroaki/chex/registry/biomes/CHEXBiomes.java`**: Biome registration
- **`forge/src/main/resources/data/cosmic_horizons_extended/worldgen/`**: World generation JSONs

### Build System

- **`Architecturary`**: Loom usage guide and best practices
- **`TB_STRATEGY.md`**: TerraBlender integration strategy
- **`PROJECT_CONTEXT.md`**: This comprehensive project overview

## Additional Documentation Files

### Core Documentation

- **`README.md`**: Quick start guide and basic usage
- **`TASKS.md`**: Complete implementation checklist (17 sections, 400+ tasks)
- **`Checklist`**: Forge-only implementation plan and design notes

### Technical Guides

- **`GTCEU_INTEGRATION.md`**: GregTech CEu integration modes and troubleshooting
- **`gettingstartedterrablender`**: TerraBlender setup and configuration guide
- **`MISSING_FEATURES.md`**: Current implementation status and missing features

### Research & Reference

- **`Research`**: Comprehensive research on Cosmic Horizons and Cosmic Unbound mods
- **`Cosmic_Horizons_Addon_Support_Consolidated.md`**: Complete guide for CH datapack development

### Example Code

- **`Example/Fabric/`**: TerraBlender example implementation for Fabric
- **`Example/Forge/`**: TerraBlender example implementation for Forge
- **`Planet Design doc`**: Detailed planet design specifications

### TerraBlender API Reference

- **`Terrablender API Common/`**: Complete TerraBlender API source code and documentation
  - **`Region.java`**: Core region class for biome parameter mapping
  - **`ParameterUtils.java`**: Climate parameter utilities and enums
  - **`SurfaceRuleManager.java`**: Surface rule management system
  - **`Regions.java`**: Region registration utilities
  - **`RegionType.java`**: Region type definitions
  - **`ModifiedVanillaOverworldBuilder.java`**: Vanilla overworld modifications
  - **`TerrablenderOverworldBiomeBuilder.java`**: Custom overworld biome builder
  - **`VanillaParameterOverlayBuilder.java`**: Vanilla parameter overlay system

## Key Features by File

### Biome Definitions

- **`CHEXBiomes.java`**: All 10 biome ResourceKeys and registration
- **`CHEXRegion.java`**: TerraBlender region with parameter points
- **Biome JSONs**: Individual biome configuration files

### Block System

- **`CHEXBlocks.java`**: All custom block registrations
- **Block JSONs**: Block state and model definitions
- **Localization**: English names for all blocks

### World Generation

- **Configured Features**: 20+ unique terrain features
- **Placed Features**: Strategic feature placement
- **Biome Modifiers**: Automatic feature distribution
- **Surface Rules**: Custom surface generation

### Configuration Management

- **`PlanetOverrides.java`**: Planet configuration parser
- **`MineralsConfigCore.java`**: Mineral distribution parser
- **JSON5 Files**: Hot-reloadable configuration

### Command System

- **`ChexCommands.java`**: All command implementations
- **Admin Tools**: Configuration management commands
- **Player Tools**: In-game planet travel

## Integration Points

### Cosmic Horizons

- **Addon Support**: Follows CH addon conventions
- **GUI Travel**: Uses CH's GUI-based travel system
- **Dimension JSONs**: Compatible with CH dimension system

### GregTech CEu

- **Mineral Distribution**: Direct GTCEu ore integration
- **Tech Progression**: Boss cores unlock GTCEu tiers
- **Tag System**: Uses GTCEu ore tags for compatibility

### TerraBlender

- **Region Overlay**: Configurable biome overlay system
- **Parameter Points**: Climate-based biome placement
- **Surface Rules**: Custom surface generation per biome

## Development Status

### ✅ Completed Features

- Planet consolidation (Pandora, Arrakis)
- 10 custom biomes with unique characteristics
- 15+ custom blocks with variants
- TerraBlender region integration
- World generation system (20+ features)
- Configuration system with hot reloading
- Command system for admin and player use
- Localization system
- GTCEu integration framework

### 🔄 In Progress

- Boss system implementation
- Advanced GTCEu mineral distribution
- Client-side features and UI
- Sound system integration

### 📋 Planned Features

- Additional planets (Exoplanet Alpha, etc.)
- Advanced progression systems
- Custom mobs and entities
- Advanced world generation features
- Multi-language support

## Technical Architecture

### Multi-loader Support

- **Architectury**: Cross-platform compatibility
- **Common Module**: Shared logic between loaders
- **Forge Module**: Forge-specific implementations
- **Future Fabric**: Planned Fabric support

### Data-driven Design

- **JSON5 Configuration**: Human-readable config files
- **Hot Reloading**: Runtime configuration updates
- **Modular System**: Easy to extend and modify
- **Tag-based Integration**: Compatible with other mods

### Performance Considerations

- **Efficient World Gen**: Optimized feature placement
- **Lazy Loading**: On-demand resource loading
- **Memory Management**: Efficient block and item handling
- **Caching**: Smart caching for frequently accessed data

This project represents a comprehensive expansion of the Cosmic Horizons mod with deep integration into the GregTech CEu ecosystem and modern Minecraft modding practices.
