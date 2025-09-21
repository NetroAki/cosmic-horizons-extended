# Cosmic Horizons Discovery Pipeline Notes

- Discovery snapshot now written at server start (PlanetDiscovery.discoverAndWrite) and immediately fed back into PlanetRegistry.reloadDiscoveredPlanets() so runtime data matches the on-disk JSON.
- PlanetRegistry maintains DISCOVERED_PLANET_IDS to clear previous Cosmic Horizons entries before loading a new snapshot.
- Registry logs a formatted table (Planet ID | Name | Tier | Suit Tag | Source) for quick verification; uses defaults from discovery JSON (Tier T1, suit chex:suits/suit1, fuel from CHEXConfig).
- Static fallback entries trimmed to core trio (earth_moon, mercury_wasteland, marslands) and marked as discovered records so they are replaced once discovery succeeds.
- Reload helper exposes PlanetRegistry.reloadDiscoveredPlanets() for other systems (used by discovery step, available for future /chex reload).
