package com.netroaki.chex.worldgen.structure;

import com.netroaki.chex.registry.ArrakisBlocks;
import com.netroaki.chex.registry.ArrakisEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.function.Function;

public class FremenVillagePieces {
    private static final int VILLAGE_RADIUS = 24;
    
    public static void addPieces(StructureTemplateManager structureManager, BlockPos centerPos, 
                               StructurePiecesBuilder builder, RandomSource random) {
        // Create the main village piece
        builder.addPiece(new FremenVillagePiece(
            centerPos.getX() - VILLAGE_RADIUS / 2,
            centerPos.getY(),
            centerPos.getZ() - VILLAGE_RADIUS / 2,
            VILLAGE_RADIUS,
            VILLAGE_RADIUS
        ));
    }
    
    public static class FremenVillagePiece extends StructurePiece {
        private final int width;
        private final int depth;
        
        public FremenVillagePiece(int x, int y, int z, int width, int depth) {
            super(ArrakisStructurePieces.FREMEN_VILLAGE_PIECE.get(), 0, 
                new BoundingBox(x, y - 2, z, x + width, y + 6, z + depth));
            this.width = width;
            this.depth = depth;
        }
        
        public FremenVillagePiece(StructurePieceSerializationContext context, CompoundTag tag) {
            super(ArrakisStructurePieces.FREMEN_VILLAGE_PIECE.get(), tag);
            this.width = tag.getInt("Width");
            this.depth = tag.getInt("Depth");
        }
        
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            tag.putInt("Width", this.width);
            tag.putInt("Depth", this.depth);
        }
        
        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, 
                              ChunkGenerator generator, RandomSource random, 
                              BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            // Create the village platform
            BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
            BlockState smoothSandstone = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
            BlockState cutSandstone = Blocks.CUT_SANDSTONE.defaultBlockState();
            
            // Generate the platform
            for (int x = 0; x < this.width; x++) {
                for (int z = 0; z < this.depth; z++) {
                    // Skip if outside the circular village
                    double distFromCenter = Math.sqrt(
                        Math.pow(x - this.width/2.0, 2) + 
                        Math.pow(z - this.depth/2.0, 2)
                    );
                    
                    if (distFromCenter > VILLAGE_RADIUS/2.0) {
                        continue;
                    }
                    
                    // Create platform layers
                    for (int y = 0; y < 3; y++) {
                        BlockState blockToPlace = y == 0 ? sandstone : 
                                            y == 1 ? smoothSandstone : cutSandstone;
                        this.placeBlock(level, blockToPlace, x, y, z, box);
                    }
                }
            }
            
