# T-213 Launch Validation Enhancements — Completed

Summary

- Extended `LaunchHooks.canLaunch` to include:
  - Destination restriction check (Neutron Forge requires `SOVEREIGN_HEART`).
  - Cargo mass multiplier and fuel quality factor.
  - Fuel validation now accounts for cargo/fuel quality; matching consumption path updated.
- Added clear deny messages via `LaunchDenyMessage` for destination and fuel.

Verification

- Launch denies when boss key missing for Neutron Forge.
- Fuel requirement scales with carried items and tier-based quality heuristic.
- Acceptance satisfied: cargo/fuel/destination considered; feedback provided.
