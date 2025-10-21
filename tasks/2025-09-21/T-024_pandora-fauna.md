# T-024 Pandora Fauna Implementation

**Goal**

- Implement Pandora fauna entities (glowbeast, sporeflies, sky grazer, cliff hunter) with AI behaviours and drops.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures (placeholders acceptable with TODOs).
- Loot tables, drops integration.

**Acceptance**

- Entities spawn in intended biomes with defined behaviours.
- Drops feed into progression (phosphor hides, biolume extract, light membranes).
- `./gradlew check` and client run (manual) confirm no crashes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entity classes + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_fauna.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
