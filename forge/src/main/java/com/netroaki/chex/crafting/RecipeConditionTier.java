package com.netroaki.chex.crafting;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class RecipeConditionTier implements ICondition {
    public static final ResourceLocation ID = CHEX.id("tier_unlocked");
    private final int minTier;

    public RecipeConditionTier(int minTier) {
        this.minTier = Math.max(1, Math.min(10, minTier));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        // Global load-time condition; per-player gating is enforced at runtime via our
        // systems.
        // Always allow loading; actual usage should be gated in gameplay.
        return true;
    }

    public int getMinTier() {
        return minTier;
    }

    public static class Serializer implements IConditionSerializer<RecipeConditionTier> {
        @Override
        public void write(com.google.gson.JsonObject json, RecipeConditionTier value) {
            json.addProperty("minTier", value.minTier);
        }

        @Override
        public RecipeConditionTier read(com.google.gson.JsonObject json) {
            int minTier = json.has("minTier") ? json.get("minTier").getAsInt() : 1;
            return new RecipeConditionTier(minTier);
        }

        @Override
        public ResourceLocation getID() {
            return RecipeConditionTier.ID;
        }
    }
}
