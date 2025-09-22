import os
import json
from pathlib import Path

# Configuration
MOD_ID = "chex"
FLUIDS = [
    "kerosene",
    "rp1",
    "lox",
    "lh2",
    "dt_mix",
    "he3_blend",
    "exotic_mix"
]

# Paths
ASSETS_DIR = Path("src/main/resources/assets")
MODEL_DIR = ASSETS_DIR / MOD_ID / "models"
BLOCKSTATE_DIR = ASSETS_DIR / MOD_ID / "blockstates"

# Create directories if they don't exist
MODEL_DIR.mkdir(parents=True, exist_ok=True)
BLOCKSTATE_DIR.mkdir(parents=True, exist_ok=True)

# Generate fluid blockstate JSON
def generate_blockstate(fluid_name):
    return {
        "variants": {
            "": {
                "model": f"{MOD_ID}:block/{fluid_name}"
            }
        }
    }

# Generate fluid model JSON
def generate_fluid_model(fluid_name):
    return {
        "parent": "minecraft:block/water",
        "textures": {
            "particle": f"{MOD_ID}:block/fluid/{fluid_name}_still"
        }
    }

# Generate bucket model JSON
def generate_bucket_model(fluid_name):
    return {
        "parent": "minecraft:item/generated",
        "textures": {
            "layer0": "minecraft:item/bucket"
        },
        "display": {
            "gui": {
                "rotation": [30, 225, 0],
                "translation": [0, 0, 0],
                "scale": [0.6, 0.6, 0.6]
            }
        }
    }

# Generate all JSON files
for fluid in FLUIDS:
    # Blockstate
    blockstate_path = BLOCKSTATE_DIR / f"{fluid}_fluid_block.json"
    with open(blockstate_path, 'w') as f:
        json.dump(generate_blockstate(f"{fluid}_fluid_block"), f, indent=2)
    
    # Fluid model
    model_path = MODEL_DIR / "block" / f"{fluid}_fluid_block.json"
    model_path.parent.mkdir(parents=True, exist_ok=True)
    with open(model_path, 'w') as f:
        json.dump(generate_fluid_model(fluid), f, indent=2)
    
    # Bucket model
    bucket_model_path = MODEL_DIR / "item" / f"{fluid}_bucket.json"
    bucket_model_path.parent.mkdir(parents=True, exist_ok=True)
    with open(bucket_model_path, 'w') as f:
        json.dump(generate_bucket_model(fluid), f, indent=2)

print("Generated fluid blockstate and model JSON files.")
