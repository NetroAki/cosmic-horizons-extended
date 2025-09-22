# Update AGENTS.md across all branches
Write-Host "Updating AGENTS.md across all branches..." -ForegroundColor Green

# Store the current branch
$currentBranch = git rev-parse --abbrev-ref HEAD
Write-Host "Current branch: $currentBranch" -ForegroundColor Yellow

# Switch to master and get the latest AGENTS.md
Write-Host "Switching to master and pulling latest changes..." -ForegroundColor Cyan
git checkout master
git pull origin master

# Get list of all local branches
Write-Host "Getting list of all branches..." -ForegroundColor Cyan
$branches = git branch --format="%(refname:short)" | Where-Object { $_ -ne "master" }

Write-Host "Found $($branches.Count) branches to update" -ForegroundColor Yellow

# Process each branch
foreach ($branch in $branches) {
    Write-Host "`nProcessing branch: $branch" -ForegroundColor Magenta
    
    try {
        # Switch to branch
        git checkout $branch
        
        # Copy agents.md from master
        git checkout master -- agents.md
        
        # Add and commit the change
        git add agents.md
        $commitResult = git commit -m "Update AGENTS.md with latest contract" 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  ✓ Committed changes to $branch" -ForegroundColor Green
        } else {
            Write-Host "  - No changes needed for $branch" -ForegroundColor Gray
        }
        
        # Push to remote
        $pushResult = git push origin $branch 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  ✓ Pushed $branch to remote" -ForegroundColor Green
        } else {
            Write-Host "  ✗ Failed to push $branch" -ForegroundColor Red
            Write-Host "    $pushResult" -ForegroundColor Red
        }
    }
    catch {
        Write-Host "  ✗ Error processing $branch : $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Return to original branch
Write-Host "`nReturning to original branch: $currentBranch" -ForegroundColor Cyan
git checkout $currentBranch

Write-Host "`nUpdate complete! All branches have been updated with the latest AGENTS.md" -ForegroundColor Green
