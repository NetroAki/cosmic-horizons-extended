# Step 21 – JSON validation tooling audit

## Summary

- Reviewed T-004 implementation for the JSON validation utility.
- Updated `scripts/validate_json.py` to detect `.json5` files and validate them when the optional `json5` module is available.
- Added graceful skipping and user-facing warnings when `json5` is not installed, ensuring the script still exits successfully.
- Documented the behaviour change in the README tooling section.

## Next Steps

- Install the `json5` package in CI if future tasks require strict `.json5` validation.
- Extend the validator with schema-based checks once configuration formats stabilise.
