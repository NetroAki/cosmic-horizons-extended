package com.netroaki.chex.world.hazards;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.capability.PlayerTierCapability;
import com.netroaki.chex.registry.CHEXBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class PandoraHazardManager {

    private static final int HAZARD_CHECK_INTERVAL = 20; // Check every second
    private static final double LEVITATION_STRENGTH = 0.1;
    private static final int HEAT_DAMAGE_INTERVAL = 40; // Damage every 2 seconds
    private static final float SPORE_BLINDNESS_CHANCE = 0.02f;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide) return;
        
        Player player = event.player;
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        
        // Only apply hazards in Pandora dimension
        if (!level.dimension().location().getPath().equals("pandora")) return;
        
        Biome biome = level.getBiome(pos).value();
        
        // Apply biome-specific hazards
        if (biome.is(CHEXBiomes.Tags.FLOATING_ISLANDS)) {
            handleLevitationHazard(player);
        } else if (biome.is(CHEXBiomes.Tags.VOLCANIC_WASTES)) {
            handleHeatHazard(player, level, pos);
        } else if (biome.is(CHEXBiomes.Tags.BIOLUMINESCENT_FOREST)) {
            handleSporeHazard(player, level, pos);
        }
    }
    
    private static void handleLevitationHazard(Player player) {
        // Check if player has gravity resistance
        if (hasGravityResistance(player)) return;
        
        // Apply levitation effect
        player.setDeltaMovement(player.getDeltaMovement().add(0, LEVITATION_STRENGTH, 0));
        player.hurtMarked = true;
        
        // Add particle effects
        if (player.level() instanceof ServerLevel serverLevel) {
            spawnLevitationParticles(serverLevel, player.position());
        }
    }
    
    private static void handleHeatHazard(Player player, Level level, BlockPos pos) {
        // Skip if player has heat resistance or is in water
        if (hasHeatResistance(player) || player.isInWaterRainOrBubble()) {
            return;
        }
        
        // Only damage at set intervals
        if (player.tickCount % HEAT_DAMAGE_INTERVAL != 0) return;
        
        // Apply damage and effects
        player.hurt(level.damageSources().inFire(), 1.0F);
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
        
        // Visual and sound effects
        if (level instanceof ServerLevel serverLevel) {
            spawnHeatParticles(serverLevel, pos);
            level.playSound(null, pos, SoundEvents.LAVA_POP, SoundSource.AMBIENT, 0.5F, 0.8F + level.random.nextFloat() * 0.4F);
        }
    }
    
    private static void handleSporeHazard(Player player, Level level, BlockPos pos) {
        // Skip if player has gas mask
        if (hasGasMask(player)) return;
        
        // Random chance to apply spore effects
        if (level.random.nextFloat() < SPORE_BLINDNESS_CHANCE) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
            
            // Visual effects
            if (level instanceof ServerLevel serverLevel) {
                spawnSporeParticles(serverLevel, player.position());
            }
        }
    }
    
    private static boolean hasGravityResistance(Player player) {
        // Check for gravity stabilizer in armor or curio slot
        return PlayerTierCapability.getTier(player).getTier() >= 2; // Example: Tier 2+ has gravity resistance
    }
    
    private static boolean hasHeatResistance(Player player) {
        // Check for heat-resistant armor
        return PlayerTierCapability.getTier(player).getTier() >= 1; // Example: Tier 1+ has basic heat resistance
    }
    
    private static boolean hasGasMask(Player player) {
        // Check for gas mask in helmet or curio slot
        return player.getInventory().armor.get(3).is(CHEXItems.GAS_MASK.get());
    }
    
    private static void spawnLevitationParticles(ServerLevel level, net.minecraft.world.phys.Vec3 pos) {
        for (int i = 0; i < 5; i++) {
            double x = pos.x + (level.random.nextDouble() - 0.5) * 2.0;
            double y = pos.y + level.random.nextDouble() * 2.0;
            double z = pos.z + (level.random.nextDouble() - 0.5) * 2.0;
            level.sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                x, y, z,
                1, 0, 0.1, 0, 0.05
            );
        }
    }
    
    private static void spawnHeatParticles(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0;
            double y = pos.getY() + 1.0 + level.random.nextDouble();
            double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0;
            level.sendParticles(
                ParticleTypes.LAVA,
                x, y, z,
                1, 0, 0, 0, 0.05
            );
        }
    }
    
    private static void spawnSporeParticles(ServerLevel level, net.minecraft.world.phys.Vec3 pos) {
        AABB bounds = new AABB(pos, pos).inflate(2.0);
        for (int i = 0; i < 20; i++) {
            double x = bounds.minX + level.random.nextDouble() * (bounds.maxX - bounds.minX);
            double y = bounds.minY + level.random.nextDouble() * (bounds.maxY - bounds.minY);
            double z = bounds.minZ + level.random.nextDouble() * (bounds.maxZ - bounds.minZ);
            level.sendParticles(
                ParticleTypes.SPORE_BLOSSOM_AIR,
                x, y, z,
                1, 0.1, 0.1, 0.1, 0.05
            );
        }
    }
}
