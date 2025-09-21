#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd -- "${SCRIPT_DIR}/.." && pwd)"
cd "${REPO_ROOT}"

echo "=== CHEX Cloud Bootstrap ==="

if ! command -v java >/dev/null 2>&1; then
  echo "Java 17 is required (Temurin preferred)." >&2
  exit 1
fi
JAVA_VER=$(java -version 2>&1 | head -n 1)
echo "Java detected: ${JAVA_VER}"

git fetch --all --prune

echo "Gradle wrapper version:"
./gradlew --version

echo "Workspace status:"
git status -sb

echo "Bootstrap complete."
