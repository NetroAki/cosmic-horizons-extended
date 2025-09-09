# Cosmic Horizons Extended (CHEX) – Full Implementation Checklist (Forge-only, MC 1.20.1)

## 0) Project Skeleton & Build

- [x] **Environment**: Java 17; **Minecraft 1.20.1**; **Forge** (matching Cosmic Horizons version).
- [x] **Gradle**:
  - [x] `build.gradle` / `gradle.properties` set Java 17 toolchain.
  - [x] Dependencies (mark soft where possible):
    - [x] `cosmichorizons` (namespace observed: `cosmos`)
    - [x] `cosmosunbound`
    - [x] `gtceu` (GregTech CEu; optional)
    - [x] `jei` (optional, for recipe/guide visibility)
  - [x] Use `ModList.get().isLoaded("modid")` gates for every integration call. Never hard-crash if a dep is missing.
- [x] **mods.toml**:
  - [x] Mod id e.g. `chex` (Cosmic Horizons Extended).
  - [x] Load order: after `cosmichorizons`, `cosmosunbound`, `gtceu` when present.
  - [x] Display name, version, license, homepage.
- [x] **Package layout** (example):
  ```
  com.netroaki.chex
    CHEXForge.java                // @Mod entry
    config/
      CHEXCommonConfig.java       // TOML
      PlanetConfigLoader.java     // JSON5
      MineralsConfigLoader.java   // JSON5
      FallbackOresConfig.java     // JSON5
    registry/
      RocketTier.java             // T1..T10 enum
      PlanetDef.java              // record (dim key, name, tier, suit, flags)
      PlanetRegistry.java         // discover+override
      FuelRegistry.java           // fallback fluids+buckets
      SuitItems.java              // 5 suit sets
    travel/
      TravelGraph.java            // tier -> allowed planets
    hooks/
      LaunchHooks.java            // fuel/tier checks
      DimensionHooks.java         // suit checks
    progress/
      PlayerTierCapability.java   // capability + packets
      RecipeConditionTier.java    // custom recipe condition (optional strategy)
      RocketWorkshop/…            // or custom workstation (optional strategy)
    datagen/
      CHEXDataGen.java
      MineralsDataGen.java        // features + placed + biome modifiers
      PlanetDataGen.java          // (optional) template dimensions/biomes
    command/
      CHEXCommands.java
    network/
      CHEXNet.java + packets
  resources/
    META-INF/mods.toml
    assets/chex/lang/en_us.json
    data/chex/** (dimensions, biomes, features, loot_modifiers, recipes, tags, biome_modifiers)
  ```

---

## 1) Enumerate Existing Cosmic Horizons Planets (runtime discovery)

- [ ] At dedicated server start:
  - [ ] Enumerate **dimensions** in namespaces `cosmos` and `cosmichorizons` (use `BuiltInRegistries.DIMENSION_TYPE`/`LevelStem` or datapack scan).
  - [ ] Build a `PlanetDef` per discovered dimension with **default**: `requiredRocketTier = 1`, `requiredSuit = chex:suit1` (to be overridden by config).
  - [ ] **Persist** discovered list to JSON: `config/chex/_discovered_planets.json`.
  - [ ] **Log** a clean table: id, name(if known), default tier, suit requirement, source=CH.
- [ ] **Verify presence** (based on 0.0.7.3 JAR):
  - [ ] `cosmos:earth_moon`
  - [ ] `cosmos:mercury_wasteland`
  - [ ] `cosmos:venuslands`
  - [ ] `cosmos:marslands`
  - [ ] `cosmos:jupiterlands`
  - [ ] `cosmos:europa_lands`
  - [ ] `cosmos:saturn_lands`
  - [ ] `cosmos:uranus_lands`
  - [ ] `cosmos:neptune_lands`
  - [ ] `cosmos:plutowastelands`
  - [ ] `cosmos:alpha_system`
  - [ ] `cosmos:b_1400_centauri`
  - [ ] `cosmos:j_1407blands`
  - [ ] `cosmos:j_1900`
  - [ ] `cosmos:glacio_lands`
  - [ ] `cosmos:gaia_bh_1`
  - [ ] `cosmos:solar_system` (meta/hub)
