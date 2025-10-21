# T-009 Fuel Registry Fallbacks

**Goal**

- Flesh out `FuelRegistry` fallback fluids (kerosene, rp1, lox, lh2, dt_mix, he3_blend, exotic_mix) with textures, lang, and config integration.

**Scope**

- `forge/src/main/java/com/netroaki/chex/registry/FuelRegistry.java`
- Data assets: textures, models, lang entries, tags.
- Config files: ensure fallback behaviour in absence of GTCEu.

**Acceptance**

- Each fallback fuel has bucket item, fluid block, texture/translation.
- Registry gracefully switches between GTCEu and fallback.
- `./gradlew check` passes (include datagen if needed).

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement fallback fluids + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_fuel_registry.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
