# PowerShell script to push all branches to remote
Write-Host "Pushing all branches to remote..."

$originalBranch = (git rev-parse --abbrev-ref HEAD)
Write-Host "Current branch: $originalBranch"

# Get list of all branches (excluding HEAD, master, and remote branches)
$branches = git branch --format="%(refname:short)" | Where-Object { $_ -ne "master" -and $_ -notlike "origin/*" }
Write-Host "Found $($branches.Count) branches to push"

$successCount = 0
$failCount = 0

foreach ($branch in $branches) {
    Write-Host "`nPushing branch: $branch"
    
    try {
        # Switch to branch
        git checkout $branch
        
        # Push to remote
        $pushResult = git push origin $branch 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  ✓ Successfully pushed $branch" -ForegroundColor Green
            $successCount++
        } else {
            Write-Host "  ❌ Failed to push $branch" -ForegroundColor Red
            Write-Host $pushResult -ForegroundColor Red
            $failCount++
        }
        
    } catch {
        Write-Host "  ❌ An error occurred while pushing $branch" -ForegroundColor Red
        $failCount++
    }
}

# Return to original branch
Write-Host "`nReturning to original branch: $originalBranch"
git checkout $originalBranch

Write-Host "`nPush complete! Successfully pushed $successCount branches, $failCount failed"
