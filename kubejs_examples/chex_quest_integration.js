// CHEX Quest Integration Examples for KubeJS
// This file shows how to integrate CHEX progression with quest mods

// =============================================================================
// FTB QUESTS INTEGRATION
// =============================================================================

// Quest: Build your first rocket
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    if (questId === 'chex:build_first_rocket') {
        // Unlock basic rocket tier
        if (PlayerTier.unlockRocketTier(player, 1)) {
            player.tell('§aYou have unlocked Rocket Tier 1!');
        }
        
        // Unlock Moon
        if (PlayerTier.unlockPlanet(player, 'cosmos:earth_moon')) {
            player.tell('§aYou have discovered the Moon!');
        }
        
        // Grant basic suit
        player.give('cosmic_horizons_extended:suit_i_helmet');
        player.give('cosmic_horizons_extended:suit_i_chestplate');
        player.give('cosmic_horizons_extended:suit_i_leggings');
        player.give('cosmic_horizons_extended:suit_i_boots');
    }
});

// Quest: Reach space
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    if (questId === 'chex:reach_space') {
        // Unlock advanced rocket tier
        if (PlayerTier.unlockRocketTier(player, 2)) {
            player.tell('§aYou have unlocked Rocket Tier 2!');
        }
        
        // Unlock Mars
        if (PlayerTier.unlockPlanet(player, 'cosmos:mars')) {
            player.tell('§aYou have discovered Mars!');
        }
        
        // Grant advanced suit
        player.give('cosmic_horizons_extended:suit_ii_helmet');
        player.give('cosmic_horizons_extended:suit_ii_chestplate');
        player.give('cosmic_horizons_extended:suit_ii_leggings');
        player.give('cosmic_horizons_extended:suit_ii_boots');
    }
});

// Quest: Mine titanium
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    if (questId === 'chex:mine_titanium') {
        // Unlock titanium mineral
        if (PlayerTier.unlockMineral(player, 'titanium')) {
            player.tell('§aYou have discovered titanium!');
        }
        
        // Unlock Pandora
        if (PlayerTier.unlockPlanet(player, 'cosmic_horizons_extended:pandora')) {
            player.tell('§aYou have discovered Pandora!');
        }
        
        // Grant titanium rewards
        player.give('cosmic_horizons_extended:titanium_ingot', 16);
        player.give('cosmic_horizons_extended:titanium_plate', 8);
    }
});

// =============================================================================
// CUSTOM QUEST CONDITIONS
// =============================================================================

// Condition: Player has specific rocket tier
ServerEvents.conditions(event => {
    event.register('chex:has_rocket_tier', (data) => {
        return (player) => {
            if (!player) return false;
            const requiredTier = data.tier || 1;
            return PlayerTier.hasRocketTier(player, requiredTier);
        };
    });
});

// Condition: Player has specific suit tier
ServerEvents.conditions(event => {
    event.register('chex:has_suit_tier', (data) => {
        return (player) => {
            if (!player) return false;
            const requiredTier = data.tier || 1;
            return PlayerTier.hasSuitTier(player, requiredTier);
        };
    });
});

// Condition: Player has discovered specific planet
ServerEvents.conditions(event => {
    event.register('chex:has_discovered_planet', (data) => {
        return (player) => {
            if (!player) return false;
            const planetId = data.planet;
            return PlayerTier.hasDiscoveredPlanet(player, planetId);
        };
    });
});

// Condition: Player has discovered specific mineral
ServerEvents.conditions(event => {
    event.register('chex:has_discovered_mineral', (data) => {
        return (player) => {
            if (!player) return false;
            const mineralId = data.mineral;
            return PlayerTier.hasDiscoveredMineral(player, mineralId);
        };
    });
});

// Condition: Player can access specific planet
ServerEvents.conditions(event => {
    event.register('chex:can_access_planet', (data) => {
        return (player) => {
            if (!player) return false;
            const planetId = data.planet;
            return PlayerTier.canAccessPlanet(player, planetId);
        };
    });
});

// =============================================================================
// CUSTOM QUEST REWARDS
// =============================================================================

// Reward: Unlock rocket tier
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    // Check if quest has rocket tier reward
    const rocketTierRewards = {
        'chex:build_advanced_rocket': 2,
        'chex:build_heavy_rocket': 3,
        'chex:build_exotic_rocket': 4,
        'chex:build_ultimate_rocket': 5
    };
    
    const tierToUnlock = rocketTierRewards[questId];
    if (tierToUnlock) {
        if (PlayerTier.unlockRocketTier(player, tierToUnlock)) {
            player.tell(`§aYou have unlocked Rocket Tier ${tierToUnlock}!`);
        }
    }
});

// Reward: Unlock suit tier
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    // Check if quest has suit tier reward
    const suitTierRewards = {
        'chex:craft_advanced_suit': 2,
        'chex:craft_heavy_suit': 3,
        'chex:craft_exotic_suit': 4,
        'chex:craft_ultimate_suit': 5
    };
    
    const tierToUnlock = suitTierRewards[questId];
    if (tierToUnlock) {
        if (PlayerTier.unlockSuitTier(player, tierToUnlock)) {
            player.tell(`§aYou have unlocked Suit Tier ${tierToUnlock}!`);
        }
    }
});

