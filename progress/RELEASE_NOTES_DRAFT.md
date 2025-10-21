# Cosmic Horizons Extended — Release Notes (Draft)

Version: 0.1.0 (2025-09-28)

Dependencies

- Minecraft Forge 1.20.1, Java 17
- GregTech CEu (hard dependency)
- Cosmic Horizons strongly recommended

Highlights

- Cross-planet systems: Boss Controller + Core Matrix, Suit Hazards (JSON5), minerals runtime reload.
- Launch validation with cargo/fuel-quality/destination gating; localized toasts.
- Client UX: tooltips, JEI categories (Rocket Assembly, Planet Resources).
- Ambience cues and visual filters per-dimension.
- Docs: README refresh, sample configs, project context updates, changelog.

Installation

- Place CHEX jar, CH, and GTCEu into mods/. Launch Forge 1.20.1.
- Verify localization and JEI categories load.

Configuration Surfaces

- `config/chex-planets.json5` — per-planet overrides
- `config/chex-minerals.json5` — mineral distributions
- `data/cosmic_horizons_extended/config/chex-suit-hazards.json5`
- `data/cosmic_horizons_extended/config/chex-visual-filters.json5`

Verification Checklist (abridged)

- `/chex minerals reload` succeeds
- Launch denial shows localized reason
- JEI categories present
- Suit hazards mitigate per tier
- Visual filters apply in Dyson/Neutron Forge

Known Notes

- GTCEu is required; fallback ore processing is intentionally omitted.