- [ ] Missing IDs produce WARN lines; no crash.

---

## 2) Add New CHEX Planets (datapack-first)

- [ ] Provide **dimension JSONs** under `data/chex/dimension/*.json` for at least:
  - [ ] `chex:leo` (Low Earth Orbit)
  - [ ] `chex:moon` (if you want a separate surface from CH's)
  - [ ] `chex:mars_orbit`
  - [ ] `chex:phobos`, `chex:deimos`
  - [ ] `chex:venus_platforms`
  - [ ] `chex:ceres`, `chex:asteroids`
  - [ ] `chex:europa`, `chex:titan`, `chex:ganymede` (if not relying solely on CH's)
  - [ ] `chex:triton`, `chex:enceladus`
  - [ ] `chex:exoplanet_alpha`, `chex:exoplanet_beta`, `chex:exoplanet_gamma`
- [ ] Each dimension references **1–3 minimal biomes** in `data/chex/worldgen/biome/…` (rocky, icy, regolith variants):
  - [ ] Set `has_skylight=true`, `fixed_time` for stylized day/night as needed.
  - [ ] Use `multi_noise` biome source templates (valid JSON).
- [ ] **Biome tags**: `data/chex/tags/worldgen/biome/*.json`
  - [ ] `#chex:moon_all`, `#chex:mars_all`, `#chex:venus_all`, etc., for ore injection targeting.
- [ ] **Placed/Configured features** placeholder (empty) to be filled by Mineral DataGen.
- [ ] **Biome modifiers** present and loadable (Forge).

---

## 3) Rocket Tiers & Travel Graph

- [ ] `RocketTier` enum: **T1..T10**, each with `level`, default fuel key.
- [ ] `TravelGraph`:
  - [ ] Config-driven map `tier -> Set<ResourceLocation planetId>`.
  - [ ] Default graph includes **all CH planets** + new CHEX planets, distributed across tiers (T1: LEO, T2: Moon, T3: Mars orbit, T4: Mars surface/Phobos/Deimos, … T10: exoplanets/black hole).
  - [ ] Validate graph at startup; log unknown planetIds; skip gracefully.
- [ ] `/chex dumpPlanets` prints: planetId, source(CH/CHEX), tier, required suit, reachable (Y/N) for current player tier.

---

## 4) Player Progression (no Quests)

- [ ] **Capability** `PlayerTierCapability`:
  - [ ] Fields: `rocketTier (int 1..10)`, `milestones (BitSet or EnumSet)`.
  - [ ] Default: tier 1.
  - [ ] Serialize NBT; sync to client on login/change; server authoritative.
- [ ] **Commands** (server-op):
  - [ ] `/chex getTier [player]`
  - [ ] `/chex setTier <player> <1..10>`
  - [ ] `/chex simulateTier <1..10>` (self only; dev toggle)
  - [ ] `/chex dumpPlanets` (prints + writes JSON snapshot to `run/chex_planets_dump.json`)

---

## 5) Fuel System (GTCEu optional)

- [ ] **Config `chex-common.toml`**:
  - [ ] `fuelMapping.T1..T10 = <fluid_id>`
  - [ ] `fuelVolumePerLaunch.T1..T10 = <mB>`
  - [ ] `acceptHigherTierFuel = true/false` (allow better fuel on lower tiers?)
- [ ] **Fallback fluids** (register if GTCEu absent):
  - [ ] `chex:kerosene`, `chex:rp1`, `chex:lox`, `chex:lh2`, `chex:dt_mix`, `chex:he3_blend`, `chex:exotic_mix`
  - [ ] Buckets, lang, simple icons.
- [ ] **Fuel validation**:
  - [ ] If CH exposes launch/fuel API → integrate.
        _If not_:
    - [ ] Provide **LaunchPad block** + **RocketItem** wrapper or intercept CH's launch GUI with an **addon launch button**.
    - [ ] On launch attempt: check **fluid stacks** in rocket or LaunchPad inventory/tanks against tier mapping; check volume; consume on success.
  - [ ] Clear on-screen failure reasons (wrong fuel, insufficient fuel, unrecognized tank, etc.).
  - [ ] Server authoritative; client gets denial packet + toast.

---

## 6) Suit Tiers & Environmental Gates

- [ ] **Items**: 5 suits (helm, chest, legs, boots) or 5 chestpieces minimum.
  - [ ] Suit I → V; attach `suit_tier` via item capability / data component / NBT.
  - [ ] Tooltip shows tier and supported environments.
  - [ ] Recipes:
    - [ ] Use GTCEu materials if loaded (Kevlar/Aramids/Ceramics/Superconductors).
    - [ ] Vanilla fallback recipes if GTCEu absent (balanced with extra steps).
- [ ] **Planet requirements**:
  - [ ] In `chex-planets.json5`, each planet has `requiredSuitTier` (1..5).
  - [ ] Add optional planet flags: `vacuum`, `acid`, `radiation`, `cryogenic`.
  - [ ] Future: per-flag suit resistances (booleans) to mix-and-match beyond flat tier.
- [ ] **DimensionHooks** (`PlayerEvent.PlayerChangedDimensionEvent`):
  - [ ] If target ∈ CH/CHEX planets:
    - [ ] Compute player's **effective suit tier** (sum or min piece tier); or require full set.
    - [ ] Compare to planet requirement.
    - [ ] If under-tier: **block entry** (preferred) and send reason; or apply **stacking damage/debuff** per config.
  - [ ] Option `bounceBack`: return to source dimension/pos if denied.

---

## 7) Minerals on Every Planet (Core Requirement)

- [ ] **Config** `config/chex/chex-minerals.json5`:
  ```json5
  {
    "cosmos:earth_moon": {
      rocketTier: 2,
      mineralTiers: [
        {
          gtTier: "MV",
          distributions: [
            {
              tag: "gtceu:ores/ilmenite",
              vein: "patch",
              count: 6,
              minY: -16,
              maxY: 48,
              biomes: ["#chex:moon_all"],
            },
            {
              tag: "gtceu:ores/titanium",
              vein: "small",
              count: 4,
              minY: -32,
              maxY: 40,
            },
          ],
        },
      ],
    },
    "cosmos:marslands": {
      rocketTier: 4,
      mineralTiers: [
        {
          gtTier: "HV",
          distributions: [
            {
              tag: "gtceu:ores/tungsten",
              vein: "large",
              count: 3,
              minY: -48,
              maxY: 8,
            },
            {
              tag: "gtceu:ores/molybdenum",
              vein: "medium",
              count: 4,
              minY: -32,
              maxY: 24,
            },
          ],
        },
      ],
    },
  }
  ```
- [ ] **Fallback map** `fallback_ores.json5` (when GTCEu absent):
  ```json5
  {
    "gtceu:ores/titanium": "chex:fallback_titanium_ore",
    "gtceu:ores/tungsten": "chex:fallback_tungsten_ore",
  }
  ```
- [ ] **Data generation**:
  - [ ] For each `distributions[]`, generate:
    - [ ] `configured_feature` (ore) using target **tag** or **id**.
    - [ ] `placed_feature` with `count`, `height_range`.
    - [ ] **Biome modifiers** to add features to `biomes` (ids or `#tags`).
  - [ ] If surface/stone doesn't fit:
    - [ ] Generate **loot modifiers** for special blocks (e.g., regolith, ice) to drop dusts/ores by chance.
    - [ ] Optionally generate **sifting**/processing recipes (vanilla crafting or custom machine block).
- [ ] **Validation**:
  - [ ] If GTCEu loaded: verify ore tags exist; else use fallback IDs.
  - [ ] Log a concise summary: planet → (#features generated, #biomes targeted).
  - [ ] `/reload` regenerates _only datapack_ parts (if feasible); otherwise require restart for dimension/biome modifiers.

---

## 8) Recipe/Progression Gating (no KubeJS/Quests)

Choose **one** (or both):

### Strategy A: **Forge Recipe Condition**

- [ ] Implement `RecipeConditionTier` with serializer id `chex:tier_unlocked`.
- [ ] Vanilla JSON recipes for CH/Unbound rockets/parts are **overridden** in `data/chex/recipes/` to include:
  ```json
  "conditions": [
    { "type": "chex:tier_unlocked", "minTier": 3 }
  ]
  ```
- [ ] Condition checks `PlayerTierCapability` for crafter (container menu side).
- [ ] Provide sample overrides for **T1→T4 rockets** and **suit recipes**.
- [ ] Provide a **pack option** to disable vanilla/Unbound recipes entirely and only use CHEX ones.

### Strategy B: **Custom Crafting Block ("Rocket Workshop")**

- [ ] Add **block**, **tile**, **menu**, **screen**:
  - [ ] Inputs: structural frames, GTCEu parts, electronics, fuel systems.
  - [ ] Enforce **tier**, **required fuel components**, **part counts**.
  - [ ] On craft, consume parts; set rocket's **tier** NBT; optional durability/maintenance stats.
- [ ] Deny craft with labeled missing requirements (red text list).

---

## 9) Launch & Target Enforcement

- [ ] **LaunchHooks**:
  - [ ] If CH exposes a **launch event** → subscribe. Else, require using CHEX **LaunchPad** to start launches.
  - [ ] On launch:
    - [ ] Determine **target planet** (from UI selection or pad setup).
    - [ ] Get **player tier**; verify `TravelGraph.allowed(playerTier, target)`.
    - [ ] Validate **fuel id** matches tier mapping; check **mB** ≥ required; consume.
    - [ ] Optional: validate **rocket mass/cargo** vs tier for balance.
    - [ ] On fail, **cancel** and show explicit reason(s).
  - [ ] On success: proceed with CH teleport / CHEX teleport call.
- [ ] **DimensionHooks** (entry):
  - [ ] Verify **suit tier** (and flags vs planet hazards).
  - [ ] On fail: cancel & bounce; or debuff/damage per config. Always message cause.

---

## 10) Config Surfaces & Hot-Reload

- [ ] `chex-common.toml`:
  - [ ] Fuel mapping & volumes (T1..T10)
  - [ ] Suit enforcement mode: `BLOCK|DEBUFF|DAMAGE`
  - [ ] Messages toggles; accept higher-tier fuels; cargo multipliers
- [ ] `chex-planets.json5`:
  - [ ] Override for any planetId: `displayName`, `requiredRocketTier`, `requiredSuitTier`, `hazardFlags`, `groupTags`
- [ ] `chex-minerals.json5`, `fallback_ores.json5`
- [ ] `chex-travel.json5`:
  - [ ] Tier → planetId[] unlocks (if not hard-coded in TOML)
- [ ] On `/reload`: log which configs can reapply without restart (biome modifiers, loot, recipes).

---

## 11) Client-Side UI/UX

- [ ] **Tooltips**:
  - [ ] Suits show `Suit Tier`, hazard resistances.
  - [ ] Rockets show **Required Fuel** & **Tier**.
- [ ] **Toasts/Messages** (localized):
  - [ ] Launch denied: "Required Fuel: X", "Required Rocket Tier: N", "Target locked behind Tier N", "Suit Tier N required: Y/Z present".
- [ ] **JEI categories** (optional):
  - [ ] "Rocket Assembly" recipes (if using Workshop).
  - [ ] "Planet Resources" info pages (planet → key ores/fuels).
- [ ] **Language**: `en_us.json` entries for all denies, items, fluids, commands.

---

## 12) Networking & Save

- [ ] **Packets**:
  - [ ] Sync player tier on login/change.
  - [ ] Launch deny reason to client (structured: code + args).
- [ ] **Saving**:
  - [ ] Capability persists with player; server authoritative.
  - [ ] Config snapshots written on boot to `/run/chex_effective_config.json` for debugging.

---

## 13) Performance & Stability

- [ ] Worldgen:
  - [ ] Avoid excessive `count`/`triangle distribution`; keep per-chunk ore adds reasonable.
  - [ ] Validate Y ranges; ensure features attach only to your planet biomes.
- [ ] Hooks:
  - [ ] **O(1)** checks on launch/entry; **no tick listeners** for enforcement.
- [ ] Guards:
  - [ ] Null-safe registry lookups; skip unknown planet IDs with WARN.
  - [ ] If CH or GTCEu absent, **degrade gracefully**: planets still load (CHEX), fallback ores/fuels used, enforcement still works.

---

## 14) Multiplayer & Security

- [ ] All enforcement **server-side**; do not trust client.
- [ ] Disallow launch/teleport commands to bypass checks (wrap your teleport call with the same validation).
- [ ] Dedicated server test: connect 2 clients; verify desyncs don't occur on denied launch.

---

## 15) QA Scenarios (must pass)

**Tier Gate E2E (with GTCEu installed):**

- [ ] Start at **T1** with Suit 0 → Launch to `chex:leo` succeeds only with **T1 fuel** and volume; Moon blocked (tier fail).
- [ ] Upgrade to **T2** + **Suit I** → Launch to **`cosmos:earth_moon`** succeeds; entering without Suit I denied with message.
- [ ] Moon spawns **Titanium/Ilmenite** per config; verify loot/ore tags.
- [ ] Upgrade to **T4** + **Suit II** → **`cosmos:marslands`** allowed; Mars ores (W/Mo) spawn.
- [ ] **Wrong fuel** at any tier denies launch with explicit message; **insufficient volume** denies with remaining required mB.
- [ ] Attempt **T7** target at **T4** denies with "Target locked behind Tier 7".
- [ ] **Higher-tier fuel on lower rocket**: behavior respects `acceptHigherTierFuel` config.

**GTCEu Missing (fallback mode):**

- [ ] Modpack boots; no missing registry crashes.
- [ ] Fallback ores generate per config mapping on Moon/Mars.
- [ ] Fuels fall back to CHEX fluids; launch works.

**Multiplayer:**

- [ ] Client cannot force launch/teleport; server denial applies with proper UI.

**Configs/Reload:**

- [ ] Edit `chex-minerals.json5` and `/reload` (if supported) updates biome modifiers (or produce "restart required" info).
- [ ] Invalid planet id in configs logs WARN and skips gracefully.

---

## 16) Documentation & Deliverables

- [ ] `README.md`:
  - [ ] Install, config overview, adding new planets, adding minerals.
  - [ ] How to map GT tier → Rocket tier.
  - [ ] Fuel ladder explanation and examples.
- [ ] `TASKS.md`: this checklist.
- [ ] `docs/planets_example.json5`: example overrides for a few planets.
- [ ] `docs/minerals_example.json5`: sample distributions for Moon/Mars/Exoplanet.
- [ ] Changelog: note CH 0.0.7.3 planet list; how discovery works.

---

## 17) Nice-to-Haves (Optional)

- [ ] **Per-planet gravity** via attribute modifiers/effects on entry (jump height, fall damage).
- [ ] **Cargo mass → fuel consumption** multiplier.
- [ ] **Maintenance parts** for rockets (wear over flights).
- [ ] **ISRU blocks** (electrolyze ice → O₂/H₂; enable in-situ refueling).
- [ ] **He-3 regolith** late-game sifting for fusion fuels.

---

### Quick Acceptance Matrix (first tiers)

- **T1 → `chex:leo`**: fuel=kerosene, suit=0; success only with correct fuel+volume.
- **T2 → `cosmos:earth_moon`**: fuel=kerosene+LOX; require Suit I; Moon has Ti/Ilmenite ores active.
- **T3 → `chex:mars_orbit`**: fuel=RP-1/LOX; Suit II optional (orbit safe) if configured.
- **T4 → `cosmos:marslands`**: fuel=cryo LOX; Suit II required; W/Mo ores spawn.

---

If you want, I can collapse this into a `TASKS.md` file and a **ready-to-build repo skeleton** layout you can feed straight into your coding AI.
