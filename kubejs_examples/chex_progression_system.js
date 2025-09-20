// CHEX Progression System Examples for KubeJS
// This file shows how to create dynamic progression systems using CHEX's tier system

// =============================================================================
// PLANET DISCOVERY TRIGGERS
// =============================================================================

// Example 1: Unlock planets when crafting specific items
ServerEvents.recipes(event => {
    // Unlock Moon when crafting first rocket
    event.custom({
        type: 'minecraft:crafting_shaped',
        pattern: [' S ', 'S S', 'S S'],
        key: { S: 'minecraft:iron_ingot' },
        result: { item: 'cosmic_horizons_extended:rocket_t1', count: 1 }
    }).then(recipe => {
        // When player crafts this rocket, unlock Moon
        recipe.onCraft((player, item) => {
            if (PlayerTier.unlockPlanet(player, 'cosmos:earth_moon')) {
                player.tell('§aYou have discovered the Moon!');
            }
        });
    });
});

// Example 2: Unlock planets when reaching certain coordinates
ServerEvents.tick(event => {
    if (event.server.ticks % 20 === 0) { // Check every second
        event.server.players.forEach(player => {
            const pos = player.position;
            
            // Unlock Mars when reaching high altitude (space)
            if (pos.y > 200 && !PlayerTier.hasDiscoveredPlanet(player, 'cosmos:mars')) {
                if (PlayerTier.unlockPlanet(player, 'cosmos:mars')) {
                    player.tell('§aYou have discovered Mars from space!');
                }
            }
        });
    }
});

// Example 3: Unlock planets when completing quests
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    // Unlock different planets based on quest completion
    const planetUnlocks = {
        'chex:build_first_rocket': 'cosmos:earth_moon',
        'chex:reach_space': 'cosmos:mars',
        'chex:mine_titanium': 'cosmic_horizons_extended:pandora_forest',
        'chex:craft_advanced_rocket': 'cosmic_horizons_extended:arrakis_desert'
    };
    
    if (planetUnlocks[questId]) {
        if (PlayerTier.unlockPlanet(player, planetUnlocks[questId])) {
            player.tell(`§aYou have discovered ${planetUnlocks[questId]}!`);
        }
    }
});

// =============================================================================
// MINERAL DISCOVERY SYSTEM
// =============================================================================

// Example 1: Discover minerals when mining ores
ServerEvents.blockBreak(event => {
    const player = event.player;
    const block = event.block;
    
    // Discover minerals when mining specific ores
    const mineralDiscoveries = {
        'gtceu:ilmenite_ore': 'ilmenite',
        'gtceu:titanium_ore': 'titanium',
        'gtceu:tungsten_ore': 'tungsten',
        'gtceu:platinum_ore': 'platinum',
        'gtceu:draconium_ore': 'draconium'
    };
    
    const mineral = mineralDiscoveries[block.id];
    if (mineral && !PlayerTier.hasDiscoveredMineral(player, mineral)) {
        if (PlayerTier.unlockMineral(player, mineral)) {
            player.tell(`§aYou have discovered ${mineral}!`);
        }
    }
});

// Example 2: Discover minerals when crafting items
ServerEvents.recipes(event => {
    // Discover lithium when crafting batteries
    event.custom({
        type: 'minecraft:crafting_shaped',
        pattern: [' L ', 'LRL', ' L '],
        key: { 
            L: 'gtceu:lithium_ingot',
            R: 'minecraft:redstone'
        },
        result: { item: 'gtceu:battery', count: 1 }
    }).then(recipe => {
        recipe.onCraft((player, item) => {
            if (PlayerTier.unlockMineral(player, 'lithium')) {
                player.tell('§aYou have discovered lithium!');
            }
        });
    });
});

// =============================================================================
// TIER PROGRESSION REWARDS
// =============================================================================

// Example 1: Grant items when unlocking rocket tiers
ServerEvents.custom('chex:rocket_tier_unlocked', event => {
    const player = event.player;
    const tier = event.tier;
    
    // Grant different rewards based on tier
    const rewards = {
        1: ['cosmic_horizons_extended:basic_suit_helmet', 'cosmic_horizons_extended:basic_suit_chestplate'],
        2: ['cosmic_horizons_extended:advanced_rocket_engine', 'cosmic_horizons_extended:fuel_tank'],
        3: ['cosmic_horizons_extended:heavy_duty_suit', 'cosmic_horizons_extended:oxygen_tank'],
        4: ['cosmic_horizons_extended:exotic_suit', 'cosmic_horizons_extended:quantum_fuel_tank'],
        5: ['cosmic_horizons_extended:ultimate_suit', 'cosmic_horizons_extended:quantum_engine']
    };
    
    const tierRewards = rewards[tier] || [];
    tierRewards.forEach(itemId => {
        player.give(itemId);
    });
    
    player.tell(`§aCongratulations! You've unlocked Rocket Tier ${tier}!`);
});

