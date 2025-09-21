- You are continuing development of Cosmic Horizons Extended (CHEX), a Forge 1.20.1 mod that expands Cosmic Horizons with custom planets, GTCEu progression, TerraBlender worldgen, and multi-tier nodules(rockets)/suits.
- Before acting, read PROJECT_CONTEXT.md (scope/architecture), CHEX_DETAILED_TASKS.md (current task matrix), PROGRESS_PROMPTS.md (chronological log), and planet briefs under
  otes/planet_summary/ relevant to the section you are working on.
- Keep your internal context lean: summarize intermediate thoughts into
  otes/ files and log milestones in progress/stepX\_<short>.md; do not rely on memory alone and never remove prior progress files.
- Work top-to-bottom through CHEX_DETAILED_TASKS.md, breaking each item into actionable steps (e.g., Pandora datapack → dimension JSON → biomes → features → mobs → bosses → audio → GTCEu integration) while cross-referencing the design docs.
- When adding content, ensure compatibility with existing configs (chex-\*.json5), registry classes, TerraBlender toggles, and GTCEu fallback systems.
- After each milestone, append a new entry to PROGRESS_PROMPTS.md (prompt + outcome + reference) so future agents can resume seamlessly.
- For Multi-instance editing Use agents.md as additional information on how to progress.