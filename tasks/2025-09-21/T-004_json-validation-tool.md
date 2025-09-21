# T-004 Tooling & JSON Validation Script

**Goal**
- Provide the scripts/validate_json.py utility referenced in AGENTS.md and wire it into the workflow (document usage, optional README note).

**Scope**
- scripts/validate_json.py
- Update documentation if needed (e.g., README tooling section).

**Acceptance**
- Script walks the repo (or resources directories) and validates .json files, exiting non-zero on failure.
- Handles missing json5 gracefully (skip or warn) without external dependencies.
- ./gradlew check still passes.
- Usage documented (inline docstring or README snippet).

**Checklist**
- [ ] ash scripts/cloud_bootstrap.sh
- [ ] Implement Python script and test via python scripts/validate_json.py
- [ ] ./gradlew check
- [ ] Add log entry progress/stepXX_tooling.md + update PROGRESS_PROMPTS.md
- [ ] Open PR referencing this task file
