# Step 024 - Pandora Fauna (T-024)

## Status

- Sporeflies implemented (`SporefliesEntity`) with attributes registered in `EntityAttributeEvent`.
- Entity classes exist for Glowbeast and Sky Grazer; attribute hooks present.
- Central registry shows `SPOREFLIES` explicitly; other fauna registry objects may be pending or in alternate registries.
- No entity loot tables found for Pandora fauna; spawn hooks exist (`PandoraMobSpawns`) but namespace check likely mismatched (uses `chex` instead of mod id), so spawns may not trigger.

## Evidence

```21:55:forge/src/main/java/com/netroaki/chex/events/EntityAttributeEvent.java
@SubscribeEvent
public static void registerAttributes(EntityAttributeCreationEvent event) {
  event.put(CHEXEntities.SPOREFLIES.get(), SporefliesEntity.createAttributes().build());
  // ... other fauna attribute registrations present
}
```

```16:25:forge/src/main/java/com/netroaki/chex/registry/entities/CHEXEntities.java
public static final RegistryObject<EntityType<SporefliesEntity>> SPOREFLIES = ENTITY_TYPES.register("sporeflies", ...);
```

```59:71:forge/src/main/java/com/netroaki/chex/world/spawn/PandoraMobSpawns.java
@SubscribeEvent
public static void onBiomeLoading(BiomeLoadingEvent event) {
  // Only process our dimension's biomes
  if (!event.getName().getNamespace().equals("chex")) {
    return; // likely wrong namespace; mod id is cosmic_horizons_extended
  }
  // ... addSpawn logic
}
```

## Commands Run

- `./gradlew check` → SUCCESS (code compiles)

## Verdict

FAIL → FIXED (partial) – Core entity and attributes in place for Sporeflies; remaining for full acceptance:

- Register Glowbeast/Sky Grazer entities in the active registry class.
- Correct spawn namespace and wire biome-based spawns.
- Add basic entity loot tables.

Next: implement the above, then re-run server to validate in-biome spawning and drops.
