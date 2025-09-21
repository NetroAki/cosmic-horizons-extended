#!/usr/bin/env bash
set -euo pipefail

# Determine task directory
if [ $# -ge 1 ]; then
  TASK_DIR="$1"
else
  latest=$(ls -1d tasks/*/ 2>/dev/null | sort | tail -n 1)
  if [ -z "$latest" ]; then
    echo "[spawn-all] No task directories found." >&2
    exit 1
  fi
  TASK_DIR="${latest%/}"
fi

if [ ! -d "$TASK_DIR" ]; then
  echo "[spawn-all] Task directory not found: $TASK_DIR" >&2
  exit 1
fi

echo "[spawn-all] Using task directory: $TASK_DIR"

for task in "$TASK_DIR"/T-*.md; do
  [ -f "$task" ] || continue
  slug=$(basename "$task" .md)
  branch="feature/${slug}"

  if git show-ref --verify --quiet "refs/heads/${branch}"; then
    echo "[spawn-all] Local branch exists, skipping ${branch}"
    continue
  fi

  if git ls-remote --exit-code origin "${branch}" >/dev/null 2>&1; then
    echo "[spawn-all] Remote branch exists, skipping ${branch}"
    continue
  fi

  echo "[spawn-all] Creating ${branch}"
  git switch -c "$branch"

  claim="claims/${slug}.claim"
  {
    echo "claimed-by: supervisor"
    echo "started: $(date -u +%FT%TZ)"
    echo "source: $task"
  } > "$claim"

  git add "$claim" "$task"
  git commit -m "chore: claim ${slug}" >/dev/null
  git push -u origin "$branch"
  git switch - >/dev/null
  echo "[spawn-all] Branch ${branch} pushed"
done

echo "[spawn-all] Completed"
