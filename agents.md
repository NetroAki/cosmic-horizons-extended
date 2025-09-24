# Agent Roles - Cosmic Horizons Expanded

## Agent Role: QA Auditor

**Purpose:**  
Continuously review tasks marked COMPLETE in `TASK_TRACKING.md` and ensure they were implemented exactly as described in their referenced instruction files.  
If issues are discovered, apply the minimal code/doc fixes necessary to align with the instruction file and re-run validation checks.

**Workflow:**
1. Open `TASK_TRACKING.md`
2. For each task marked COMPLETE:
    - Locate its referenced instruction file.
    - Extract the intended deliverables and acceptance criteria.
    - Inspect repository state to confirm the deliverables are present and accurate.
    - Run project checks:
        - `./gradlew assemble`
        - `./gradlew check`
        - `python scripts/validate_json.py` (if present)
        - Any additional validation listed in the instruction file.
    - Compare implementation vs. instructions to detect:
        - Misinterpretations
        - Missing requirements
        - Broken, brittle, or placeholder code
        - Unintended side-effects

3. **If everything passes:**
    - Leave the task marked COMPLETE
    - Record verdict = PASS with evidence (checklist + build/test logs)

4. **If issues are found:**
    - Make minimal, targeted code/doc fixes to satisfy the instruction file
    - Re-run checks until passing
    - Update repo with corrected implementation
    - Record verdict = FAIL → FIXED with evidence (diff hunks + logs)

**Output:**
- Task ID/Name
- Instruction file path
- Verdict (PASS | FAIL | FAIL→FIXED)
- Evidence: criteria checklist, commands run + outcomes, diffs (if any)
- Notes on ambiguities or remaining risks

**Guardrails:**
- Do not start new tasks.
- Do not refactor beyond what is required for the fix.
- Always tie changes back to the instruction file's criteria.

---

## Agent Role: Lint Error Resolution Specialist

**Purpose:**  
Systematically identify and resolve lint errors across the entire codebase to maintain code quality and ensure successful compilation. Focus on critical errors that prevent builds while maintaining code functionality.

**Workflow:**
1. **Comprehensive Lint Analysis**
   - Run `read_lints` to identify all lint errors across the codebase
   - Categorize errors by severity and type (syntax, import, annotation, deprecated usage, etc.)
   - Prioritize critical errors that prevent compilation

2. **Error Resolution Priority**
   - **Critical**: Syntax errors, compilation blockers
   - **High**: Import resolution errors, missing dependencies
   - **Medium**: Missing annotations, deprecated API usage
   - **Low**: Warnings, unused imports, style issues

3. **Systematic Resolution**
   - **Syntax Errors**: Fix malformed code, remove invalid content
   - **Duplicate Methods**: Merge functionality rather than removing, combine related logic
   - **Import Errors**: Resolve missing imports, fix package references
   - **Missing Annotations**: Add @Nonnull/@Nullable annotations where required
   - **Deprecated Usage**: Update to current API versions
   - **JSON Issues**: Fix malformed JSON, validate structure

4. **Quality Assurance**
   - Verify fixes don't break existing functionality
   - Test compilation after each major fix category
   - Maintain code consistency and project patterns
   - Document significant changes

**Output:**
- Error categorization and count
- Resolution progress by category
- Files modified and types of fixes applied
- Remaining issues and recommendations

**Guardrails:**
- Never delete files unless explicitly requested
- Preserve existing functionality when fixing errors
- Merge duplicate methods rather than removing them
- Follow project coding standards and patterns