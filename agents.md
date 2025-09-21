# CHEX Cloud Agent Contract

You acknowledge that every edit follows this checklist exactly. Deviating causes hand-off gaps and broken builds.

## Setup (run in order)

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

- **Do** run Spotless and `./gradlew check` before finishing
- **Do** protect GTCEu fallback behaviour
- **Do** mention unresolved warnings in the final summary
- **Don't** edit `progress/` history in place - append only
- **Don't** merge `master` locally; leave integration to maintainers
- **Don't** assume a task is free without checking for an existing `.claim`

- `PROJECT_CONTEXT.md` - architecture & scope
- `CHEX_PROMPT_GUIDE.md` - full working instructions
- `CHEX_DETAILED_TASKS.md` - task matrix (top-to-bottom)
- `tasks/` - task briefs agents reference when claiming
- `claims/` - active task locks; contains `.gitkeep` and individual `.claim` files
- `notes/` - detailed design & intermediate reasoning
- `progress/` - chronological milestone snapshots
- `scripts/setup_dev_env.ps1` - Windows bootstrap (installs hooks)
- `scripts/cloud_bootstrap.sh` - Linux/macOS bootstrap
- `scripts/spawn_task_branches.sh` - branch + claim automation
- `AGENTS.md` - this contract (keep in sync)
