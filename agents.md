# Repository Guidelines

## Project Structure & Module Organization
Cosmic Horizons Expanded splits shared logic in `common/` and Forge-specific loaders in `forge/`. Generated data and assets live under `forge/src/main/resources` with datagen outputs checked in. Command scripts, hooks, and automation sit in `scripts/`, while long-form design notes belong in `notes/`, and chronological updates go in `progress/`. Keep client-dev scaffolding in `dev/` and example packs in `kubejs` or `kubejs_examples`.

## Build, Test, and Development Commands
Run `./gradlew assemble` for a fast compile sanity check; follow with `./gradlew check` before any hand-off. Use `./gradlew :forge:runData` to regenerate resources, then inspect the diff. `python scripts/validate_json.py` catches malformed data. For focused suites, `./gradlew :forge:test` runs Forge-side unit and integration tests.

## Coding Style & Naming Conventions
Java sources use Spotless with the repo default (four-space indent, trailing newline). Apply fixes via `./gradlew :common:spotlessApply :forge:spotlessApply`. Name packages in lowercase dotted form, classes in PascalCase, and registry objects with snake_case resource keys. Keep config JSON compact with double quotes and comment using standard Fabric-style doc blocks when behaviour is non-obvious.

## Testing Guidelines
Co-locate tests beside implementation under `forge/src/test/java`. Name test classes `<Feature>Test` and individual methods `should<Expectation>`. When adding gameplay logic, cover edge cases for GTCEu fallback behaviour and data-driven recipes. Ensure datagen output stays deterministic by re-running `:forge:runData` after adjusting test fixtures.

## Commit & Pull Request Guidelines
Work on branches named `feature/<task-id>` and never rewrite published history. Follow conventional commits (`feat:`, `fix:`, `chore:`) and keep messages imperative. Every PR links the tracked task, lists verification commands (include `./gradlew check`), and notes any warnings or follow-up work. Attach screenshots or logs when altering assets, GUIs, or progression flows.
