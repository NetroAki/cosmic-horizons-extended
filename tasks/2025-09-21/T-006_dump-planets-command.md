# T-006 /chex dumpPlanets Command

## Goal
Implemented `/chex dump_planets [--reload] [outputFile]` command to display planet information in-game and optionally save it to a JSON file.

## Implementation Details

### 1. Command Structure
- Added support for optional `--reload` flag to refresh the planet registry before dumping
- Added support for optional output file path to save the planet data as JSON
- Command requires OP level 2 permission

### 2. Features
- Displays a formatted table of all registered planets with their properties
- Can reload the planet registry on-demand with the `--reload` flag
- Saves planet data to a JSON file if an output path is provided
- Includes comprehensive error handling and user feedback

### 3. Output Format
- Console output shows a human-readable table with planet details
- JSON output includes all planet properties for programmatic use
- Timestamps all outputs for tracking changes over time

### 4. Usage Examples
```
/chex dump_planets                     # Show planet list in chat
/chex dump_planets --reload            # Reload planets and show list
/chex dump_planets path/to/output.json # Save planet data to file
/chex dump_planets --reload path/to/output.json  # Reload and save
```

## Testing
- [x] Verified command output in-game
- [x] Tested with and without --reload flag
- [x] Verified file output format and content
- [x] Confirmed error handling for file operations

## Related Changes
- Added `reloadDiscoveredPlanets()` to `PlanetRegistry`
- Added file utility methods for saving JSON data
- Updated command argument handling and suggestions

## Checklist
- [x] Implement command handler + wiring
- [x] Add support for --reload flag
- [x] Add file output functionality
- [x] Add command suggestions for file paths
- [x] Test all command variations
- [x] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [x] `./gradlew check`
- [x] Update documentation
- [x] Open PR referencing this task file
