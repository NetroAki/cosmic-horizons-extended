# T-165 Moon GTCEu Integration

**Goal**

- Add lunar-specific materials and processing chains to enhance Moon [EXISTING] with GTCEu integration.

**Scope**

- Lunar-specific ore generation and processing
- GTCEu integration for lunar materials
- Processing chains and recipe systems

**Acceptance**

- Lunar ores: Unique materials found only on the Moon
- Processing chains: GTCEu recipes for lunar material processing
- Integration: Seamless connection between lunar resources and GTCEu technology
- Progression: Lunar materials unlock advanced GTCEu recipes
- Balance: Appropriate resource distribution and processing requirements
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement lunar-specific ore generation
- [ ] Create GTCEu processing chains for lunar materials
- [ ] Add lunar material recipes and processing
- [ ] Integrate with GTCEu technology progression
- [ ] Balance resource distribution and requirements
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_moon_gtceu_integration.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
