#!/bin/bash
set -euo pipefail

echo "Deleting branches T-001 to T-005..."

# Switch to master branch
echo "Switching to master branch..."
git checkout master

# Delete local branches
echo "Deleting local branches..."
git branch -D feature/T-001_planet-def-alignment || echo "T-001 not found locally"
git branch -D feature/T-002_fuel-bucket-registry || echo "T-002 not found locally"
git branch -D feature/T-003_terrablender-constants || echo "T-003 not found locally"
git branch -D feature/T-004_json-validation-tool || echo "T-004 not found locally"
git branch -D feature/T-005_planet-registry-overrides || echo "T-005 not found locally"

# Delete remote branches
echo "Deleting remote branches..."
git push origin --delete feature/T-001_planet-def-alignment || echo "T-001 not found remotely"
git push origin --delete feature/T-002_fuel-bucket-registry || echo "T-002 not found remotely"
git push origin --delete feature/T-003_terrablender-constants || echo "T-003 not found remotely"
git push origin --delete feature/T-004_json-validation-tool || echo "T-004 not found remotely"
git push origin --delete feature/T-005_planet-registry-overrides || echo "T-005 not found remotely"

echo "Done! Checking remaining branches..."
git branch --list | grep -E "T-00[1-5]" || echo "No T-001 to T-005 branches found"
