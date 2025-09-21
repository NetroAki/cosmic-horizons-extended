# Dev Environment Setup Notes (2025-09-21)

- Added PowerShell helper at scripts/setup_dev_env.ps1 with comment-based help and switches for SkipGradle, SkipConfigs, InstallHooks, DryRun.
- Script warms Forge cache via ./gradlew --stop && ./gradlew genIntellijRuns unless SkipGradle is supplied.
- Copies default config JSON5 files from forge/config into run/config/chex (directory-aware, supports dry-run).
- Highlights key docs (PROJECT_CONTEXT.md, CHEX_DETAILED_TASKS.md, PROGRESS_PROMPTS.md, TB_STRATEGY.md, notes/, progress/, Checklist).
- Optional InstallHooks flag copies scripts/git-hooks/pre-commit into .git/hooks/pre-commit, honoring DryRun previews.
- Verified script loads via powershell.exe -ExecutionPolicy Bypass -File scripts/setup_dev_env.ps1 -DryRun.
