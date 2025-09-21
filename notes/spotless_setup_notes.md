# Spotless Setup Notes (2025-09-21)

- Applied com.diffplug.spotless 6.25.0 plugin to all projects via root build.gradle.
- Java formatter: googleJavaFormat 1.17.0 with import ordering + unused import removal targeting src/\*_/_.java.
- JSON + mcmeta files: Prettier 3.3.2 with json parser; excludes build/.gradle/run/out directories.
- JSON5 files: Prettier 3.3.2 json5 parser to keep config files consistent.
- Markdown: Prettier 3.3.2 markdown parser covering docs/notes.
- check task now depends on spotlessCheck for every module so CI enforces formatting.
- Added scripts/git-hooks/pre-commit invoking ./gradlew spotlessApply (honours SKIP_SPOTLESS=1 to bypass when needed).
