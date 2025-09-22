package com.netroaki.chex.world.environment;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.ArrakisBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class ArrakisEnvironmentHandler {
    private static final int DUST_STORM_CHANCE = 1000; // 1 in X chance per tick when conditions are met
    private static final int MAX_DUST_STORM_DURATION = 12000; // 10 minutes max duration
    private static final int HEAT_DAMAGE_INTERVAL = 100; // Ticks between heat damage
    
    private static boolean isDustStormActive = false;
    private static int dustStormTimer = 0;
    private static float dustStormIntensity = 0f;
    
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) {
            return;
        }
        
        ServerLevel level = (ServerLevel) event.level;
        
        // Only handle in Arrakis dimension
        if (level.dimension() != Level.OVERWORLD) { // TODO: Replace with Arrakis dimension
            return;
        }
        
        // Handle dust storm timing and intensity
        handleDustStorm(level);
        
        // Apply environmental effects to players
        for (Player player : level.players()) {
            handleHeatEffects(player, level);
            handleDustStormEffects(player, level);
        }
    }
    
    private static void handleDustStorm(ServerLevel level) {
        Random random = level.random;
        
        if (isDustStormActive) {
            dustStormTimer--;
            
            // Gradually increase intensity at start, maintain, then decrease at end
            if (dustStormTimer > MAX_DUST_STORM_DURATION * 0.9f) {
                // Ramp up
                dustStormIntensity = 1.0f - (dustStormTimer - MAX_DUST_STORM_DURATION * 0.9f) / (MAX_DUST_STORM_DURATION * 0.1f);
            } else if (dustStormTimer < MAX_DUST_STORM_DURATION * 0.2f) {
                // Ramp down
                dustStormIntensity = dustStormTimer / (float)(MAX_DUST_STORM_DURATION * 0.2f);
            } else {
                // Maintain peak intensity
                dustStormIntensity = 1.0f;
            }
            
            // End dust storm
            if (dustStormTimer <= 0) {
                isDustStormActive = false;
                dustStormIntensity = 0f;
                // Play storm end sound
                level.playSound(null, level.getSharedSpawnPos(), SoundEvents.WEATHER_RAIN_STOP, 
                    SoundSource.WEATHER, 1.0f, 0.8f + random.nextFloat() * 0.2f);
            }
        } else if (random.nextInt(DUST_STORM_CHANCE) == 0) {
            // Start new dust storm
            isDustStormActive = true;
            dustStormTimer = MAX_DUST_STORM_DURATION / 2 + random.nextInt(MAX_DUST_STORM_DURATION / 2);
            // Play storm start sound
            level.playSound(null, level.getSharedSpawnPos(), SoundEvents.WEATHER_RAIN_ABOVE, 
                SoundSource.WEATHER, 1.0f, 0.5f + random.nextFloat() * 0.3f);
        }
    }
    
    private static void handleHeatEffects(Player player, ServerLevel level) {
        if (player.tickCount % HEAT_DAMAGE_INTERVAL != 0) {
            return;
        }
        
        BlockPos pos = player.blockPosition();
        
        // Check if player is exposed to direct sunlight
        boolean isExposed = level.canSeeSky(pos) && level.isDay();
        
        if (isExposed) {
            // Apply heat effect based on protection level
            int protectionLevel = getHeatProtectionLevel(player);
            
            if (protectionLevel < 1) {
                // No protection - apply severe effects
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 1));
                
                if (player.tickCount % (HEAT_DAMAGE_INTERVAL * 2) == 0) {
                    player.hurt(level.damageSources().onFire(), 1.0f);
                }
            } else if (protectionLevel < 3) {
                // Partial protection - apply moderate effects
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0));
                
                if (player.tickCount % (HEAT_DAMAGE_INTERVAL * 4) == 0) {
                    player.hurt(level.damageSources().onFire(), 0.5f);
                }
            }
            // Full protection - no effects
        }
    }
    
    private static void handleDustStormEffects(Player player, ServerLevel level) {
        if (!isDustStormActive || dustStormIntensity < 0.1f) {
            return;
        }
        
        // Apply visibility reduction
        if (player.tickCount % 20 == 0) {
            int visibilityReduction = (int)(10 * dustStormIntensity);
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 0, false, false));
        }
        
        // Spawn dust particles around player
        if (level.random.nextFloat() < dustStormIntensity * 0.3f) {
            double x = player.getX() + (level.random.nextDouble() - 0.5) * 10.0;
            double y = player.getY() + level.random.nextDouble() * 5.0;
            double z = player.getZ() + (level.random.nextDouble() - 0.5) * 10.0;
            
            level.sendParticles(
                ParticleTypes.CLOUD,
                x, y, z,
                5, // Count
                0.5, 0.1, 0.5, // Delta
                0.1 // Speed
            );
        }
    }
    
    private static int getHeatProtectionLevel(LivingEntity entity) {
        // TODO: Implement proper protection level check based on armor
        // This is a placeholder - check for environmental protection gear
        int protection = 0;
        
        // Check each armor slot for protection
        for (ItemStack armor : entity.getArmorSlots()) {
            if (!armor.isEmpty()) {
                // TODO: Add custom check for heat-resistant armor
                protection++;
            }
        }
        
        // Standing in water provides some protection
        if (entity.isInWaterRainOrBubble()) {
            protection = Math.max(protection, 2);
        }
        
        return Math.min(protection, 4);
    }
    
    public static boolean isDustStormActive() {
        return isDustStormActive;
    }
    
    public static float getDustStormIntensity() {
        return dustStormIntensity;
    }
}
