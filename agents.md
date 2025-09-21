# CHEX Cloud Agent Contract

You acknowledge that every edit follows this checklist exactly. Deviating causes hand-off gaps and broken builds.

## Setup (run in order)

1. `java -version` - must report Temurin/OpenJDK 17.x
2. `git fetch --all --prune`
3. Linux/macOS: `bash scripts/cloud_bootstrap.sh`
4. Windows: `powershell -ExecutionPolicy Bypass -File scripts/setup_dev_env.ps1 -InstallHooks`
5. `bash scripts/spawn_task_branches.sh tasks/<date>/T-XXX_slug.md` - spawn feature branch + claim (use Git Bash or WSL on Windows)

## Claiming Workflow

- **ALWAYS** run `scripts/spawn_task_branches.sh tasks/<date>/T-XXX_slug.md` - this creates both the feature branch AND the claim file
- **NEVER** use `git switch -c feature/<task-id>` directly - this bypasses the claim system
- Pull latest tasks before claiming: `git pull --ff-only origin master` (from master) to get newest brief
- Verify task is free: check if `claims/<slug>.claim` already exists before starting
- If `claims/` directory is missing: recreate it with `mkdir -p claims` and ensure `claims/.gitkeep` is tracked
- The spawn script handles: branch creation, claim file creation, task file staging, and initial commit
- Push immediately after claiming so other agents see the lock: `git push --set-upstream origin feature/<slug>`
- **NEVER** edit someone else's claim file without coordination

## Build (Architectury Loom 1.5.x)

- `./gradlew assemble` - fast smoke compile via Loom
- `./gradlew check` - full compile + Spotless; must pass before hand-off

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

## Multi-Agent Coordination

- **Check claims first**: Always verify `claims/<slug>.claim` doesn't exist before starting work
- **Respect active claims**: If a claim exists, coordinate hand-off or choose a different task
- **Communicate conflicts**: If you find conflicting work, document in `notes/` and coordinate
- **Update claims**: Mark tasks as completed by updating the claim file or removing it
- **Clean up**: Remove claim files when tasks are fully completed and merged
- **Never edit claims manually**: Only use the spawn script to create/update claims
- **Verify claims/.gitkeep**: Ensure this file is tracked so fresh clones retain the claims directory

## Do / Don't

- **Do** use `scripts/spawn_task_branches.sh` to claim tasks; do not create feature branches manually
- **Do** check for existing claims before starting any task
- **Do** run Spotless and `./gradlew check` before finishing
- **Do** protect GTCEu fallback behaviour
- **Do** mention unresolved warnings in the final summary
- **Don't** edit `progress/` history in place - append only
- **Don't** merge `master` locally; leave integration to maintainers
- **Don't** assume a task is free without checking for an existing `.claim`
- **Don't** create feature branches without using the spawn script
- **Don't** edit someone else's claim file without coordination

## File Map (quick reference)

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
