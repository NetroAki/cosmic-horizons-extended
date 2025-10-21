# T-168 Mars GTCEu Integration

**Goal**

- Add Martian-specific ores and terraforming technology to enhance Mars [EXISTING] with GTCEu integration.

**Scope**

- Martian-specific ore generation and processing
- GTCEu integration for terraforming technology
- Processing chains and recipe systems

**Acceptance**

- Martian ores: Unique materials found only on Mars
- Terraforming technology: GTCEu recipes for atmospheric processing
- Integration: Seamless connection between Martian resources and GTCEu technology
- Progression: Martian materials unlock advanced terraforming recipes
- Balance: Appropriate resource distribution and processing requirements
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Martian-specific ore generation
- [ ] Create GTCEu processing chains for Martian materials
- [ ] Add terraforming technology recipes
- [ ] Integrate with GTCEu technology progression
- [ ] Balance resource distribution and requirements
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mars_gtceu_integration.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
