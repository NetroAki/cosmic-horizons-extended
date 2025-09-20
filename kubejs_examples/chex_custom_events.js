// CHEX Custom Events for KubeJS
// This file shows how to listen to and respond to CHEX events

// =============================================================================
// CUSTOM CHEX EVENTS
// =============================================================================

// Event: Player unlocks a rocket tier
ServerEvents.custom('chex:rocket_tier_unlocked', event => {
    const player = event.player;
    const tier = event.tier;
    const previousTier = event.previousTier;
    
    console.log(`Player ${player.name} unlocked rocket tier ${tier} (was ${previousTier})`);
    
    // Send congratulations message
    player.tell(`§aCongratulations! You've unlocked Rocket Tier ${tier}!`);
    
    // Grant tier-specific rewards
    const rewards = {
        1: ['cosmic_horizons_extended:basic_rocket_engine'],
        2: ['cosmic_horizons_extended:advanced_rocket_engine'],
        3: ['cosmic_horizons_extended:heavy_rocket_engine'],
        4: ['cosmic_horizons_extended:exotic_rocket_engine'],
        5: ['cosmic_horizons_extended:ultimate_rocket_engine']
    };
    
    const tierRewards = rewards[tier] || [];
    tierRewards.forEach(itemId => {
        player.give(itemId);
    });
    
    // Unlock related planets
    const planetUnlocks = {
        1: ['cosmos:earth_moon'],
        2: ['cosmos:mercury', 'cosmos:venus'],
        3: ['cosmos:mars', 'cosmic_horizons_extended:pandora_forest'],
        4: ['cosmic_horizons_extended:arrakis_desert', 'cosmos:jupiter'],
        5: ['cosmic_horizons_extended:aqua_mundus', 'cosmos:europa']
    };
    
    const planetsToUnlock = planetUnlocks[tier] || [];
    planetsToUnlock.forEach(planetId => {
        if (PlayerTier.unlockPlanet(player, planetId)) {
            player.tell(`§aYou have discovered ${planetId}!`);
        }
    });
});

// Event: Player unlocks a suit tier
ServerEvents.custom('chex:suit_tier_unlocked', event => {
    const player = event.player;
    const tier = event.tier;
    const previousTier = event.previousTier;
    
    console.log(`Player ${player.name} unlocked suit tier ${tier} (was ${previousTier})`);
    
    // Send congratulations message
    player.tell(`§aCongratulations! You've unlocked Suit Tier ${tier}!`);
    
    // Apply tier-specific effects
    const effects = {
        1: { effect: 'minecraft:water_breathing', duration: 6000, amplifier: 0 },
        2: { effect: 'minecraft:fire_resistance', duration: 6000, amplifier: 0 },
        3: { effect: 'minecraft:resistance', duration: 6000, amplifier: 0 },
        4: { effect: 'minecraft:regeneration', duration: 6000, amplifier: 0 },
        5: { effect: 'minecraft:absorption', duration: 6000, amplifier: 0 }
    };
    
    const tierEffect = effects[tier];
    if (tierEffect) {
        player.potionEffects.add(tierEffect.effect, tierEffect.duration, tierEffect.amplifier);
    }
    
    // Grant suit items
    const suitItems = {
        1: ['cosmic_horizons_extended:suit_i_helmet', 'cosmic_horizons_extended:suit_i_chestplate', 'cosmic_horizons_extended:suit_i_leggings', 'cosmic_horizons_extended:suit_i_boots'],
        2: ['cosmic_horizons_extended:suit_ii_helmet', 'cosmic_horizons_extended:suit_ii_chestplate', 'cosmic_horizons_extended:suit_ii_leggings', 'cosmic_horizons_extended:suit_ii_boots'],
        3: ['cosmic_horizons_extended:suit_iii_helmet', 'cosmic_horizons_extended:suit_iii_chestplate', 'cosmic_horizons_extended:suit_iii_leggings', 'cosmic_horizons_extended:suit_iii_boots'],
        4: ['cosmic_horizons_extended:suit_iv_helmet', 'cosmic_horizons_extended:suit_iv_chestplate', 'cosmic_horizons_extended:suit_iv_leggings', 'cosmic_horizons_extended:suit_iv_boots'],
        5: ['cosmic_horizons_extended:suit_v_helmet', 'cosmic_horizons_extended:suit_v_chestplate', 'cosmic_horizons_extended:suit_v_leggings', 'cosmic_horizons_extended:suit_v_boots']
    };
    
    const tierSuitItems = suitItems[tier] || [];
    tierSuitItems.forEach(itemId => {
        player.give(itemId);
    });
});

