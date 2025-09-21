# Build Alignment Notes (2025-09-21)

- Root build.gradle was on JavaLanguageVersion.of(21); switched to 17 to match Forge toolchains.
- Verified common/build.gradle and forge/build.gradle already target Java 17; no extra changes needed.
- Updated common/gradle.properties loom.platform flag from forge to common to restore Architectury split.
- Gradle wrapper (8.13) already compatible with Java 17; no action required.
- Marked CHEX_DETAILED_TASKS entry complete; follow-up items: CI workflow, Spotless, dev setup script.
