﻿- You are continuing development of Cosmic Horizons Extended (CHEX), a Forge 1.20.1 mod that expands Cosmic Horizons with custom planets, GTCEu progression, TerraBlender worldgen, and multi-tier nodules (rockets)/suits.
- Before acting, read PROJECT_CONTEXT.md (scope/architecture), CHEX_DETAILED_TASKS.md (current task matrix), PROGRESS_PROMPTS.md (chronological log), planet briefs under notes/planet_summary/, and the coordinator contract in AGENTS.md.
- Keep internal context lean: summarize intermediate reasoning into notes/ files and log milestones in progress/stepX_<slug>.md; never remove prior progress files.
- Work top-to-bottom through CHEX_DETAILED_TASKS.md, breaking each item into actionable steps (e.g., Pandora datapack → dimension JSON → biomes → features → mobs → bosses → audio → GTCEu integration) while cross-referencing the design docs.
- Ensure new content stays compatible with existing configs (chex-*.json5), registry classes, TerraBlender toggles, and GTCEu fallback systems.
- Follow AGENTS.md for setup/build/format/validation workflow (tooling commands, branch discipline, parallel-claim etiquette).
- After each milestone, append a new entry to PROGRESS_PROMPTS.md (prompt + outcome + reference) so future agents can resume seamlessly.
