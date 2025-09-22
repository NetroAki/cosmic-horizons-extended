# T-092 Stormworld Blocks & Features

**Goal**
- Add Stormworld block/feature set: stormstone + condensed variants, reforming cloud blocks, ion spires, fulgurite glass, charged crystals, metallic hydrogen pools, and lightning rod/charge collector infrastructure.

**Scope**
- Block registrations/assets, feature configs, biome modifier wiring for each layer (Upper Atmosphere → cloud blocks, Storm Bands → ion spires, Lightning Fields → fulgurite/charged crystals, Depths → metallic hydrogen pools).
- Interaction logic (cloud block regeneration, charged crystal power bursts) as described in design doc.

**Acceptance**
- Features generate in intended layers with density per design doc (ion spires in storm bands, fulgurite/charged crystals in lightning fields, metallic hydrogen pools in depths).
- Interactive behaviour (cloud block regen, charge collectors capturing lightning) validated; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks/features + assets
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_features.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