            // Generate buildings and villagers
            if (this.boundingBox.getCenter().equals(pos.getCenter())) {
                // Generate a central meeting area
                generateMeetingArea(level, box, random, 
                    this.boundingBox.minX() + this.width/2 - 5,
                    this.boundingBox.minY() + 3,
                    this.boundingBox.minZ() + this.depth/2 - 5
                );
                
                // Generate houses around the center
                generateHouses(level, box, random);
                
                // Generate villagers
                generateVillagers(level, box, random);
            }
        }
        
        private void generateMeetingArea(LevelAccessor level, BoundingBox box, RandomSource random, 
                                      int x, int y, int z) {
            // Generate a circular meeting area with seating
            int radius = 5;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx*dx + dz*dz <= radius*radius) {
                        // Floor
                        this.placeBlock(level, Blocks.SMOOTH_SANDSTONE_SLAB.defaultBlockState(), 
                            x + dx, y, z + dz, box);
                            
                        // Seating around the edge
                        if (dx*dx + dz*dz >= (radius-1)*(radius-1) && (dx+dz) % 2 == 0) {
                            this.placeBlock(level, Blocks.SMOOTH_SANDSTONE_SLAB.defaultBlockState()
                                .setValue(SlabBlock.TYPE, SlabType.BOTTOM), 
                                x + dx, y + 1, z + dz, box);
                        }
                    }
                }
            }
        }
        
        private void generateHouses(LevelAccessor level, BoundingBox box, RandomSource random) {
            int centerX = this.boundingBox.minX() + this.width/2;
            int centerZ = this.boundingBox.minZ() + this.depth/2;
            int y = this.boundingBox.minY() + 3;
            
            // Generate houses in a circle around the center
            int houseCount = 4 + random.nextInt(3);
            for (int i = 0; i < houseCount; i++) {
                double angle = (2 * Math.PI * i) / houseCount;
                int x = (int)(centerX + Math.cos(angle) * (VILLAGE_RADIUS * 0.4));
                int z = (int)(centerZ + Math.sin(angle) * (VILLAGE_RADIUS * 0.4));
                
                generateHouse(level, box, random, x, y, z, 
                    (float)(angle + Math.PI/2));
            }
        }
        
        private void generateHouse(LevelAccessor level, BoundingBox box, RandomSource random, 
                                 int x, int y, int z, float rotation) {
            // Simple dome-shaped house
            int radius = 3 + random.nextInt(2);
            int height = 2 + random.nextInt(2);
            
            // Floor
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx*dx + dz*dz <= radius*radius) {
                        this.placeBlock(level, Blocks.SMOOTH_SANDSTONE.defaultBlockState(), 
                            x + dx, y, z + dz, box);
                    }
                }
            }
            
            // Walls and dome
            for (int dy = 1; dy <= height + 1; dy++) {
                int currentRadius = (int)(radius * (1 - (dy / (float)(height + 1))));
                
                for (int dx = -currentRadius; dx <= currentRadius; dx++) {
                    for (int dz = -currentRadius; dz <= currentRadius; dz++) {
                        if (dx*dx + dz*dz <= currentRadius*currentRadius) {
                            // Walls with door
                            if (dy <= height && (dy > 1 || Math.abs(dx) > 1 || dz != -currentRadius + 1)) {
                                this.placeBlock(level, Blocks.CUT_SANDSTONE.defaultBlockState(), 
                                    x + dx, y + dy, z + dz, box);
                            }
                            
                            // Dome
                            if (dy == height + 1) {
                                this.placeBlock(level, Blocks.SMOOTH_SANDSTONE_SLAB.defaultBlockState(), 
                                    x + dx, y + dy, z + dz, box);
                            }
                        }
                    }
                }
            }
            
            // Add door
            int doorX = x;
            int doorZ = z - radius + 1;
            this.placeBlock(level, Blocks.AIR.defaultBlockState(), doorX, y + 1, doorZ, box);
            this.placeBlock(level, Blocks.AIR.defaultBlockState(), doorX, y + 2, doorZ, box);
        }
        
        private void generateVillagers(LevelAccessor level, BoundingBox box, RandomSource random) {
            int centerX = this.boundingBox.minX() + this.width/2;
            int centerZ = this.boundingBox.minZ() + this.depth/2;
            int y = this.boundingBox.minY() + 4;
            
            // Generate Fremen villagers
            int villagerCount = 3 + random.nextInt(4);
            for (int i = 0; i < villagerCount; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double distance = 2 + random.nextDouble() * (VILLAGE_RADIUS/2 - 4);
                
                int x = (int)(centerX + Math.cos(angle) * distance);
                int z = (int)(centerZ + Math.sin(angle) * distance);
                
                // Spawn the villager
                if (level instanceof ServerLevel serverLevel) {
                    FremenVillager villager = ArrakisEntities.FREMEN_VILLAGER.get().create(serverLevel);
                    if (villager != null) {
                        villager.setPos(x + 0.5, y, z + 0.5);
                        villager.setVillagerData(villager.getVillagerData()
                            .setProfession(getRandomProfession(random)));
                        level.addFreshEntity(villager);
                    }
                }
            }
        }
        
        private VillagerProfession getRandomProfession(RandomSource random) {
            VillagerProfession[] professions = {
                VillagerProfession.ARMORER,
                VillagerProfession.BUTCHER,
                VillagerProfession.CARTOGRAPHER,
                VillagerProfession.CLERIC,
                VillagerProfession.FARMER,
                VillagerProfession.FISHERMAN,
                VillagerProfession.FLETCHER,
                VillagerProfession.LEATHERWORKER,
                VillagerProfession.LIBRARIAN,
                VillagerProfession.MASON,
                VillagerProfession.NITWIT,
                VillagerProfession.SHEPHERD,
                VillagerProfession.TOOLSMITH,
                VillagerProfession.WEAPONSMITH
            };
            return professions[random.nextInt(professions.length)];
        }
        
        private void placeBlock(LevelAccessor level, BlockState state, int x, int y, int z, BoundingBox box) {
            BlockPos pos = new BlockPos(x, y, z);
            if (box.isInside(pos)) {
                level.setBlock(pos, state, 2);
            }
        }
    }
}
