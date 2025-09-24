#!/usr/bin/env python3
"""JSON validation script for Cosmic Horizons Expanded.

Validates all JSON files in the project for syntax and basic structure.
Exits with code 0 if all files are valid, 1 if any file is invalid.
"""

import json
import os
import sys
from pathlib import Path

try:
    import json5  # type: ignore

    JSON5_AVAILABLE = True
except ModuleNotFoundError:  # pragma: no cover - optional dependency
    json5 = None  # type: ignore
    JSON5_AVAILABLE = False

def strip_comments(json_like: str) -> str:
    """Remove ``//`` and ``/* */`` style comments while preserving string contents."""

    result: list[str] = []
    in_string = False
    string_delim = '"'
    in_single_comment = False
    in_multi_comment = False
    escape_next = False
    i = 0
    length = len(json_like)

    while i < length:
        char = json_like[i]
        next_char = json_like[i + 1] if i + 1 < length else ''

        if in_single_comment:
            if char == '\n':
                in_single_comment = False
                result.append(char)
            i += 1
            continue

        if in_multi_comment:
            if char == '\n':
                result.append(char)
                i += 1
                continue

            if char == '\r' and next_char == '\n':
                result.append(char)
                i += 1
                continue

            if char == '*' and next_char == '/':
                in_multi_comment = False
                i += 2
            else:
                i += 1
            continue

        if in_string:
            result.append(char)
            if escape_next:
                escape_next = False
            elif char == '\\':
                escape_next = True
            elif char == string_delim:
                in_string = False
            i += 1
            continue

        if char == '/' and next_char == '/':
            in_single_comment = True
            i += 2
            continue

        if char == '/' and next_char == '*':
            if result and not result[-1].isspace():
                result.append(' ')
            in_multi_comment = True
            i += 2
            continue

        if char in {'"', "'"}:
            in_string = True
            string_delim = char
            result.append(char)
            i += 1
            continue

        result.append(char)
        i += 1

    return ''.join(result)

def is_valid_json(file_path: Path):
    """
    Check if a file contains valid JSON, handling common extensions like:
    - UTF-8 BOM
    - Single-line (//) and block (/* */) comments
    - Single-line and block comments outside of string literals
    """
    try:
        # Try reading with utf-8-sig to handle BOM
        with open(file_path, 'r', encoding='utf-8-sig') as f:
            content = f.read()

        if file_path.suffix.lower() == '.json5':
            if not JSON5_AVAILABLE:
                # Caller should skip when json5 is unavailable, but guard regardless
                return True, None
            json5.loads(content)  # type: ignore[arg-type]
            return True, None

        # Strip comments before validation for vanilla JSON files
        content = strip_comments(content)

        # Try to parse the JSON
        json.loads(content)
        return True, None
        
    except UnicodeDecodeError:
        # Fall back to regular utf-8 if utf-8-sig fails
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                if file_path.suffix.lower() == '.json5':
                    if not JSON5_AVAILABLE:
                        return True, None
                    json5.loads(content)  # type: ignore[arg-type]
                else:
                    content = strip_comments(content)
                    json.loads(content)
            return True, None
        except json.JSONDecodeError as e:
            return False, f"Invalid JSON: {str(e)}"
            
    except json.JSONDecodeError as e:
        return False, f"Invalid JSON: {str(e)}"
        
    except Exception as e:
        return False, f"Error reading file: {str(e)}"

SKIP_DIR_NAMES = {
    'build',
    '.gradle',
    'node_modules',
    '.idea',
    'out',
    'run',
    'generated',
    'bin',
    'buildsrc',
    '.git',
}

SKIP_DIR_PREFIXES = (
    'spotless-prettier-node-modules-',
)


def should_skip_directory(path: Path) -> bool:
    """Check if a directory should be skipped during JSON file search."""

    lower_parts = [part.lower() for part in path.parts]

    for part in lower_parts:
        if part in SKIP_DIR_NAMES:
            return True
        if any(part.startswith(prefix) for prefix in SKIP_DIR_PREFIXES):
            return True

    return False

def find_json_files(directory):
    """Recursively find all JSON files in the given directory, excluding build dirs."""
    json_files = []
    for root, dirs, files in os.walk(directory, topdown=True):
        # Skip unwanted directories
        dirs[:] = [d for d in dirs if not should_skip_directory(Path(root) / d)]

        for file in files:
            if file.endswith(('.json', '.json5')):
                json_files.append(Path(root) / file)
    return sorted(json_files)

def main():
    project_root = Path(__file__).parent.parent
    json_files = find_json_files(project_root)
    
    # Set up console output with proper encoding for Windows
    if sys.platform == 'win32':
        import io
        sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
    
    print(f"Validating {len(json_files)} JSON files in {project_root}")
    
    has_errors = False
    skipped_json5 = []

    for json_file in json_files:
        if json_file.suffix.lower() == '.json5' and not JSON5_AVAILABLE:
            skipped_json5.append(json_file)
            continue

        is_valid, error = is_valid_json(json_file)
        if not is_valid:
            has_errors = True
            rel_path = json_file.relative_to(project_root)
            # Use ASCII-only characters for Windows compatibility
            if sys.platform == 'win32':
                print(f"[ERROR] {rel_path} - Invalid JSON: {error}")
            else:
                print(f"❌ {rel_path} - Invalid JSON: {error}")
    
    if skipped_json5:
        print("\n⚠️  Skipped JSON5 validation for the following files (install the 'json5' package to validate):")
        for skipped in skipped_json5:
            print(f"   - {skipped.relative_to(project_root)}")

    if has_errors:
        if sys.platform == 'win32':
            print("\n[ERROR] Validation failed: Some JSON files are invalid")
        else:
            print("\n❌ Validation failed: Some JSON files are invalid")
        sys.exit(1)
    else:
        if sys.platform == 'win32':
            print("\n[SUCCESS] All JSON files are valid")
        else:
            print("\n✅ All JSON files are valid")
        sys.exit(0)

if __name__ == "__main__":
    main()
