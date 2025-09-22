package com.netroaki.chex.world.hazards;

import com.netroaki.chex.CHEX;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class AlphaCentauriHazardHandler {
    private static final int FLARE_WARNING_TIME = 200; // 10 seconds at 20 TPS
    private static final int FLARE_DURATION = 400; // 20 seconds at 20 TPS
    private static final float FLARE_DAMAGE = 4.0f;
    private static final int HEAT_DAMAGE_INTERVAL = 40; // 2 seconds at 20 TPS
    
    private static boolean isFlareActive = false;
    private static int flareTimer = 0;
    private static int warningTimer = 0;
    private static float flareIntensity = 0f;
    
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) {
            return;
        }
        
        ServerLevel level = (ServerLevel) event.level;
        
        // Only handle in Alpha Centauri dimension
        if (level.dimension() != Level.OVERWORLD) { // TODO: Replace with Alpha Centauri dimension
            return;
        }
        
        handleFlareEvent(level);
        
        // Apply effects to all players in dimension
        for (Player player : level.players()) {
            handleHeatHazards(player, level);
            if (isFlareActive) {
                handleFlareEffects(player, level);
            }
        }
    }
    
    private static void handleFlareEvent(ServerLevel level) {
        Random random = level.random;
        
        if (isFlareActive) {
            flareTimer--;
            
            // Ramp up/down intensity
            if (flareTimer > FLARE_DURATION * 0.9f) {
                flareIntensity = 1.0f - (flareTimer - FLARE_DURATION * 0.9f) / (FLARE_DURATION * 0.1f);
            } else if (flareTimer < FLARE_DURATION * 0.2f) {
                flareIntensity = flareTimer / (float)(FLARE_DURATION * 0.2f);
            } else {
                flareIntensity = 1.0f;
            }
            
            if (flareTimer <= 0) {
                isFlareActive = false;
                flareIntensity = 0f;
                // Play flare end sound
                level.playSound(null, level.getSharedSpawnPos(), 
                    SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, 
                    1.0f, 0.5f + random.nextFloat() * 0.5f);
            }
        } else if (warningTimer > 0) {
            warningTimer--;
            
            // Visual warning for incoming flare
            if (warningTimer % 20 == 0) {
                level.players().forEach(player -> {
                    player.displayClientMessage(
                        Component.literal("WARNING: SOLAR FLARE DETECTED! SEEK SHELTER!"), 
                        true
                    );
                    level.playSound(null, player.blockPosition(), 
                        SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.AMBIENT, 
                        1.0f, 0.5f);
                });
            }
            
            if (warningTimer <= 0) {
                isFlareActive = true;
                flareTimer = FLARE_DURATION;
                // Play flare start sound
                level.playSound(null, level.getSharedSpawnPos(), 
                    SoundEvents.ENDER_DRAGON_GROWL, SoundSource.AMBIENT, 
                    5.0f, 0.5f);
            }
        } else if (random.nextInt(12000) == 0) { // ~10 minute average between flares
            warningTimer = FLARE_WARNING_TIME;
        }
    }
    
    private static void handleHeatHazards(Player player, ServerLevel level) {
        if (player.tickCount % HEAT_DAMAGE_INTERVAL != 0) {
            return;
        }
        
        BlockPos pos = player.blockPosition();
        
        // Check if player is exposed to direct sunlight
        boolean isExposed = level.canSeeSky(pos) && level.isDay();
        
        if (isExposed) {
            int protectionLevel = getHeatProtectionLevel(player);
            
            // Base heat damage
            if (protectionLevel < 4) { // Require Suit IV for full protection
                // Apply heat effects
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                
                // Scale damage based on protection level
                if (protectionLevel < 2) {
                    player.hurt(level.damageSources().onFire(), 2.0f);
                } else if (protectionLevel < 4) {
                    player.hurt(level.damageSources().onFire(), 1.0f);
                }
            }
        }
    }
    
    private static void handleFlareEffects(Player player, ServerLevel level) {
        // Visual effects
        if (level.random.nextFloat() < 0.1f * flareIntensity) {
            double x = player.getX() + (level.random.nextDouble() - 0.5) * 10.0;
            double y = player.getY() + level.random.nextDouble() * 2.0;
            double z = player.getZ() + (level.random.nextDouble() - 0.5) * 10.0;
            
            level.sendParticles(
                ParticleTypes.FLAME,
                x, y, z,
                5, // Count
                0.5, 0.1, 0.5, // Delta
                0.1 // Speed
            );
        }
        
        // Damage and effects
        if (player.tickCount % 20 == 0) {
            int protectionLevel = getHeatProtectionLevel(player);
            
            // Flares bypass some protection
            if (protectionLevel < 6) { // Suit VI required for full flare protection
                float damage = FLARE_DAMAGE * (1.0f - (protectionLevel * 0.15f));
                player.hurt(level.damageSources().inFire(), damage);
                
                if (protectionLevel < 4) {
                    player.setSecondsOnFire(5);
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                }
            }
        }
    }
    
    private static int getHeatProtectionLevel(LivingEntity entity) {
        // TODO: Implement suit protection level check
        // This should check the player's armor and return the protection level
        // 0 = no protection, 1-3 = partial, 4 = full heat, 6 = flare proof
        return 0;
    }
    
    public static boolean isFlareActive() {
        return isFlareActive;
    }
    
    public static float getFlareIntensity() {
        return flareIntensity;
    }
}
