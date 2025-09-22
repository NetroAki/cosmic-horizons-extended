package com.netroaki.chex.world.aqua;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "chex")
public class AquaMechanics {
    // Oxygen consumption rate in ticks (1 per second by default)
    private static final int OXYGEN_CONSUMPTION_RATE = 20;
    private static final int OXYGEN_DAMAGE_INTERVAL = 40; // Damage every 2 seconds without oxygen
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        Level level = player.level();
        
        if (!level.dimension().location().getPath().equals("aqua_mundus")) return;
        
        // Handle oxygen system
        if (!player.isCreative() && !player.isSpectator()) {
            handleOxygenSystem(player, level);
        }
        
        // Handle pressure system
        handlePressureSystem(player, level);
        
        // Handle thermal system
        handleThermalSystem(player, level);
    }
    
    private static void handleOxygenSystem(Player player, Level level) {
        // Check if player has a valid breathing apparatus
        boolean hasOxygen = checkOxygenSupply(player);
        
        if (player.isUnderWater() && !hasOxygen) {
            // Apply oxygen damage at intervals
            if (player.tickCount % OXYGEN_DAMAGE_INTERVAL == 0) {
                player.hurt(level.damageSources().drown(), 2.0F);
            }
            
            // Show air bubbles animation
            if (level.isClientSide && level.random.nextFloat() < 0.1F) {
                level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.BUBBLE,
                    player.getX() + (level.random.nextDouble() - 0.5) * player.getBbWidth(),
                    player.getY() + level.random.nextDouble() * player.getBbHeight(),
                    player.getZ() + (level.random.nextDouble() - 0.5) * player.getBbWidth(),
                    0.0D, 0.1D, 0.0D
                );
            }
        }
    }
    
    private static boolean checkOxygenSupply(Player player) {
        // TODO: Implement check for oxygen tanks or other breathing apparatus
        // This should check player's equipment for items that provide oxygen
        return false;
    }
    
    private static void handlePressureSystem(Player player, Level level) {
        if (player.isUnderWater()) {
            int depth = calculateDepth(player, level);
            
            // Apply pressure effects based on depth
            if (depth > 30) {
                // Apply mining fatigue at great depths
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.DIG_SLOWDOWN,
                    100,
                    Math.min(2, depth / 30 - 1),
                    false,
                    true
                ));
                
                // Apply damage if no pressure protection
                if (!hasPressureProtection(player) && player.tickCount % 40 == 0) {
                    player.hurt(level.damageSources().drown(), 1.0F);
                }
            }
        }
    }
    
    private static int calculateDepth(Player player, Level level) {
        BlockPos.MutableBlockPos pos = player.blockPosition().mutable();
        int depth = 0;
        
        // Count water blocks above the player
        while (level.getBlockState(pos).is(Blocks.WATER) && depth < 256) {
            depth++;
            pos.move(net.minecraft.core.Direction.UP);
        }
        
        return depth;
    }
    
    private static boolean hasPressureProtection(Player player) {
        // TODO: Implement check for pressure protection equipment
        // This should check player's armor for pressure suits or similar
        return false;
    }
    
    private static void handleThermalSystem(Player player, Level level) {
        Biome biome = level.getBiome(player.blockPosition()).value();
        float temperature = biome.getBaseTemperature();
        
        // Check for thermal vents or other heat sources
        if (isNearHeatSource(player, level)) {
            temperature += 1.0F; // Increase temperature near heat sources
        }
        
        // Apply effects based on temperature
        if (temperature < 0.1F) {
            // Very cold - apply slowness and damage over time
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN,
                100,
                1,
                false,
                true
            ));
            
            if (player.tickCount % 80 == 0) {
                player.hurt(level.damageSources().freeze(), 1.0F);
            }
        } else if (temperature > 1.5F) {
            // Very hot - apply fire resistance but also damage over time
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE,
                100,
                0,
                false,
                true
            ));
            
            if (player.tickCount % 80 == 0) {
                player.hurt(level.damageSources().onFire(), 1.0F);
            }
        }
    }
    
    private static void showColdEffect(Player player, Level level) {
        // Show cold breath particles
        for (int i = 0; i < 5; i++) {
            level.addParticle(
                net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,
                player.getX() + (level.random.nextDouble() - 0.5) * 0.5,
                player.getY() + player.getEyeHeight() + (level.random.nextDouble() - 0.5) * 0.5,
                player.getZ() + (level.random.nextDouble() - 0.5) * 0.5,
                0.0D, 0.0D, 0.0D
            );
        }
    }
    
    private static void showHeatEffect(Player player, Level level) {
        // Show heat distortion and steam particles
        for (int i = 0; i < 3; i++) {
            level.addParticle(
                net.minecraft.core.particles.ParticleTypes.CLOUD,
                player.getX() + (level.random.nextDouble() - 0.5) * player.getBbWidth(),
                player.getY() + level.random.nextDouble() * player.getBbHeight(),
                player.getZ() + (level.random.nextDouble() - 0.5) * player.getBbWidth(),
                (level.random.nextDouble() - 0.5) * 0.1,
                level.random.nextDouble() * 0.1,
                (level.random.nextDouble() - 0.5) * 0.1
            );
        }
    }
    
    private static boolean isNearHeatSource(LivingEntity entity, Level level) {
        // Check for nearby heat sources like lava or thermal vents
        AABB searchBox = entity.getBoundingBox().inflate(5.0D);
        return BlockPos.betweenClosedStream(searchBox).anyMatch(pos -> 
            level.getBlockState(pos).is(Blocks.LAVA) || 
            level.getBlockState(pos).is(net.minecraft.tags.BlockTags.FIRE) ||
            level.getBlockState(pos).is(Blocks.MAGMA_BLOCK) ||
            level.getBlockState(pos).is(Blocks.CAMPFIRE) ||
            level.getBlockState(pos).is(Blocks.SOUL_CAMPFIRE) ||
            level.getBlockState(pos).is(Blocks.LAVA_CAULDRON) ||
            level.getBlockState(pos).is(Blocks.FURNACE) ||
            level.getBlockState(pos).is(Blocks.BLAST_FURNACE) ||
            level.getBlockState(pos).is(Blocks.SMOKER)
        );
    }
}
