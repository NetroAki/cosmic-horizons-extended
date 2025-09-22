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
BLOCKSTATES_DIR = ASSETS_DIR / MOD_ID / "blockstates"
MODELS_BLOCK_DIR = ASSETS_DIR / MOD_ID / "models/block"
MODELS_ITEM_DIR = ASSETS_DIR / MOD_ID / "models/item"
TEXTURES_BLOCK_DIR = ASSETS_DIR / MOD_ID / "textures/block"
TEXTURES_ITEM_DIR = ASSETS_DIR / MOD_ID / "textures/item"

# Create directories if they don't exist
for directory in [BLOCKSTATES_DIR, MODELS_BLOCK_DIR, MODELS_ITEM_DIR, TEXTURES_BLOCK_DIR, TEXTURES_ITEM_DIR]:
    directory.mkdir(parents=True, exist_ok=True)

# Generate blockstate JSON
def generate_blockstate(fluid_name):
    return {
        "variants": {
            "": {
                "model": f"{MOD_ID}:block/{fluid_name}_block"
            }
        }
    }

# Generate block model JSON
def generate_block_model(fluid_name):
    return {
        "parent": "minecraft:block/cube_all",
        "textures": {
            "all": f"{MOD_ID}:block/{fluid_name}_still"
        }
    }

# Generate bucket model JSON
def generate_bucket_model(fluid_name):
    return {
        "parent": "minecraft:item/generated",
        "textures": {
            "layer0": "minecraft:item/bucket",
            "layer1": f"{MOD_ID}:item/{fluid_name}_bucket"
        },
        "display": {
            "firstperson_righthand": {
                "rotation": [0, -90, 0],
                "translation": [0, 4, 0],
                "scale": [0.5, 0.5, 0.5]
            },
            "firstperson_lefthand": {
                "rotation": [0, 90, 0],
                "translation": [0, 4, 0],
                "scale": [0.5, 0.5, 0.5]
            },
            "thirdperson_righthand": {
                "rotation": [75, 135, 0],
                "translation": [0, 1.25, 0],
                "scale": [0.375, 0.375, 0.375]
            },
            "thirdperson_lefthand": {
                "rotation": [75, 135, 0],
                "translation": [0, 1.25, 0],
                "scale": [0.375, 0.375, 0.375]
            }
        }
    }

# Generate files for each fluid
for fluid in FLUIDS:
    # Blockstate
    blockstate_path = BLOCKSTATES_DIR / f"{fluid}_block.json"
    if not blockstate_path.exists():
        with open(blockstate_path, 'w') as f:
            json.dump(generate_blockstate(fluid), f, indent=2)
        print(f"Generated {blockstate_path}")
    
    # Block model
    block_model_path = MODELS_BLOCK_DIR / f"{fluid}_block.json"
    if not block_model_path.exists():
        with open(block_model_path, 'w') as f:
            json.dump(generate_block_model(fluid), f, indent=2)
        print(f"Generated {block_model_path}")
    
    # Bucket model
    bucket_model_path = MODELS_ITEM_DIR / f"{fluid}_bucket.json"
    if not bucket_model_path.exists():
        with open(bucket_model_path, 'w') as f:
            json.dump(generate_bucket_model(fluid), f, indent=2)
        print(f"Generated {bucket_model_path}")

print("\nNote: You'll need to create the following texture files manually:")
print(f"- {TEXTURES_BLOCK_DIR}/<fluid_name>_still.png")
print(f"- {TEXTURES_BLOCK_DIR}/<fluid_name>_flowing.png")
print(f"- {TEXTURES_ITEM_DIR}/<fluid_name>_bucket.png")
print("\nReplace <fluid_name> with each fluid name from the FLUIDS list in the script.")
