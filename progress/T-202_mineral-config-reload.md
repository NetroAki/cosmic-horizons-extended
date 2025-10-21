# T-202 Mineral Config Reload Command — Completed

Summary

- Implemented runtime reload for minerals config using `MineralsRuntime`.
- Added `/chex minerals reload` admin command to reload and report status/errors.

Verification

- Command calls `MineralsRuntime.reload(server)` and returns success/failure with messages.
- Acceptance satisfied: runtime reload without restart, thread-safe holder, status visible.
