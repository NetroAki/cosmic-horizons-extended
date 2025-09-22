# Player Tier Capability Implementation

## Overview
Implemented the `PlayerTierCapability` system to track player progression through rocket/suit tiers and milestone achievements. This system is crucial for gating content and managing player progression in the mod.

## Key Features

### Core Capability
- Added `PlayerTierCapability` class with support for:
  - Rocket/Nodule tiers (1-10)
  - Suit tiers (1-5)
  - Planet unlocks
  - Mineral discoveries
  - Milestone achievements

### Networking
- Implemented `PlayerTierSyncMessage` for client-server synchronization
- Added network packet handling for capability updates
- Ensured proper serialization/deserialization of all capability data

### Integration
- Added hooks in `LaunchHooks` for tier validation during rocket launches
- Integrated with `DimensionHooks` for dimension entry requirements
- Added command support for debugging and testing

### Testing
- Created comprehensive unit tests covering all capability functionality
- Verified network synchronization
- Tested edge cases and error conditions

## Usage
Players progress through tiers by:
1. Unlocking new rocket/nodule tiers to access more advanced planets
2. Upgrading their suit to survive in more hazardous environments
3. Discovering new planets and resources
4. Achieving milestones that unlock new content

## Technical Details
- Uses Forge's capability system for data persistence
- Implements `INBTSerializable` for NBT storage
- Includes proper client-server synchronization
- Follows mod's existing coding standards and patterns
