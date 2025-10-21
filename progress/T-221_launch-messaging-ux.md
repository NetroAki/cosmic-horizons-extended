# T-221 Launch Messaging UX — Completed

Summary

- Localized launch denial and success feedback.
  - Updated `LaunchDenyMessage` to use translatable Components and added DESTINATION reason.
  - Added lang keys for toast title and reasons in `assets/cosmic_horizons_extended/lang/en_us.json`.
- The server now sends clear reasons via `LaunchHooks` (fuel, tier, suit, discovery, destination lock). Client displays chat line + toast.

Verification

- Messages appear localized; destination lock for Neutron Forge shows proper toast.
- Acceptance satisfied: toasts/messages with reasons are displayed.
