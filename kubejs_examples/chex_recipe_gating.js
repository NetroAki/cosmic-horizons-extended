// CHEX Recipe Gating Examples for KubeJS
// This file shows how to create tier-gated recipes using CHEX's tier system

// Example 1: Rocket recipes gated by rocket tier
ServerEvents.recipes(event => {
    // T2 Rocket - requires T2 rocket tier
    event.shaped('cosmic_horizons_extended:rocket_t2', [
        ' S ',
        'S S', 
        'S S'
    ], {
        S: 'minecraft:iron_ingot'
    }).condition('kubejs:player_has_tier', {
        tier: 2,
        type: 'rocket'
    });
    
    // T3 Rocket - requires T3 rocket tier
    event.shaped('cosmic_horizons_extended:rocket_t3', [
        ' S ',
        'S S',
        'S S'
    ], {
        S: 'minecraft:gold_ingot'
    }).condition('kubejs:player_has_tier', {
        tier: 3,
        type: 'rocket'
    });
    
    // Advanced Rocket Engine - requires T5 rocket tier
    event.shaped('cosmic_horizons_extended:advanced_rocket_engine', [
        ' D ',
        'DED',
        ' D '
    ], {
        D: 'minecraft:diamond',
        E: 'minecraft:redstone'
    }).condition('kubejs:player_has_tier', {
        tier: 5,
        type: 'rocket'
    });
});

// Example 2: Suit recipes gated by suit tier
ServerEvents.recipes(event => {
    // Suit II Helmet - requires T2 suit tier
    event.shaped('cosmic_horizons_extended:suit_ii_helmet', [
        'III',
        'I I',
        '   '
    ], {
        I: 'minecraft:iron_ingot'
    }).condition('kubejs:player_has_tier', {
        tier: 2,
        type: 'suit'
    });
    
    // Suit III Chestplate - requires T3 suit tier
    event.shaped('cosmic_horizons_extended:suit_iii_chestplate', [
        'I I',
        'III',
        'III'
    ], {
        I: 'minecraft:gold_ingot'
    }).condition('kubejs:player_has_tier', {
        tier: 3,
        type: 'suit'
    });
});

// Example 3: Complex recipes with multiple conditions
ServerEvents.recipes(event => {
    // Quantum Fuel Tank - requires T8 rocket tier AND discovered specific planet
    event.shaped('cosmic_horizons_extended:quantum_fuel_tank', [
        'DDD',
        'D D',
        'DDD'
    ], {
        D: 'minecraft:netherite_ingot'
    }).condition('kubejs:player_has_tier', {
        tier: 8,
        type: 'rocket'
    }).condition('kubejs:player_has_discovered_planet', {
        planet: 'cosmic_horizons_extended:exotica'
    });
});

// Example 4: Mineral-based recipes
ServerEvents.recipes(event => {
    // Draconium Alloy - requires discovered draconium mineral
    event.shaped('cosmic_horizons_extended:draconium_alloy', [
        'DDD',
        'D D',
        'DDD'
    ], {
        D: 'gtceu:draconium_ingot'
    }).condition('kubejs:player_has_discovered_mineral', {
        mineral: 'draconium'
    });
});