// Example 2: Apply effects when unlocking suit tiers
ServerEvents.custom('chex:suit_tier_unlocked', event => {
    const player = event.player;
    const tier = event.tier;
    
    // Apply different effects based on suit tier
    const effects = {
        1: { effect: 'minecraft:water_breathing', duration: 6000 },
        2: { effect: 'minecraft:fire_resistance', duration: 6000 },
        3: { effect: 'minecraft:resistance', duration: 6000 },
        4: { effect: 'minecraft:regeneration', duration: 6000 },
        5: { effect: 'minecraft:absorption', duration: 6000 }
    };
    
    const tierEffect = effects[tier];
    if (tierEffect) {
        player.potionEffects.add(tierEffect.effect, tierEffect.duration, 0);
    }
    
    player.tell(`§aCongratulations! You've unlocked Suit Tier ${tier}!`);
});

// =============================================================================
// SUIT ENFORCEMENT LOGIC
// =============================================================================

// Example 1: Custom hazard damage system
ServerEvents.tick(event => {
    if (event.server.ticks % 20 === 0) { // Check every second
        event.server.players.forEach(player => {
            const dimension = player.level.dimension.location.toString();
            
            // Check if player is in a hazardous dimension
            const hazardousDimensions = [
                'cosmos:mars',
                'cosmic_horizons_extended:arrakis_desert',
                'cosmic_horizons_extended:inferno_prime'
            ];
            
            if (hazardousDimensions.includes(dimension)) {
                const suitTier = PlayerTier.getSuitTier(player);
                const requiredTier = PlayerTier.getRequiredSuitTier(dimension);
                
                if (suitTier < requiredTier) {
                    // Apply gradual damage instead of instant death
                    const damage = Math.max(1, requiredTier - suitTier);
                    player.hurt('magic', damage);
                    player.tell(`§cWarning: Insufficient suit protection! (T${suitTier}/${requiredTier})`);
                }
            }
        });
    }
});

// Example 2: Custom environmental effects
ServerEvents.tick(event => {
    if (event.server.ticks % 100 === 0) { // Check every 5 seconds
        event.server.players.forEach(player => {
            const dimension = player.level.dimension.location.toString();
            
            // Apply different effects based on dimension and suit tier
            const environmentalEffects = {
                'cosmos:mars': {
                    effect: 'minecraft:slowness',
                    duration: 200,
                    amplifier: 0
                },
                'cosmic_horizons_extended:arrakis_desert': {
                    effect: 'minecraft:hunger',
                    duration: 200,
                    amplifier: 1
                },
                'cosmic_horizons_extended:inferno_prime': {
                    effect: 'minecraft:wither',
                    duration: 200,
                    amplifier: 0
                }
            };
            
            const envEffect = environmentalEffects[dimension];
            if (envEffect) {
                const suitTier = PlayerTier.getSuitTier(player);
                const requiredTier = PlayerTier.getRequiredSuitTier(dimension);
                
                if (suitTier < requiredTier) {
                    player.potionEffects.add(envEffect.effect, envEffect.duration, envEffect.amplifier);
                }
            }
        });
    }
});

// =============================================================================
// FUEL CONSUMPTION & LAUNCH LOGIC
// =============================================================================

// Example 1: Custom fuel consumption rates
ServerEvents.custom('chex:rocket_launch', event => {
    const player = event.player;
    const targetPlanet = event.targetPlanet;
    const rocketTier = event.rocketTier;
    
    // Calculate fuel consumption based on distance and rocket tier
    const fuelConsumption = calculateFuelConsumption(targetPlanet, rocketTier);
    
    // Check if player has enough fuel
    if (!hasEnoughFuel(player, fuelConsumption)) {
        player.tell('§cInsufficient fuel for launch!');
        event.cancel();
        return;
    }
    
    // Consume fuel
    consumeFuel(player, fuelConsumption);
    
    // Launch success
    player.tell(`§aLaunch successful! Consumed ${fuelConsumption.amount} ${fuelConsumption.type}`);
});

// Example 2: Launch failure conditions
ServerEvents.custom('chex:rocket_launch', event => {
    const player = event.player;
    const targetPlanet = event.targetPlanet;
    
    // Random launch failure chance (5%)
    if (Math.random() < 0.05) {
        player.tell('§cLaunch failed! Rocket malfunction detected.');
        event.cancel();
        return;
    }
    
    // Check weather conditions
    if (player.level.isRaining() && Math.random() < 0.3) {
        player.tell('§cLaunch failed! Weather conditions too dangerous.');
        event.cancel();
        return;
    }
});

// Helper functions
function calculateFuelConsumption(planet, rocketTier) {
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