// Reward: Unlock planet
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    // Check if quest has planet reward
    const planetRewards = {
        'chex:explore_moon': 'cosmos:earth_moon',
        'chex:explore_mars': 'cosmos:mars',
        'chex:explore_pandora': 'cosmic_horizons_extended:pandora',
        'chex:explore_arrakis': 'cosmic_horizons_extended:arrakis',
        'chex:explore_aquamundus': 'cosmic_horizons_extended:aqua_mundus',
        'chex:explore_inferno': 'cosmic_horizons_extended:inferno_prime',
        'chex:explore_kepler': 'cosmic_horizons_extended:kepler_452b',
        'chex:explore_aurelia': 'cosmic_horizons_extended:aurelia_ringworld'
    };
    
    const planetToUnlock = planetRewards[questId];
    if (planetToUnlock) {
        if (PlayerTier.unlockPlanet(player, planetToUnlock)) {
            player.tell(`§aYou have discovered ${planetToUnlock}!`);
        }
    }
});

// Reward: Unlock mineral
ServerEvents.custom('ftbquests:task_completed', event => {
    const player = event.player;
    const questId = event.task;
    
    // Check if quest has mineral reward
    const mineralRewards = {
        'chex:mine_ilmenite': 'ilmenite',
        'chex:mine_titanium': 'titanium',
        'chex:mine_tungsten': 'tungsten',
        'chex:mine_platinum': 'platinum',
        'chex:mine_draconium': 'draconium'
    };
    
    const mineralToUnlock = mineralRewards[questId];
    if (mineralToUnlock) {
        if (PlayerTier.unlockMineral(player, mineralToUnlock)) {
            player.tell(`§aYou have discovered ${mineralToUnlock}!`);
        }
    }
});

// =============================================================================
// QUEST PROGRESSION TRACKING
// =============================================================================

// Track rocket tier progression
ServerEvents.custom('chex:rocket_tier_unlocked', event => {
    const player = event.player;
    const tier = event.tier;
    
    // Update quest progress
    const questUpdates = {
        1: 'chex:unlock_rocket_tier_1',
        2: 'chex:unlock_rocket_tier_2',
        3: 'chex:unlock_rocket_tier_3',
        4: 'chex:unlock_rocket_tier_4',
        5: 'chex:unlock_rocket_tier_5'
    };
    
    const questToUpdate = questUpdates[tier];
    if (questToUpdate) {
        // This would need to be implemented based on your quest system
        console.log(`Updating quest ${questToUpdate} for player ${player.name}`);
    }
});

// Track suit tier progression
ServerEvents.custom('chex:suit_tier_unlocked', event => {
    const player = event.player;
    const tier = event.tier;
    
    // Update quest progress
    const questUpdates = {
        1: 'chex:unlock_suit_tier_1',
        2: 'chex:unlock_suit_tier_2',
        3: 'chex:unlock_suit_tier_3',
        4: 'chex:unlock_suit_tier_4',
        5: 'chex:unlock_suit_tier_5'
    };
    
    const questToUpdate = questUpdates[tier];
    if (questToUpdate) {
        // This would need to be implemented based on your quest system
        console.log(`Updating quest ${questToUpdate} for player ${player.name}`);
    }
});

// Track planet discovery
ServerEvents.custom('chex:planet_discovered', event => {
    const player = event.player;
    const planetId = event.planetId;
    
    // Update quest progress
    const questUpdates = {
        'cosmos:earth_moon': 'chex:discover_moon',
        'cosmos:mars': 'chex:discover_mars',
        'cosmic_horizons_extended:pandora': 'chex:discover_pandora',
        'cosmic_horizons_extended:arrakis': 'chex:discover_arrakis',
        'cosmic_horizons_extended:aurelia_ringworld': 'chex:discover_aurelia'
    };
    
    const questToUpdate = questUpdates[planetId];
    if (questToUpdate) {
        // This would need to be implemented based on your quest system
        console.log(`Updating quest ${questToUpdate} for player ${player.name}`);
    }
});

// Track mineral discovery
ServerEvents.custom('chex:mineral_discovered', event => {
    const player = event.player;
    const mineralId = event.mineralId;
    
    // Update quest progress
    const questUpdates = {
        'ilmenite': 'chex:discover_ilmenite',
        'titanium': 'chex:discover_titanium',
        'tungsten': 'chex:discover_tungsten',
        'platinum': 'chex:discover_platinum',
        'draconium': 'chex:discover_draconium'
    };
    
    const questToUpdate = questUpdates[mineralId];
    if (questToUpdate) {
        // This would need to be implemented based on your quest system
        console.log(`Updating quest ${questToUpdate} for player ${player.name}`);
    }
});
