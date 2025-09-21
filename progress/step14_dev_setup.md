# Step 14 - Dev Environment Script (2025-09-21)

- Authored scripts/setup_dev_env.ps1 to validate Java/Gradle, optionally warm Forge runs, mirror default configs, and install the Spotless hook.
- Added dry-run support for safe preview and documented usage flags in notes/dev_env_setup_notes.md.
- Confirmed script executes in dry-run mode via powershell.exe -ExecutionPolicy Bypass -File scripts/setup_dev_env.ps1 -DryRun.
