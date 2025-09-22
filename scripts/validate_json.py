#!/usr/bin/env python3
"""JSON validation script for Cosmic Horizons Expanded.

This script recursively scans the repository for .json files and validates their syntax.
It can also handle .json5 files if the json5 package is installed.

Usage:
    python scripts/validate_json.py [directory]

If no directory is specified, it will scan the current directory.

Excludes:
    - .gradle/
    - .idea/
    - build/
    - run/
    - .git/
    - **/generated/
"""

import json
import os
import sys
from pathlib import Path
from typing import List, Tuple, Optional, Set
from dataclasses import dataclass

@dataclass
class ValidationResult:
    """Container for validation results."""
    file_path: Path
    is_valid: bool
    error: Optional[str] = None
    is_json5: bool = False

# Try to import json5, but make it optional
HAS_JSON5 = False
try:
    import json5  # type: ignore
    HAS_JSON5 = True
except ImportError:
    try:
        # Try an alternative import method
        from json5 import loads as json5_loads
        HAS_JSON5 = True
    except ImportError:
        HAS_JSON5 = False

class ValidationError(Exception):
    """Custom exception for validation errors."""
    pass

def validate_json_file(file_path: Path) -> Tuple[bool, Optional[str]]:
    """Validate a single JSON or JSON5 file.
    
    Args:
        file_path: Path to the file to validate
        
    Returns:
        Tuple of (is_valid, error_message)
    """
    try:
        # First try with utf-8-sig to handle BOM
        try:
            with open(file_path, 'r', encoding='utf-8-sig') as f:
                content = f.read()
            
            # Try to parse as JSON first
            try:
                json.loads(content)
                return True, None
            except json.JSONDecodeError as e:
                # If JSON parsing fails and it's a .json5 file, try with json5 if available
                if file_path.suffix.lower() == '.json5':
                    if not HAS_JSON5:
                        return False, f"JSON5 file found but json5 package is not installed. Run 'pip install json5' to enable JSON5 validation."
                    try:
                        json5.loads(content)
                        return True, None
                    except Exception as e:
                        return False, f"Invalid JSON5: {str(e)}"
                else:
                    # If JSON parsing with BOM handling failed, try without BOM handling
                    # to provide a more specific error message
                    with open(file_path, 'r', encoding='utf-8') as f_no_bom:
                        content = f_no_bom.read()
                        json.loads(content)  # This will raise the actual JSON error
                        return True, None
        except UnicodeDecodeError:
            # If utf-8-sig fails, try with default encoding
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                json.loads(content)
                return True, None
    except Exception as e:
        return False, f"Error reading file: {str(e)}"

def is_excluded(path: Path, exclude_dirs: Set[Path]) -> bool:
    """Check if a path should be excluded from validation."""
    try:
        # Skip node_modules directories and their contents
        if 'node_modules' in path.parts:
            return True
            
        # Skip if any parent directory is in exclude_dirs
        for parent in path.parents:
            if parent in exclude_dirs:
                return True
                
        # Skip TypeScript config files with non-strict JSON
        if path.name == 'tsconfig.json' and 'spotless-prettier-node-modules' in str(path):
            return True
            
        return False
    except OSError:
        # Skip if there's an error accessing the path
        return True

def find_json_files(directory: Path) -> List[Path]:
    """Recursively find all .json and .json5 files in a directory."""
    # Define directories to exclude
    exclude_dirs = {
        directory / ".gradle",
        directory / ".idea",
        directory / "build",
        directory / "run",
        directory / ".git",
        directory / "node_modules",  # Explicitly exclude node_modules
    }
    
    # Add generated directories
    for generated_dir in directory.rglob("**/generated"):
        exclude_dirs.add(generated_dir)
    
    json_files = []
    for ext in ('*.json', '*.json5'):
        for file_path in directory.rglob(ext):
            if not is_excluded(file_path, exclude_dirs):
                json_files.append(file_path)
    
    return sorted(json_files)

def validate_files(file_paths: List[Path], root_dir: Path) -> List[ValidationResult]:
    """Validate a list of JSON/JSON5 files."""
    results = []
    for file_path in file_paths:
        is_valid, error = validate_json_file(file_path)
        is_json5 = file_path.suffix.lower() == '.json5'
        results.append(ValidationResult(file_path, is_valid, error, is_json5))
    return results

def print_summary(results: List[ValidationResult], root_dir: Path) -> int:
    """Print validation summary and return exit code."""
    valid = [r for r in results if r.is_valid]
    errors = [r for r in results if not r.is_valid]
    
    # Print errors first
    for result in errors:
        rel_path = result.file_path.relative_to(root_dir)
        print(f"[ERROR] {rel_path}: {result.error}", file=sys.stderr)
    
    # Print summary
    print("\n" + "=" * 80)
    print(f"JSON Validation Summary")
    print("=" * 80)
    print(f"Total files checked: {len(results)}")
    print(f"Valid files: {len(valid)}")
    print(f"Files with errors: {len(errors)}")
    
    if HAS_JSON5:
        json5_files = [r for r in results if r.is_json5]
        print(f"\nJSON5 files: {len(json5_files)}")
    
    return 1 if errors else 0

def main() -> int:
    """Main entry point."""
    # Determine the directory to scan
    if len(sys.argv) > 1:
        root_dir = Path(sys.argv[1]).resolve()
    else:
        root_dir = Path.cwd().resolve()
    
    if not root_dir.exists() or not root_dir.is_dir():
        print(f"Error: Directory not found: {root_dir}", file=sys.stderr)
        return 1
    
    print("=" * 80)
    print(f"JSON Validation Tool - {root_dir}")
    print("=" * 80)
    
    if HAS_JSON5:
        print("JSON5 support: Enabled")
    else:
        print("JSON5 support: Not available (install with 'pip install json5' to validate .json5 files)")
    
    print("\nScanning for JSON files... (this may take a moment)")
    
    # Find and validate all JSON files
    json_files = find_json_files(root_dir)
    if not json_files:
        print("\nNo JSON files found to validate.")
        return 0
    
    print(f"\nFound {len(json_files)} JSON files to validate...\n")
    
    # Validate files
    results = validate_files(json_files, root_dir)
    
    # Print summary and return appropriate exit code
    return print_summary(results, root_dir)

if __name__ == "__main__":
    sys.exit(main())
