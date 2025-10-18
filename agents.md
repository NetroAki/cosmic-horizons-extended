# Agent Roles - Cosmic Horizons Expanded

## Agent Role: Compilation Fixes Executor

**Purpose:**  
Systematically execute the compilation fixes tasks in `progress/compilation_fixes/` to resolve all 100 compilation errors and achieve a clean build. Follow the organized task breakdown to efficiently progress through the fixes.

**Workflow:**

1. **Start with Master Index**
   - Open `progress/compilation_fixes/00_MASTER_INDEX.md`
   - Review the 9 organized tasks and execution order
   - Understand the systematic approach and time estimates

2. **Execute Tasks Sequentially**
   - **Task 01**: Registry Import Fixes (30 min)
   - **Task 02**: Missing Base Classes (45 min)  
   - **Task 03**: Duplicate Methods (20 min)
   - **Task 04**: Incomplete World Systems (15 min)
   - **Task 05**: Remaining Symbol Errors (30 min)
   - **Task 06**: Clean Build Verification (15 min)
   - **Task 07**: Placeholder Textures (45 min)
   - **Task 08**: Models & Animations Tracking (15 min)
   - **Task 09**: Entity Placeholders (30 min)

3. **For Each Task:**
   - Read the specific task file (e.g., `01_registry_import_fixes.md`)
   - Follow the detailed solution steps
   - Apply the fixes as described
   - Run validation checks:
     - `./gradlew check`
     - `./gradlew assemble`
     - `python scripts/validate_json.py`
   - Verify error count reduction
   - Document progress and any issues

4. **Final Verification:**
   - Ensure zero compilation errors
   - Run `./gradlew check` successfully
   - Test server startup: `./gradlew :forge:runServer`
   - Generate placeholder assets: `.\scripts\create_placeholder_textures.ps1`

**Output:**

- Task completion status for each of the 9 tasks
- Error count reduction progress
- Build validation results
- Asset generation verification
- Any remaining issues or recommendations

**Guardrails:**

- Follow the systematic approach in the task files
- Do not skip tasks or change the order
- Preserve existing functionality when fixing errors
- Use the provided solutions and time estimates
- Document any deviations or additional issues found

---

## Agent Role: Asset Generation Specialist

**Purpose:**  
Execute the placeholder texture generation system and create all necessary assets for blocks, items, and entities. Use the automated ImageMagick-based system to generate planet-themed placeholder assets for development and debugging.

**Workflow:**

1. **Setup and Prerequisites**
   - Verify ImageMagick is installed and accessible
   - Confirm Minecraft assets are available at `InventivetalentDev-minecraft-assets-af628ec/`
   - Review the texture generation script: `scripts/create_placeholder_textures.ps1`

2. **Execute Asset Generation**
   - Run the main texture script: `.\scripts\create_placeholder_textures.ps1`
   - Monitor the generation of:
     - **15+ Block textures** with planet themes
     - **10+ Item textures** for progression items
     - **10 Entity textures** with planet themes
     - **Complete JSON models** (block states, item models, entity models)

3. **Verify Asset Creation**
   - Check that all textures are generated in correct directories
   - Verify JSON model files are created properly
   - Confirm planet color themes are applied correctly:
     - **Purple**: Pandora entities (bioluminescence)
     - **Orange**: Arrakis entities (desert)
     - **Red**: Inferno Prime entities (heat/fire)
     - **Blue**: Aqua Mundus entities (ocean)
     - **Cyan**: Stormworld entities (storms)

4. **Test Asset Loading**
   - Run build validation: `./gradlew check`
   - Test server startup: `./gradlew :forge:runServer`
   - Verify no missing texture errors in logs
   - Test entity spawning to confirm textures load

**Output:**

- Asset generation completion status
- Texture file counts and locations
- JSON model verification results
- Build and runtime validation results
- Any missing or problematic assets

**Guardrails:**

- Use the provided PowerShell script for consistency
- Do not manually create textures unless script fails
- Maintain planet color themes for easy identification
- Verify all assets are properly referenced in JSON files
- Test in-game to ensure assets load correctly
