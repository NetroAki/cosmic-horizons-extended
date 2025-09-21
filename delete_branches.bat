@echo off
echo Deleting branches T-001 to T-005...

git checkout master

echo Deleting local branches...
git branch -D feature/T-001_planet-def-alignment
git branch -D feature/T-002_fuel-bucket-registry
git branch -D feature/T-003_terrablender-constants
git branch -D feature/T-004_json-validation-tool
git branch -D feature/T-005_planet-registry-overrides

echo Deleting remote branches...
git push origin --delete feature/T-001_planet-def-alignment
git push origin --delete feature/T-002_fuel-bucket-registry
git push origin --delete feature/T-003_terrablender-constants
git push origin --delete feature/T-004_json-validation-tool
git push origin --delete feature/T-005_planet-registry-overrides

echo Done!
