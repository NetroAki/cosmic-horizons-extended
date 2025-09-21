# CHEX Remote Agent Quickstart

This repo already carries a detailed workflow in `CHEX_PROMPT_GUIDE.md`, the task matrix, and the progress logs. The bullet points below condense the essentials so a cloud-hosted Codex agent can come up to speed quickly and avoid trampling local context.

## Required Boot Sequence
- Read `PROJECT_CONTEXT.md`, `CHEX_DETAILED_TASKS.md`, and the latest entries in `PROGRESS_PROMPTS.md` before editing code.
- Mirror transient reasoning into `notes/` files and record milestones in `progress/stepX_<slug>.md`; never delete prior logs.
- Run `git fetch` at session start so remote state is current (do **not** auto-merge).
- Use the provided tooling: `./gradlew check` (Spotless + build), `./gradlew :common:spotlessApply :forge:spotlessApply` to repair formatting, and `scripts/setup_dev_env.ps1` when a new workspace spins up.

## Coding & Formatting Guardrails
- Java formatting is enforced via Spotless (Google Java Format) and Prettier for JSON/JSON5/Markdown. Always run the appropriate `spotlessApply` target after touching sources or resources.
- Resource JSONs now validate cleanly; keep them valid by running the repo-wide JSON sanity script if you add data assets (see `notes/dev_env_setup_notes.md`).
- Register new features/biomes via the TerraBlender-compatible registries. Mineral generation now routes through `MineralBiomeModifier` (BiomeModifier); inject additions there instead of the removed `BiomeLoadingEvent` hook.

## Task Flow Expectations
- Advance items top-to-bottom through `CHEX_DETAILED_TASKS.md`, splitting work into substeps logged under `progress/stepX_*.md` and referenced from `PROGRESS_PROMPTS.md`.
- Keep GTCEu compatibility and fallback behaviors in mind (fluids/ores). When introducing tiers, update both the config parsers in `common/` and the Forge registries.
- After completing a milestone: update `notes/` (if needed), append a numbered prompt entry to `PROGRESS_PROMPTS.md`, and mark the checkbox in the task matrix.

## Validation Checklist
- `./gradlew check` must pass before handing off (includes Spotless + compile).
- Run targeted gradle tasks (e.g., `:forge:runData`) when datagen changes occur; do not commit generated output without formatting.
- Always mention unresolved build warnings or failing tasks in the final summary so the next agent sees the outstanding work.

Following these points keeps the cloud agent in sync with the established workflow and ensures later sessions can pick up with minimal friction.
