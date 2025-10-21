# Repeatable Task Execution Prompt

## Primary Prompt (Use this for each task execution)

```
@TASK_TRACKING.md

**TASK EXECUTION INSTRUCTIONS:**

1. **IDENTIFY NEXT TASK**: Find the first uncompleted task (marked with `- [ ]`) in the TASK_TRACKING.md file
2. **VERIFY COMPLETION**: Check if the task is actually complete by examining the codebase and implementation
3. **EXECUTE TASK**: If incomplete, implement the task according to its specifications
4. **VALIDATE COMPLETION**: Ensure the task meets all acceptance criteria before marking complete
5. **UPDATE TRACKING**: Mark the task as complete (`- [x]`) with implementation details in TASK_TRACKING.md
6. **PROVIDE SUMMARY**: Give a brief summary of what was accomplished

**COMPLETION CRITERIA:**
- Task must be fully implemented and functional
- All acceptance criteria from the task file must be met
- Code must compile and pass basic validation
- Implementation must integrate properly with existing systems
- Task file must exist in `tasks/2025-09-21/` directory

**DO NOT MARK COMPLETE UNLESS:**
- The task is actually implemented in the codebase
- All requirements are satisfied
- The implementation is tested and functional
- Integration with existing systems is verified

**RESPONSE FORMAT:**
- Start with "NEXT TASK: [Task ID and Name]"
- Provide implementation details if task was completed
- End with "TASK COMPLETE: [Task ID] - [Brief summary]"
- Update the TASK_TRACKING.md file with completion status
```

## Alternative Prompt (If you want more specific guidance)

```
@TASK_TRACKING.md

**COSMIC HORIZONS EXPANDED - TASK EXECUTION**

**STEP 1: LOCATE NEXT TASK**
- Scan TASK_TRACKING.md for the first `- [ ]` (incomplete) task
- Identify the corresponding task file in `tasks/2025-09-21/`
- Read the task file to understand requirements

**STEP 2: ASSESS CURRENT STATE**
- Check if the task is already implemented in the codebase
- Verify if any partial implementation exists
- Determine what needs to be done

**STEP 3: IMPLEMENT TASK**
- Follow the task file's checklist and acceptance criteria
- Implement according to the project's architecture
- Ensure integration with existing CHEX systems
- Test the implementation

**STEP 4: VALIDATE COMPLETION**
- Verify all acceptance criteria are met
- Check that code compiles (`./gradlew check`)
- Ensure proper integration with existing systems
- Test functionality if applicable

**STEP 5: UPDATE TRACKING**
- Mark task as complete: `- [x]` in TASK_TRACKING.md
- Add implementation details and notes
- Update progress counters if applicable

**PROJECT CONTEXT:**
- This is a Minecraft mod (Cosmic Horizons Expanded)
- Uses Architectury framework (Forge/Fabric)
- Integrates with GregTech CEu
- Focus on planet implementation and progression systems
- Current focus: Arrakis implementation (mostly complete)

**RESPONSE FORMAT:**
```

NEXT TASK: [Task ID] - [Task Name]
STATUS: [Incomplete/Already Complete/Implemented]
ACTION: [What was done]
COMPLETION: [Task ID] - [Brief summary]

```

**CRITICAL: Only mark complete if the task is actually implemented and functional in the codebase.**
```

## Usage Instructions

1. **Copy the Primary Prompt** above and paste it as your message
2. **The AI will:**

   - Find the next incomplete task
   - Check if it's already implemented
   - Implement it if needed
   - Mark it complete only when actually done
   - Provide a summary

3. **Repeat this process** for each subsequent task
4. **The AI will work through tasks systematically** until all are complete

## Key Improvements

- **Specific to this project**: References the actual file structure and task system
- **Clear completion criteria**: Defines what "complete" means
- **Validation steps**: Ensures tasks are actually done before marking complete
- **Consistent format**: Standardized response format for easy tracking
- **Project context**: Includes relevant information about the mod structure
- **Repeatable**: Can be used over and over for systematic task completion

## Example Expected Response

```
NEXT TASK: T-219 - Arrakis Dimension Setup
STATUS: Incomplete - Dimension JSON not created
ACTION: Created dimension JSON file with desert world configuration, implemented sandstorm weather controller, added hydration drain mechanics
COMPLETION: T-219 - Arrakis dimension setup complete with desert world mechanics
```

This prompt ensures systematic, thorough task completion while maintaining project context and quality standards.
