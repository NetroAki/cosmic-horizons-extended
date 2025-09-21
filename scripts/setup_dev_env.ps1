<#
.SYNOPSIS
    Bootstraps a local Forge 1.20.1 development environment for Cosmic Horizons Extended (CHEX).

.DESCRIPTION
    Performs common first-run tasks: validates Java and Gradle availability, optionally warms the Forge MDK cache,
    copies default CHEX configs into the run directory, highlights key documentation, and installs the Spotless
    pre-commit hook if requested. All actions support a dry-run mode for previewing changes.

.PARAMETER SkipGradle
    Skip Gradle warm-up tasks (gradlew --stop, gradlew genIntellijRuns).

.PARAMETER SkipConfigs
    Skip copying default configuration files into the run/config/chex directory.

.PARAMETER InstallHooks
    Copy scripts/git-hooks/pre-commit into .git/hooks/pre-commit.

.PARAMETER DryRun
    Print intended actions without modifying the filesystem.

.EXAMPLE
    ./scripts/setup_dev_env.ps1 -InstallHooks
#>

[CmdletBinding()]
param(
    [switch]$SkipGradle,
    [switch]$SkipConfigs,
    [switch]$InstallHooks,
    [switch]$DryRun
)

function Write-Section {
    param([string]$Message)
    Write-Host "`n=== $Message ===" -ForegroundColor Cyan
}

function Invoke-Step {
    param(
        [scriptblock]$Action,
        [string]$Description
    )

    if ($DryRun) {
        Write-Host "[dry-run] $Description" -ForegroundColor Yellow
        return
    }

    Write-Host $Description -ForegroundColor Green
    & $Action
    if ($LASTEXITCODE -ne 0) {
        throw "Step failed: $Description"
    }
}

try {
    $repoRoot = Resolve-Path -LiteralPath (Join-Path $PSScriptRoot '..')
    Write-Host "CHEX setup script running from $repoRoot" -ForegroundColor Gray
    Set-Location $repoRoot

    Write-Section "Environment Checks"
    $javaCmd = Get-Command java -ErrorAction SilentlyContinue
    if (-not $javaCmd) {
        throw "Java executable not found on PATH. Install Temurin 17 or ensure java is accessible."
    }
    $javaVersion = (& java -version 2>&1 | Select-Object -First 1)
    Write-Host "Java detected: $javaVersion" -ForegroundColor Gray

    if (-not (Test-Path './gradlew')) {
        throw "Gradle wrapper (gradlew) is missing. Ensure the repository clone is complete."
    }

    if (-not $SkipGradle) {
        Write-Section "Preloading Forge Resources"
        Invoke-Step -Description "Running ./gradlew --stop" -Action { ./gradlew --stop | Out-Null }
        Invoke-Step -Description "Running ./gradlew genIntellijRuns" -Action { ./gradlew genIntellijRuns }
    } else {
        Write-Host "Skipping Gradle warm-up (SkipGradle set)." -ForegroundColor Yellow
    }

    if (-not $SkipConfigs) {
        Write-Section "Copying Default Configs"
        $configSource = Join-Path $repoRoot 'forge/config'
        $configTarget = Join-Path $repoRoot 'run/config/chex'

        if (-not (Test-Path $configSource)) {
            Write-Host "Config source $configSource not found; skipping copy." -ForegroundColor Yellow
        } else {
            if ($DryRun) {
                Write-Host "[dry-run] Ensure $configTarget exists" -ForegroundColor Yellow
            } elseif (-not (Test-Path $configTarget)) {
                New-Item -ItemType Directory -Path $configTarget -Force | Out-Null
                Write-Host "Created config target directory: $configTarget" -ForegroundColor Gray
            }

            Get-ChildItem -Path $configSource -Filter '*.json5' -Recurse -File | ForEach-Object {
                $relative = $_.FullName.Substring($configSource.Length).TrimStart('\','/')
                $dest = Join-Path $configTarget $relative
                $destDir = Split-Path $dest -Parent
                if ($DryRun) {
                    Write-Host "[dry-run] Copy $($_.FullName) -> $dest" -ForegroundColor Yellow
                } else {
                    if (-not (Test-Path $destDir)) {
                        New-Item -ItemType Directory -Path $destDir -Force | Out-Null
                    }
                    Copy-Item -Path $_.FullName -Destination $dest -Force
                }
            }

            if (-not $DryRun) {
                Write-Host "Default configs copied to $configTarget" -ForegroundColor Green
            }
        }
    } else {
        Write-Host "Skipping config copy (SkipConfigs set)." -ForegroundColor Yellow
    }

    Write-Section "Documentation Shortcuts"
    $docPaths = @(
        'PROJECT_CONTEXT.md',
        'CHEX_DETAILED_TASKS.md',
        'PROGRESS_PROMPTS.md',
        'TB_STRATEGY.md',
        'notes',
        'progress',
        'Checklist'
    )

    foreach ($doc in $docPaths) {
        $full = Join-Path $repoRoot $doc
        if (Test-Path $full) {
            Write-Host "- $doc" -ForegroundColor Gray
        } else {
            Write-Host "- Missing: $doc" -ForegroundColor Yellow
        }
    }

    if ($InstallHooks) {
        Write-Section "Installing Git Hooks"
        $hookSource = Join-Path $repoRoot 'scripts/git-hooks/pre-commit'
        $hookDest = Join-Path $repoRoot '.git/hooks/pre-commit'

        if (-not (Test-Path $hookSource)) {
            Write-Host "Pre-commit hook template not found at $hookSource" -ForegroundColor Yellow
        } else {
            if ($DryRun) {
                Write-Host "[dry-run] Copy $hookSource -> $hookDest" -ForegroundColor Yellow
            } else {
                Copy-Item -Path $hookSource -Destination $hookDest -Force
                Write-Host "Installed pre-commit hook. Ensure it is executable on POSIX systems." -ForegroundColor Green
            }
        }
    }

    Write-Section "Next Actions"
    Write-Host "1. Import the project into IntelliJ IDEA (dependencies cached via Gradle)." -ForegroundColor Gray
    Write-Host "2. Use generated Forge run configs (genIntellijRuns) or rerun ./gradlew genIntellijRuns if needed." -ForegroundColor Gray
    Write-Host "3. Review notes/ and progress/ directories before starting new tasks." -ForegroundColor Gray

    Write-Host "Setup complete." -ForegroundColor Cyan
}
catch {
    Write-Error $_
    if (-not $DryRun) {
        exit 1
    }
}
