# CHEX Cloud Agent Contract

You acknowledge that every edit follows this checklist exactly. Deviating causes hand-off gaps and broken builds.

## Setup (run in order)
1. `java -version` — must report Temurin/OpenJDK 17.x
2. `git fetch --all --prune`
3. Linux/macOS: `bash scripts/cloud_bootstrap.sh`
4. Windows: `powershell -ExecutionPolicy Bypass -File scripts/setup_dev_env.ps1 -InstallHooks`
5. `git switch -c feature/<task-id>` — branch per task; never rewrite shared history

## Build (Architectury Loom 1.5.x)
- `./gradlew assemble` — fast smoke compile via Loom
- `./gradlew check` — full compile + Spotless; must pass before hand-off

## Format
- After touching Java: `./gradlew :common:spotlessApply :forge:spotlessApply`
- Data-only changes: `./gradlew spotlessApply`

## Validate
- JSON sanity: `python scripts/validate_json.py` (restore if missing)
- Extra suites as needed: `./gradlew :forge:test`

## Datagen
- Regenerate resources with `./gradlew :forge:runData`
- Review generated files, re-run Spotless, stage minimal diff

## Logging & Notes
- Capture reasoning in `notes/<topic>.md`
- Record milestones in `progress/stepX_<slug>.md`
- Append numbered summary to `PROGRESS_PROMPTS.md`
- Update `CHEX_DETAILED_TASKS.md` checkboxes when tasks advance

## PR / Handoff
1. `git status -sb`
2. `git add <files>`
3. `git commit -m "feat: <short description>"`
4. `git push --set-upstream origin feature/<task-id>`
5. Final summary references: task, notes, progress log, `./gradlew check`

## Do / Don’t
- **Do** run Spotless and `./gradlew check` before finishing
- **Do** protect GTCEu fallback behaviour
- **Do** mention unresolved warnings in the final summary
- **Don’t** edit `progress/` history in place — append only
- **Don’t** merge `master` locally; leave integration to maintainers

## File Map (quick reference)
- `PROJECT_CONTEXT.md` — architecture & scope
- `CHEX_PROMPT_GUIDE.md` — full working instructions
- `CHEX_DETAILED_TASKS.md` — task matrix (top-to-bottom)
- `notes/` — detailed design & intermediate reasoning
- `progress/` — chronological milestone snapshots
- `scripts/setup_dev_env.ps1` — Windows bootstrap (installs hooks)
- `scripts/cloud_bootstrap.sh` — Linux/macOS bootstrap
- `common/` — shared logic/config parsers
- `forge/` — Forge-side implementation + resources (Architectury Loom target)
- `AGENTS.md` — this contract (keep in sync)
