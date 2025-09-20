package com.netroaki.chex.ring.blockentity;

import com.netroaki.chex.registry.blocks.CHEXBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ArcSceneryBlockEntity extends BlockEntity {
    public float radius = 120f;
    public float arcAngleDeg = 45f;
    public float thickness = 6f;
    public float tiltDeg = 8f;
    public float yawDeg = 0f;
    public int colorARGB = 0xC0FFFFFF;
    public double fadeStart = 96.0;
    public double fadeEnd = 192.0;
    public boolean emissive = true;
    public boolean dither = true;
    // Use a safe vanilla texture by default until our asset exists
    public ResourceLocation texture = new ResourceLocation("minecraft", "textures/misc/white.png");

    public ArcSceneryBlockEntity(BlockPos pos, BlockState state) {
        super(com.netroaki.chex.registry.CHEXBlockEntities.ARC_SCENERY.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        radius = tag.getFloat("radius");
        arcAngleDeg = tag.getFloat("arc");
        thickness = tag.getFloat("thick");
        tiltDeg = tag.getFloat("tilt");
        yawDeg = tag.getFloat("yaw");
        colorARGB = tag.getInt("color");
        fadeStart = tag.getDouble("fadeStart");
        fadeEnd = tag.getDouble("fadeEnd");
        emissive = tag.getBoolean("emissive");
        dither = tag.getBoolean("dither");
        if (tag.contains("tex", 8)) {
            texture = ResourceLocation.tryParse(tag.getString("tex"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putFloat("radius", radius);
        tag.putFloat("arc", arcAngleDeg);
        tag.putFloat("thick", thickness);
        tag.putFloat("tilt", tiltDeg);
        tag.putFloat("yaw", yawDeg);
        tag.putInt("color", colorARGB);
        tag.putDouble("fadeStart", fadeStart);
        tag.putDouble("fadeEnd", fadeEnd);
        tag.putBoolean("emissive", emissive);
        tag.putBoolean("dither", dither);
        tag.putString("tex", texture.toString());
    }
}
