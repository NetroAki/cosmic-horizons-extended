# T-200 Mineral Config Alignment — Completed

Summary

- Verified existing parser `common/src/main/java/com/netroaki/chex/config/MineralsConfigCore.java` loads JSON5 with lenient parsing and maps per-planet distributions.
- Reviewed existing configuration at `forge/src/main/resources/data/cosmic_horizons_extended/config/chex-minerals.json5` (GTNH-style per-planet ore mapping).
- Added offline validator script `scripts/validate_minerals.py` to check structural integrity (IDs/tags form, numeric ranges, presence of distributions).

Verification

- Parser present and functional.
- Config file exists and passed structural validation with the new script (JSON5 parsing tolerance implemented; runtime registry validation is out of scope for this script).
- Acceptance satisfied: config aligns with design structure; validator detects structural issues.
