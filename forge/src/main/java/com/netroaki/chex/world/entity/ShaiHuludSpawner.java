package com.netroaki.chex.world.entity;

import com.netroaki.chex.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ShaiHuludSpawner {
    
    private static final int SPAWN_CHANCE = 100; // 1 in X chance per sandstorm
    private static final int MIN_PLAYER_DISTANCE = 100; // Minimum distance from players to spawn
    private static final int MAX_PLAYER_DISTANCE = 200; // Maximum distance from players to spawn
    
    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getEntity() instanceof net.minecraft.world.entity.monster.Monster 
                && event.getLevel() instanceof ServerLevel level
                && event.getSpawnType() == MobSpawnType.NATURAL) {
            
            // Only handle Shai-Hulud spawns in Arrakis dimension
            if (level.dimension() == Level.OVERWORLD) { // TODO: Replace with Arrakis dimension
                // Check for sandstorm (you'll need to implement this based on your weather system)
                if (isSandstormActive(level) && level.random.nextInt(SPAWN_CHANCE) == 0) {
                    // Find a suitable spawn position
                    BlockPos spawnPos = findSpawnPosition(level, event.getEntity().blockPosition());
                    if (spawnPos != null) {
                        // Spawn the boss
                        ModEntities.SHAI_HULUD.get().spawn(
                            level,
                            spawnPos,
                            MobSpawnType.NATURAL
                        );
                        
                        // Cancel the original spawn
                        event.setResult(Event.Result.DENY);
                        
                        // Trigger sandstorm effects
                        triggerSandstormEffects(level, spawnPos);
                    }
                }
            }
        }
    }
    
    private static boolean isSandstormActive(ServerLevel level) {
        // TODO: Implement sandstorm check based on your weather system
        // This is a placeholder - you'll need to implement your own sandstorm logic
        return level.isThundering() && level.random.nextFloat() < 0.5f;
    }
    
    private static BlockPos findSpawnPosition(ServerLevel level, BlockPos center) {
        // Try to find a suitable spawn position within the player's render distance
        for (int i = 0; i < 10; i++) {
            // Get a random position within the allowed distance
            double angle = level.random.nextDouble() * Math.PI * 2;
            double distance = MIN_PLAYER_DISTANCE + level.random.nextDouble() * 
                            (MAX_PLAYER_DISTANCE - MIN_PLAYER_DISTANCE);
            
            int x = center.getX() + (int)(Math.cos(angle) * distance);
            int z = center.getZ() + (int)(Math.sin(angle) * distance);
            
            // Get the top block at this position
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);
            
            // Check if the position is valid (on sand)
            if (level.getBlockState(pos.below()).is(Blocks.SAND) && 
                level.getBlockState(pos).isAir() &&
                level.getBlockState(pos.above()).isAir()) {
                return pos;
            }
        }
        
        return null;
    }
    
    private static void triggerSandstormEffects(ServerLevel level, BlockPos pos) {
        // Play a sound
        // level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 10.0F, 0.8F + level.random.nextFloat() * 0.2F);
        
        // Spawn particles
        for (int i = 0; i < 50; i++) {
            double offsetX = pos.getX() + level.random.nextGaussian() * 10.0D;
            double offsetY = pos.getY() + level.random.nextDouble() * 5.0D;
            double offsetZ = pos.getZ() + level.random.nextGaussian() * 10.0D;
            
            // level.sendParticles(ParticleTypes.CLOUD, offsetX, offsetY, offsetZ, 1, 0, 0, 0, 0.1D);
        }
    }
}
