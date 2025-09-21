#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 1 ]; then
  echo "Usage: $0 tasks/<date>/T-XXX_slug.md [...more tasks]" >&2
  exit 1
fi

for task in "$@"; do
  if [ ! -f "$task" ]; then
    echo "Task file not found: $task" >&2
    exit 1
  fi
  slug=$(basename "$task" .md)
  branch="feature/${slug}"
  git switch -c "$branch"
  claim_file="claims/${slug}.claim"
  {
    echo "claimed-by: supervisor"
    echo "started: $(date -u +%FT%TZ)"
    echo "source: $task"
  } > "$claim_file"
  git add "$claim_file" "$task"
  git commit -m "chore: claim ${slug}"
  git push -u origin "$branch"
  echo "Spawned branch $branch"
  git switch -
  echo "-----"

done
