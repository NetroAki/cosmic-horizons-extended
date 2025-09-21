# Step 13 - Spotless Formatting Pipeline (2025-09-21)

- Enabled Spotless across root/common/forge modules with googleJavaFormat + Prettier profiles for Java, JSON/JSON5, and Markdown.
- Hooked spotlessCheck into Gradle check so CI/jobs block on formatting violations.
- Created scripts/git-hooks/pre-commit to run ./gradlew spotlessApply; documented behaviour in notes/spotless_setup_notes.md.