// Event: Player discovers a planet
ServerEvents.custom('chex:planet_discovered', event => {
    const player = event.player;
    const planetId = event.planetId;
    const discoveryMethod = event.discoveryMethod; // 'crafting', 'exploration', 'quest', etc.
    
    console.log(`Player ${player.name} discovered planet ${planetId} via ${discoveryMethod}`);
    
    // Send discovery message
    player.tell(`§aYou have discovered ${planetId}!`);
    
    // Grant discovery rewards
    const discoveryRewards = {
        'cosmos:earth_moon': ['cosmic_horizons_extended:moon_rock', 'cosmic_horizons_extended:moon_dust'],
        'cosmos:mars': ['cosmic_horizons_extended:mars_rock', 'cosmic_horizons_extended:mars_dust'],
        'cosmic_horizons_extended:pandora_forest': ['cosmic_horizons_extended:pandora_seed', 'cosmic_horizons_extended:floating_stone'],
        'cosmic_horizons_extended:arrakis_desert': ['cosmic_horizons_extended:spice', 'cosmic_horizons_extended:sandworm_scale']
    };
    
    const rewards = discoveryRewards[planetId] || [];
    rewards.forEach(itemId => {
        player.give(itemId);
    });
    
    // Unlock related minerals
    const mineralUnlocks = {
        'cosmos:earth_moon': ['ilmenite', 'titanium', 'lithium'],
        'cosmos:mars': ['tungsten', 'molybdenum', 'chromium'],
        'cosmic_horizons_extended:pandora_forest': ['titanium', 'vanadium', 'lithium'],
        'cosmic_horizons_extended:arrakis_desert': ['tungsten', 'molybdenum', 'chromium', 'magnesium']
    };
    
    const mineralsToUnlock = mineralUnlocks[planetId] || [];
    mineralsToUnlock.forEach(mineralId => {
        if (PlayerTier.unlockMineral(player, mineralId)) {
            player.tell(`§aYou have discovered ${mineralId}!`);
        }
    });
});

// Event: Player discovers a mineral
ServerEvents.custom('chex:mineral_discovered', event => {
    const player = event.player;
    const mineralId = event.mineralId;
    const discoveryMethod = event.discoveryMethod; // 'mining', 'crafting', 'exploration', etc.
    
    console.log(`Player ${player.name} discovered mineral ${mineralId} via ${discoveryMethod}`);
    
    // Send discovery message
    player.tell(`§aYou have discovered ${mineralId}!`);
    
    // Grant discovery rewards
    const mineralRewards = {
        'ilmenite': ['cosmic_horizons_extended:ilmenite_sample'],
        'titanium': ['cosmic_horizons_extended:titanium_sample'],
        'tungsten': ['cosmic_horizons_extended:tungsten_sample'],
        'platinum': ['cosmic_horizons_extended:platinum_sample'],
        'draconium': ['cosmic_horizons_extended:draconium_sample']
    };
    
    const rewards = mineralRewards[mineralId] || [];
    rewards.forEach(itemId => {
        player.give(itemId);
    });
});

