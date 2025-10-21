# Cosmic Horizons Expanded (CHEX) - Project Overview

## 🚀 Project Summary

**Cosmic Horizons Expanded (CHEX)** is a comprehensive Minecraft 1.20.1 mod that extends the Cosmic Horizons mod with 30+ planets, advanced progression systems, and deep GregTech CEu integration. The project uses Architectury for multi-loader compatibility and follows modern Minecraft modding best practices.

### Key Features

- **30+ Planets**: From early space exploration (Tier 1) to post-GT completion content (Tier 15+)
- **GregTech CEu Integration**: Seamless progression through GT tech tree
- **Boss-Gated Progression**: Defeat bosses to unlock new technologies and planets
- **Environmental Hazards**: Planet-specific challenges requiring advanced suits
- **Data-Driven Configuration**: Hot-reloadable JSON5 configs for easy customization
- **Multi-Loader Support**: Architectury framework for Forge/Fabric compatibility

## 📁 Critical Project Files

### 🎯 **START HERE - Essential Files**

#### Project Management

- **`tasks/2025-09-21/COMPREHENSIVE_TASK_TRACKING.md`** - Complete task tracking with 426 individual tasks
- **`tasks/2025-09-21/REPEATABLE_TASK_PROMPT.md`** - Repeatable prompt for systematic task execution
- **`PLANET_DESIGN_TASKS.md`** - Detailed planet implementation specifications
- **`PROJECT_CONTEXT.md`** - Comprehensive project documentation and features

#### Core Configuration

- **`build.gradle`** - Root build configuration with dependencies
- **`settings.gradle`** - Gradle project settings and module structure
- **`common/build.gradle`** - Common module build configuration
- **`forge/build.gradle`** - Forge-specific build configuration

### 🏗️ **Build & Development Files**

#### Build System

- **`gradlew`** / **`gradlew.bat`** - Gradle wrapper for building
- **`gradle.properties`** - Gradle configuration properties
- **`architectury.mixins.json`** - Mixin configuration
- **`architectury-forge-refmap.json`** - Forge reference map

#### Development Tools

- **`scripts/validate_json.py`** - JSON validation script
- **`scripts/cloud_bootstrap.sh`** - Development environment setup
- **`scripts/setup_dev_env.ps1`** - Windows development setup
- **`scripts/spawn_task_branches.sh`** - Git branch management

### 🌍 **Planet Implementation Files**

#### Core Planet Systems

- **`forge/src/main/java/com/netroaki/chex/registry/CHEXBiomes.java`** - All biome registrations
- **`forge/src/main/java/com/netroaki/chex/registry/CHEXBlocks.java`** - All block registrations
- **`forge/src/main/java/com/netroaki/chex/worldgen/region/CHEXRegion.java`** - TerraBlender region
- **`forge/src/main/resources/data/cosmic_horizons_extended/dimension/`** - Dimension definitions

#### World Generation

- **`forge/src/main/resources/data/cosmic_horizons_extended/worldgen/`** - World generation features
- **`forge/src/main/resources/data/cosmic_horizons_extended/forge/biome_modifier/`** - Biome modifiers
- **`forge/src/main/resources/data/chex/tags/worldgen/biome/`** - Biome tags

### ⚙️ **Configuration Files**

#### Runtime Configuration

- **`config/chex/chex-planets.json5`** - Planet override configuration
- **`config/chex/chex-minerals.json5`** - Mineral distribution configuration
- **`config/chex/fallback_ores.json5`** - Fallback ore blocks when GTCEu is missing
- **`cosmic_horizons_extended-common.toml`** - Behavior toggles and fuel mapping

#### Localization

- **`forge/src/main/resources/assets/cosmic_horizons_extended/lang/en_us.json`** - English localization
- **`forge/src/main/resources/assets/cosmic_horizons_extended/lang/`** - Additional language files

### 🎮 **Game Systems**

#### Commands

- **`forge/src/main/java/com/netroaki/chex/commands/ChexCommands.java`** - All command implementations
- **`forge/src/main/java/com/netroaki/chex/commands/`** - Individual command classes

#### Configuration Management

- **`forge/src/main/java/com/netroaki/chex/config/`** - Configuration parsing and management
- **`forge/src/main/java/com/netroaki/chex/registry/`** - Registry management

#### World Generation

- **`forge/src/main/java/com/netroaki/chex/worldgen/`** - World generation code
- **`forge/src/main/java/com/netroaki/chex/features/`** - Custom world generation features

### 📚 **Documentation Files**

#### Core Documentation

- **`README.md`** - Quick start guide and basic usage
- **`TASKS.md`** - Implementation checklist (17 sections, 400+ tasks)
- **`Checklist`** - Forge-only implementation plan

#### Technical Guides

- **`GTCEU_INTEGRATION.md`** - GregTech CEu integration guide
- **`TB_STRATEGY.md`** - TerraBlender integration strategy
- **`MISSING_FEATURES.md`** - Current implementation status
- **`gettingstartedterrablender`** - TerraBlender setup guide

#### Research & Reference

- **`Research`** - Comprehensive research on Cosmic Horizons mods
- **`Cosmic_Horizons_Addon_Support_Consolidated.md`** - CH datapack development guide
- **`Planet Design doc new copy.md`** - Detailed planet design specifications

