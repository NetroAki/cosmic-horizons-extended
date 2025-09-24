"""Unit tests for scripts/validate_json.py helpers."""

from __future__ import annotations

import sys
from pathlib import Path
import unittest

# Allow importing the validate_json script as a module.
SCRIPT_DIR = Path(__file__).resolve().parents[1] / "scripts"
if str(SCRIPT_DIR) not in sys.path:
    sys.path.insert(0, str(SCRIPT_DIR))

import validate_json  # type: ignore  # noqa: E402


class ShouldSkipDirectoryTests(unittest.TestCase):
    def test_skips_exact_matches(self) -> None:
        self.assertTrue(validate_json.should_skip_directory(Path("/tmp/project/build")))
        self.assertTrue(validate_json.should_skip_directory(Path("/tmp/project/.gradle")))
        self.assertTrue(validate_json.should_skip_directory(Path("/tmp/project/run")))

    def test_skips_prefixed_matches(self) -> None:
        path = Path("/tmp/project/spotless-prettier-node-modules-abcdef")
        self.assertTrue(validate_json.should_skip_directory(path))

    def test_allows_similar_names(self) -> None:
        self.assertFalse(validate_json.should_skip_directory(Path("/tmp/project/builders")))
        self.assertFalse(validate_json.should_skip_directory(Path("/tmp/project/runes")))
        self.assertFalse(validate_json.should_skip_directory(Path("/tmp/project/generated-content")))


if __name__ == "__main__":
    unittest.main()
