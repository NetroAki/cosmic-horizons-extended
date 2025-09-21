# CI Workflow Notes (2025-09-21)

- Created .github/workflows/build.yml to run on push/pull_request.
- Workflow sets up Temurin JDK 17 and leverages gradle/actions/setup-gradle for wrapper + caching.
- Executes ./gradlew check forge:runData --stacktrace to cover unit checks and data generation.
- Added post-step guard that prints git status --short then fails the job if git diff --exit-code detects generated JSON drift.
- No additional secrets or env configuration required beyond default GITHUB_TOKEN.
