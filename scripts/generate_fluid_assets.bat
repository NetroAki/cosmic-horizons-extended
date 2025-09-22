@echo off
setlocal enabledelayedexpansion

set MOD_ID=chex

set FLUIDS=(
    "kerosene"
    "rp1"
    "lox"
    "lh2"
    "dt_mix"
    "he3_blend"
    "exotic_mix"
)

set ASSETS_DIR=src\main\resources\assets
set BLOCKSTATES_DIR=%ASSETS_DIR%\%MOD_ID%\blockstates
set MODELS_BLOCK_DIR=%ASSETS_DIR%\%MOD_ID%\models\block
set MODELS_ITEM_DIR=%ASSETS_DIR%\%MOD_ID%\models\item

:: Create directories
mkdir "%BLOCKSTATES_DIR%" 2>nul
mkdir "%MODELS_BLOCK_DIR%" 2>nul
mkdir "%MODELS_ITEM_DIR%" 2>nul

for %%f in (%FLUIDS%) do (
    echo Generating files for %%f
    
    :: Blockstate
    echo.{
    echo   "variants": {
    echo     "": {
    echo       "model": "%MOD_ID%:block/%%~f_fluid_block"
    echo     }
    echo   }
    echo.}
    ) > "%BLOCKSTATES_DIR%\%%~f_fluid_block.json"
    
    :: Fluid model
    echo.{
    echo   "parent": "minecraft:block/water",
    echo   "textures": {
    echo     "particle": "%MOD_ID%:block/fluid/%%~f_still"
    echo   }
    echo.}
    ) > "%MODELS_BLOCK_DIR%\%%~f_fluid_block.json"
    
    :: Bucket model
    echo.{
    echo   "parent": "minecraft:item/generated",
    echo   "textures": {
    echo     "layer0": "minecraft:item/bucket"
    echo   },
    echo   "display": {
    echo     "gui": {
    echo       "rotation": [30, 225, 0],
    echo       "translation": [0, 0, 0],
    echo       "scale": [0.6, 0.6, 0.6]
    echo     }
    echo   }
    echo.}
    ) > "%MODELS_ITEM_DIR%\%%~f_bucket.json"
)

echo.
echo Generated fluid assets for %FLUIDS%