// Event: Player attempts to launch a rocket
ServerEvents.custom('chex:rocket_launch_attempt', event => {
    const player = event.player;
    const targetPlanet = event.targetPlanet;
    const rocketTier = event.rocketTier;
    
    console.log(`Player ${player.name} attempting to launch to ${targetPlanet} with T${rocketTier} rocket`);
    
    // Check if player has discovered the planet
    if (!PlayerTier.hasDiscoveredPlanet(player, targetPlanet)) {
        player.tell('§cYou haven\'t discovered this planet yet!');
        event.cancel();
        return;
    }
    
    // Check if player has the required rocket tier
    const requiredTier = PlayerTier.getRequiredRocketTier(targetPlanet);
    if (rocketTier < requiredTier) {
        player.tell(`§cYou need a T${requiredTier} rocket to reach ${targetPlanet}!`);
        event.cancel();
        return;
    }
    
    // Check if player has the required suit tier
    const requiredSuitTier = PlayerTier.getRequiredSuitTier(targetPlanet);
    const playerSuitTier = PlayerTier.getSuitTier(player);
    if (playerSuitTier < requiredSuitTier) {
        player.tell(`§cYou need Suit Tier ${requiredSuitTier} to survive on ${targetPlanet}!`);
        event.cancel();
        return;
    }
    
    // Check fuel requirements
    const fuelReq = calculateFuelRequirement(targetPlanet, rocketTier);
    if (!hasEnoughFuel(player, fuelReq)) {
        player.tell(`§cInsufficient fuel! You need ${fuelReq.amount} ${fuelReq.type} to reach ${targetPlanet}.`);
        event.cancel();
        return;
    }
    
    // Launch is valid, proceed
    player.tell(`§aLaunch sequence initiated! Preparing to travel to ${targetPlanet}...`);
});

// Event: Player successfully launches a rocket
ServerEvents.custom('chex:rocket_launch_success', event => {
    const player = event.player;
    const targetPlanet = event.targetPlanet;
    const rocketTier = event.rocketTier;
    const fuelConsumed = event.fuelConsumed;
    
    console.log(`Player ${player.name} successfully launched to ${targetPlanet} with T${rocketTier} rocket`);
    
    // Send success message
    player.tell(`§aLaunch successful! You are now traveling to ${targetPlanet}.`);
    
    // Consume fuel
    consumeFuel(player, fuelConsumed);
    
    // Apply launch effects
    player.potionEffects.add('minecraft:levitation', 100, 0);
    player.potionEffects.add('minecraft:slow_falling', 200, 0);
    
    // Grant launch rewards
    const launchRewards = {
        'cosmos:earth_moon': ['cosmic_horizons_extended:moon_rock'],
        'cosmos:mars': ['cosmic_horizons_extended:mars_rock'],
        'cosmic_horizons_extended:pandora_forest': ['cosmic_horizons_extended:pandora_seed'],
        'cosmic_horizons_extended:arrakis_desert': ['cosmic_horizons_extended:spice']
    };
    
    const rewards = launchRewards[targetPlanet] || [];
    rewards.forEach(itemId => {
        player.give(itemId);
    });
});

// Event: Player fails to launch a rocket
ServerEvents.custom('chex:rocket_launch_failure', event => {
    const player = event.player;
    const targetPlanet = event.targetPlanet;
    const failureReason = event.failureReason;
    
    console.log(`Player ${player.name} failed to launch to ${targetPlanet}: ${failureReason}`);
    
    // Send failure message
    player.tell(`§cLaunch failed: ${failureReason}`);
    
    // Apply failure effects
    player.potionEffects.add('minecraft:slowness', 200, 0);
    player.potionEffects.add('minecraft:weakness', 200, 0);
    
    // Damage player slightly
    player.hurt('magic', 2);
});

// Helper functions
function calculateFuelRequirement(planet, rocketTier) {
    const baseConsumption = {
        'cosmos:earth_moon': 1000,
        'cosmos:mars': 2000,
        'cosmic_horizons_extended:pandora_forest': 3000,
        'cosmic_horizons_extended:arrakis_desert': 4000
    };
    
    const base = baseConsumption[planet] || 1000;
    const tierMultiplier = 1 + (rocketTier - 1) * 0.1; // 10% reduction per tier
    
    return {
        type: 'chex:kerosene',
        amount: Math.floor(base / tierMultiplier)
    };
}

function hasEnoughFuel(player, fuelReq) {
    // Check if player has enough fuel in inventory or nearby tanks
    // This would need to be implemented based on your fuel system
    return true; // Placeholder
}

function consumeFuel(player, fuelReq) {
    // Consume fuel from player inventory or nearby tanks
    // This would need to be implemented based on your fuel system
    console.log(`Consuming ${fuelReq.amount} ${fuelReq.type} for ${player.name}`);
}
