# Cosmic Horizons — Addon Support (Consolidated Guide)

> A single, consolidated reference for datapack authors targeting **Cosmic Horizons**.  

- Source sections:
  - Getting started
  - Cosmic data files
  - Solar Systems
  - Planets
  - Dimensions
  - Skyboxes
  - Sky data
  - Fancy JSON text

---

## Table of contents

1. [Getting started](#getting-started)
2. [Cosmic data files](#cosmic-data-files)
3. [Solar Systems](#solar-systems)
4. [Planets](#planets)
5. [Dimensions](#dimensions)
6. [Skyboxes](#skyboxes)
7. [Sky data](#sky-data)
8. [Fancy JSON text](#fancy-json-text)
9. [Appendix: Minimal templates](#appendix-minimal-templates)

---

## Getting started

**Wildcard keys convention.** In the docs, a JSON key shown with a trailing underscore means the actual key name can be **anything** (the underscore itself is optional).

**Example** — the following schema:

```text
"key": int
"key_data": int
"keyname_": int
```

may be written as (choose any real key name for the wildcard entry):

```json
{
  "key": 1,
  "key_data": 2,
  "my_key": 3
}
```

or

```json
{
  "key": 1,
  "key_data": 2,
  "the_fancy_key": 3
}
```

_The rest of this guide assumes you know this convention._

---

## Cosmic data files

Most datapack features originate from **Cosmic Data** JSON files under the `cosmos:cosmic_data` folder of your datapack.

**Folder layout (typical):**

```
data/
  <your_namespace>/
    (other stuff)/
  cosmos/
    cosmic_data/
      solar_system_1.json
      solar_system_2.json
      planet_1.json
      planet_2.json
      overworld.json
      other_dimension.json
```

All of these are **cosmic dimension data files** used in two common ways:

1) **Solar System files** — define systems, GUI, the space-dimension container, and list planets + their target dimensions.  
2) **Planet/Dimension files** — configure a single dimension (gravity, air resistance, custom sky, optional “space” transfer, fog/weather overrides, etc.).

Each file **must include** `"attached_dimention_id"` (typo preserved from the game’s key), pointing to the Minecraft **dimension id** it controls.

**What can each file type include?**

| Feature / Section      | Solar systems | Planets | Dimensions |
|------------------------|:-------------:|:-------:|:----------:|
| `attached_dimention_id`|      ☑        |   ☑     |     ☑      |
| `planet_data`          |      ☑        |   ☐     |     ☐      |
| `gui_data`             |      ☑        |   ☐     |     ☐      |
| `skybox_data`          |      ☐        |   ☑     |     ☑      |
| `dimensional_data`     |      ☑        |   ☑     |     ☑      |
| `sky_data`             |      ☐        |   ☑     |     ☑      |

**Example – minimal Solar System file:**

```json
{
  "planet_data": {
    /* see Planets section */
  },
  "attached_dimention_id": "cosmos:solar_sys_d",
  "local_id": "Solar Sys.",
  "skybox_data": {
    /* see Skyboxes */
  },
  "dimensional_data": {
    "dimension_type": "space",
    "weather": false,
    "clouds": false,
    "sky_objects": false,
    "gravity": 0,
    "air_resistance": 1
  },
  "gui_data": {
    "solar_sys": {
      "object_data": {
        /* GUI planet objects */
      },
      "travel_dimension": "cosmos:solar_sys_d",
      "origin_x": -24100,
      "origin_y": 1000,
      "origin_z": 5100,
      "unlocking_dimension": "none",
      "background": "solar_bg",
      "title": "Solar Sys.",
      "order": -4
    }
  }
}
```

**Example – minimal Planet/Dimension file:**

```json
{
  "attached_dimention_id": "cosmos:marslands",
  "skybox_data": { /* optional skybox */ },
  "dimensional_data": {
    "dimension_type": "planet",
    "weather": false,
    "clouds": false,
    "sky_objects": false,
    "gravity": 38,
    "air_resistance": 0.98,
    "atmospheric_data": {
      "atmosphere_y": 560,
      "travel_to": "cosmos:solar_sys_d",
      "origin_x": -41000,
      "origin_y": 860,
      "origin_z": 18000,
      "overlay_texture_id": "mars_bar",
      "shipbit_y": 24,
      "ship_min_y": 120
    }
  },
  "sky_data": {
    /* see Sky data */
  }
}
```

---

## Solar Systems

**Location:** `cosmos:cosmic_data/<file>.json` (see “Cosmic data files”).

### Top-level attributes

- `"planet_data"` — JSON object with **planets** (see [Planets](#planets)).  
- `"attached_dimention_id"` — `namespace:dimension` id for the **space** dimension representing this system.  
- `"skybox_data"` — skybox settings (see [Skyboxes](#skyboxes)).  
- `"dimensional_data"` — dimension settings for the **space** dimension (see [Dimensions](#dimensions)).  
- `"gui_data"` — config for the **space GUI** tabs and objects (below).

### `gui_data`

- `"category_"` — Object that creates a **GUI tab** (commonly used for multi‑star systems). Contains:
  - `"travel_dimension"`: dimension id to teleport to via GUI (usually same as attached).
  - `"origin_x"`, `"origin_y"`, `"origin_z"`: spawn coords when entering.
  - `"unlocking_dimension"`: dimension id needed to unlock travel.
  - `"background"`: texture name in `assets/cosmos/textures` (no extension).
  - `"title"`: tab title.
  - `"order"`: tab sort order (global across all systems/packs).
  - `"object_data"`: dict of named **GUI planet objects**.

### GUI Planet object (`object_data` → `<name>`)

- `"texture_id"` (texture in `assets/cosmos/textures/`)
- `"scale"`: mini‑planet size in list (≈15)
- `"ponder_scale"`: size in the detailed viewer (≈60)
- `"yaw"`, `"pitch"`, `"roll"`: orientation
- `"yaw_speed"`, `"pitch_speed"`, `"roll_speed"`: rotation speeds (often 0→1)
- `"travel_x"`, `"travel_y"`, `"travel_z"`: fast travel target coords (in space dim; near planet, not inside)
- `"unlocking_dimension"`: dimension required to unlock fast travel
- Display fields that use **Fancy JSON text** (see [Fancy JSON text](#fancy-json-text)): `"atmosphere"`, `"name"`, `"type"`, `"conditions"`, `"size"`, `"category"`
- `"life"`: float `0..1` (UI detail only; no gameplay effect)
- `"ringed"`: boolean; if true, include `"ring_data"`

**Rings in GUI**

```json
"ring_data": {
  "ring1": { "texture_id": "jb1", "scale_radius": 0.675 },
  "ring2": { "texture_id": "jb2", "scale_radius": 1.5 }
}
```

---

## Planets

`planet_data` objects are usually **embedded in a Solar System file** under `"planet_data"`.

### Basic transform & scale

- `xpos`, `ypos`, `zpos`: planet position
- `yaw`, `pitch`, `roll`: orientation
- `scale`: visual size

### Visuals

- `texture_id`: texture in `assets/cosmos/textures/`
- `glowing`: boolean — if **true**, include:
  - `core_color`: `{ "r":0..255, "g":0..255, "b":0..255 }`
  - `bloom_color`: `{ "r":0..255, "g":0..255, "b":0..255 }`
  - `layer`: integer (detail level for glow; ~64 recommended)

### Rings

- `ringed`: boolean — if **true**, include `"ring_data"` with any number of named rings:

```json
"ring_data": {
  "ring1": {
    "texture_id": "sat",
    "radius": 10,
    "scale_radius": 0.04,
    "flip_y": true,
    "flip_x": false,
    "flip_z": false
  }
}
```

> **Notes**
> - `radius`: distance from planet center (used in distance calcs).
> - `scale_radius`: ring size scalar (individual, not `planetRadius + scale`).
> - `flip_x/y/z`: choose the axis(es) that match the ring’s tilt. Multiple flips allowed.

### Linking to gameplay dimension

- `attached_dimension_id`: the dimension to **teleport** to when travelling to this planet.
- `skybox_data`: optional (see [Skyboxes](#skyboxes)).

---

## Dimensions

Use inside cosmic data files (either **space** system files or **planet/dimension** files) to set dimension behavior.

### Attributes

- `"dimension_type"`: `"space"` or `"planet"`
- `"sky_objects"`: bool — hide vanilla stars/sun/moon if false (default true)
- `"weather"`: bool — vanilla rain/snow on/off (default true)
- `"clouds"`: bool — clouds on/off (default true)
- `"gravity"`: number — % of Earth gravity (`0.1` means 10% **or** `10` means 10%); default `100`
- `"air_resistance"`: `0..1` — **1 = no friction** (space‑like), **0 = max friction** (immobile); default `0`

#### `fog_data` (optional)

```json
"fog_data": {
  "color": { "r":255, "g":255, "b":255 },
  "level": 0.1
}
```

- `color`: RGB 0–255
- `level`: density (vanilla ≈ 0.1)

#### `weather_data` (optional; requires `"weather": false`)

```json
"weather_data": {
  "condition": "rain|snow|none",
  "texture_id": "my_weather_tex",
  "speed": 0.5,
  "sound_generic": "block.snow.fall",
  "sound_special": "block.snow.fall",
  "power": 3,
  "hurt": false,
  "damage": 0
}
```

#### `atmospheric_data` (optional; for planet ascent to space)

```json
"atmospheric_data": {
  "atmosphere_y": 560,
  "travel_to": "cosmos:solar_sys_d",
  "origin_x": -24100,
  "origin_y": 1000,
  "origin_z": 5100,
  "overlay_texture_id": "earth_bar",
  "shipbit_y": 24,
  "ship_min_y": 120
}
```

---

## Skyboxes

`skybox_data` defines a textured sky dome + optional sunlight color overrides. Commonly used in **Solar System** or **Planet** files.

### Attributes

- `"attached_skybox_texture_id"`: texture in `assets/cosmos/textures/` (no extension)
- `"yaw"`, `"pitch"`, `"roll"`: orientation of the skybox
- `"alpha"`: `0..255` transparency (0 = fully fog, 255 = fully skybox)
- `"rotation_plane"`: `"yaw"|"pitch"|"roll"` (optional) — which axis the skybox spins around
- `"fade"`: `"day"|"night"` (optional) — when skybox fades
- `"vanilla_sunlight"`: boolean — if false, set `"sunlight_color"`:

```json
"sunlight_color": { "r": 255, "g": 255, "b": 255, "alpha": 255 }
```

---

## Sky data

Define **objects** and **rings** to render in a custom sky for a planet/dimension. The object order matters: **first = furthest** (rendered behind later entries).

### Common

- Keys are arbitrary object names (use as many as you like).

### Objects (`"type": "object"`)

- `"phased"`: boolean — apply lunar‑style phases (rotates object by 60° per phase)
- `"object_yaw"`, `"object_pitch"`, `"object_roll"`: local orientation
- `"yaw"`, `"pitch"`, `"roll"`: initial position around parent
- `"yaw_speed"`, `"pitch_speed"`, `"roll_speed"`: orbital speeds
- `"scale"`: float — size; vanilla sun ≈ `0.14`
- `"texture_id"`: texture in `assets/cosmos/...`  
  If omitted, the object is treated as a **star** and requires:
  - `"core_color"` and `"bloom_color"` (RGB 0–255). See **Planets → Glowing**.
- `"ring_data"`: optional ring(s) attached to this object (same format as Planet rings).

### Rings (`"type": "ring"`)

- `"yaw"`, `"pitch"`, `"roll"`: orientation around parent
- `"ring_data"`:
  - `"texture_id"`
  - `"additive"`: boolean — holographic look (useful for atmospheric rings)
  - `"scale_radius"`: float — typical values around `0.05`

---

## Fancy JSON text

Some UI fields use a **simple** (non‑vanilla) JSON text object with exactly **two keys**:

```json
{ "text": "Your label", "color": "orange" }
```

- These **cannot** be combined as a list to multi‑color a single field.
- `"color"` is a lowercase **enum** value from this list:  
  `red, orange, yellow, green, lime, cyan, light blue, blue, magenta, purple, pink, brown, gray, light gray, black, white`

**Invalid (multi-part list)**
```json
"name": [
  {"text": "red ", "color": "red"},
  {"text": "blue", "color": "blue"}
]
```

**Valid (single object)**
```json
"name": { "text": "Only red", "color": "red" }
```

---

## Appendix: Minimal templates

**Solar system (safest starting point)**
```json
{
  "attached_dimention_id": "cosmos:my_space_dim",
  "dimensional_data": { "dimension_type": "space", "gravity": 0, "air_resistance": 1, "weather": false, "clouds": false, "sky_objects": false },
  "skybox_data": { "attached_skybox_texture_id": "stars_01", "alpha": 255, "yaw": 0, "pitch": 0, "roll": 0 },
  "gui_data": {
    "main": {
      "title": "My System",
      "background": "solar_bg",
      "order": 0,
      "travel_dimension": "cosmos:my_space_dim",
      "origin_x": 0, "origin_y": 100, "origin_z": 0,
      "object_data": { }
    }
  },
  "planet_data": { }
}
```

**Planet (single dimension)**
```json
{
  "attached_dimention_id": "my_pack:my_planet_dim",
  "skybox_data": { "attached_skybox_texture_id": "my_sky", "alpha": 255, "yaw": 0, "pitch": 0, "roll": 0 },
  "dimensional_data": {
    "dimension_type": "planet",
    "gravity": 80,
    "air_resistance": 0.95,
    "sky_objects": false,
    "atmospheric_data": {
      "atmosphere_y": 512,
      "travel_to": "cosmos:my_space_dim",
      "origin_x": 0, "origin_y": 128, "origin_z": 0,
      "overlay_texture_id": "my_bar",
      "shipbit_y": 24,
      "ship_min_y": 120
    }
  },
  "sky_data": { }
}
```

**GUI Planet object (for `gui_data.object_data`)**
```json
{
  "texture_id": "p_tex",
  "scale": 15,
  "ponder_scale": 60,
  "yaw": 0, "pitch": 0, "roll": 0,
  "yaw_speed": 0.25, "pitch_speed": 0, "roll_speed": 0,
  "travel_x": 0, "travel_y": 256, "travel_z": 0,
  "unlocking_dimension": "my_pack:my_planet_dim",
  "name": { "text": "Planet X", "color": "cyan" },
  "type": { "text": "Rocky", "color": "brown" },
  "conditions": { "text": "Windy", "color": "magenta" },
  "size": { "text": "Dwarf", "color": "lime" },
  "category": { "text": "Satellite", "color": "light gray" },
  "ringed": false,
  "life": 0.0
}
```

---

**Tip:** Prefer small, valid slices first. Load the pack, watch logs for key errors, then expand.