### 🎯 **Task Management**

#### Individual Task Files

- **`tasks/2025-09-21/T-001_fuel-registry-fallbacks.md`** - Example task file format
- **`tasks/2025-09-21/T-020_pandora-dimension-json.md`** - Pandora dimension setup
- **`tasks/2025-09-21/T-219_arrakis-dimension-setup.md`** - Arrakis dimension setup
- **`tasks/2025-09-21/`** - Directory containing all 426 individual task files

#### Progress Tracking

- **`progress/`** - Directory containing progress documentation
- **`PROGRESS_PROMPTS.md`** - Progress tracking prompts
- **`notes/`** - Development notes and planning documents

### 🔧 **Example & Reference Code**

#### TerraBlender Examples

- **`Example/Fabric/`** - TerraBlender example for Fabric
- **`Example/Forge/`** - TerraBlender example for Forge
- **`Terrablender API Common/`** - Complete TerraBlender API reference

#### Temporary Development Files

- **`temp_density_functions/`** - Temporary density function implementations
- **`temp_jei_files/`** - Temporary JEI integration files
- **`kubejs/`** - KubeJS integration examples
- **`kubejs_examples/`** - KubeJS usage examples

## 🎯 **Quick Start Guide**

### For New Developers

1. **Read**: `PROJECT_CONTEXT.md` for comprehensive project overview
2. **Check**: `tasks/2025-09-21/COMPREHENSIVE_TASK_TRACKING.md` for current progress
3. **Use**: `tasks/2025-09-21/REPEATABLE_TASK_PROMPT.md` for systematic task execution
4. **Build**: Run `./gradlew build` to compile the project
5. **Test**: Use `./gradlew :forge:runClient` to test in development

### For Task Execution

1. **Copy** the prompt from `REPEATABLE_TASK_PROMPT.md`
2. **Paste** it as your message to the AI
3. **Repeat** for each subsequent task
4. **Track** progress in `COMPREHENSIVE_TASK_TRACKING.md`

### For Configuration

1. **Edit** `config/chex/chex-planets.json5` for planet settings
2. **Modify** `config/chex/chex-minerals.json5` for ore distribution
3. **Use** `/chex reload` command to apply changes
4. **Validate** with `python scripts/validate_json.py`

## 🏗️ **Project Architecture**

### Multi-Loader Support

- **Architectury**: Cross-platform compatibility framework
- **Common Module**: Shared logic between Forge and Fabric
- **Forge Module**: Forge-specific implementations
- **Future Fabric**: Planned Fabric support

### Data-Driven Design

- **JSON5 Configuration**: Human-readable config files with comments
- **Hot Reloading**: Runtime configuration updates without restart
- **Modular System**: Easy to extend and modify
- **Tag-based Integration**: Compatible with other mods

### Integration Points

- **Cosmic Horizons**: Addon support following CH conventions
- **GregTech CEu**: Direct mineral distribution and tech progression
- **TerraBlender**: Biome overlay system with climate parameters
- **KubeJS**: Scripting support for advanced customization

## 📊 **Current Status**

### ✅ Completed (37 tasks)

- **Arrakis Implementation**: Complete desert world with terrain, biomes, structures, mobs, equipment, villagers, boss encounters, environment, and quest system
- **Quest System**: Full quest implementation with main story, side quests, and reputation system
- **Reputation System**: Faction-based reputation with multiple tiers and benefits

### 🔄 In Progress

- **Quest Rewards**: Final quest reward implementation for Arrakis

### 📋 Upcoming (389 tasks)

- **Pandora Implementation**: Next priority planet (Tier 3)
- **Alpha Centauri A**: Stellar world implementation (Tier 5)
- **GTCEu Integration**: Chemical processing chains and fuel systems
- **Boss System**: Core boss mechanics and progression integration

## 🎯 **Key Commands**

### Admin Commands

- **`/chex reload`** - Reload all configuration files
- **`/chex dumpPlanets`** - Export planet data to JSON
- **`/chex travel <tier>`** - List reachable planets for tier
- **`/chex minerals <planetId>`** - Show mineral distribution for planet
- **`/chex launch <planetId>`** - Travel to planet with validation

### Development Commands

- **`/chex_audit_cosmic_data`** - Audit cosmic data integrity
- **`/chex_snapshot_config`** - Export current configuration

## 🔗 **External Resources**

- **Cosmic Horizons Addon Docs**: https://cosmic-mod.github.io/addonsupport/
- **GregTech CEu**: https://github.com/GregTechCEu/GregTech
- **TerraBlender**: https://github.com/Glitchfiend/TerraBlender
- **Architectury**: https://github.com/architectury/architectury

## 📝 **Development Notes**

- **Java Version**: 17 (required)
- **Minecraft Version**: 1.20.1
- **Forge Version**: Latest 1.20.1
- **Architectury Version**: 1.5-SNAPSHOT
- **Build Tool**: Gradle with Loom

This project represents a comprehensive expansion of the Cosmic Horizons mod with deep integration into the GregTech CEu ecosystem and modern Minecraft modding practices. The systematic task-based approach ensures thorough implementation of all planned features.
