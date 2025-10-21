# T-033 Arrakis World Features

**Goal**

- Implement Arrakis terrain features (dune ripple noise, spice geysers, sandworm tunnels, polar ice formations, storm crystal shards).

**Scope**

- Feature code/JSONs, placement configuration, integration into biomes.

**Acceptance**

- Features spawn in intended biomes without errors.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add configured/placed features
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_features.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
