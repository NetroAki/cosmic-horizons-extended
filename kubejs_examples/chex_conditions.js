// CHEX KubeJS Conditions
// These conditions integrate with CHEX's tier system

// Condition: Player has specific rocket tier
ServerEvents.conditions(event => {
    event.register('kubejs:player_has_tier', (data) => {
        return (player) => {
            if (!player) return false;
            
            const tier = data.tier || 1;
            const type = data.type || 'rocket';
            
            if (type === 'rocket') {
                return PlayerTier.hasRocketTier(player, tier);
            } else if (type === 'suit') {
                return PlayerTier.hasSuitTier(player, tier);
            }
            
            return false;
        };
    });
    
    // Condition: Player has discovered specific planet
    event.register('kubejs:player_has_discovered_planet', (data) => {
        return (player) => {
            if (!player || !data.planet) return false;
            return PlayerTier.hasDiscoveredPlanet(player, data.planet);
        };
    });
    
    // Condition: Player has discovered specific mineral
    event.register('kubejs:player_has_discovered_mineral', (data) => {
        return (player) => {
            if (!player || !data.mineral) return false;
            return PlayerTier.hasDiscoveredMineral(player, data.mineral);
        };
    });
    
    // Condition: Player has minimum rocket tier
    event.register('kubejs:player_min_rocket_tier', (data) => {
        return (player) => {
            if (!player) return false;
            const minTier = data.minTier || 1;
            return PlayerTier.getRocketTier(player) >= minTier;
        };
    });
    
    // Condition: Player has minimum suit tier
    event.register('kubejs:player_min_suit_tier', (data) => {
        return (player) => {
            if (!player) return false;
            const minTier = data.minTier || 1;
            return PlayerTier.getSuitTier(player) >= minTier;
        };
    });
    
    // Condition: Player has specific tier range
    event.register('kubejs:player_tier_range', (data) => {
        return (player) => {
            if (!player) return false;
            const minTier = data.minTier || 1;
            const maxTier = data.maxTier || 10;
            const type = data.type || 'rocket';
            
            let currentTier;
            if (type === 'rocket') {
                currentTier = PlayerTier.getRocketTier(player);
            } else if (type === 'suit') {
                currentTier = PlayerTier.getSuitTier(player);
            } else {
                return false;
            }
            
            return currentTier >= minTier && currentTier <= maxTier;
        };
    });
});
