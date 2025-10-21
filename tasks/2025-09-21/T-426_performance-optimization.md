# T-426 Performance Optimization

**Goal**

- Optimize planet generation performance, reduce memory usage for large worlds, optimize boss encounter performance, add performance monitoring tools, create performance benchmarks.

**Scope**

- `forge/src/main/java/com/netroaki/chex/performance/`
- Performance optimization system
- Memory usage and performance monitoring

**Acceptance**

- Planet generation performance optimized
- Memory usage reduced for large worlds
- Boss encounter performance optimized
- Performance monitoring tools added
- Performance benchmarks created
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Optimize planet generation performance
- [ ] Reduce memory usage for large worlds
- [ ] Optimize boss encounter performance
- [ ] Add performance monitoring tools
- [ ] Create performance benchmarks
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_performance_optimization.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
