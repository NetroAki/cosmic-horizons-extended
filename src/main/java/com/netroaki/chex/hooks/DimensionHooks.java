package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.config.CHEXConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class DimensionHooks {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new DimensionHooks());
    }

    @SubscribeEvent
    public void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ResourceKey<Level> to = event.getTo();
        if (player == null || to == null)
            return;

        for (var entry : PlanetRegistry.getAllPlanets().entrySet()) {
            var def = entry.getValue();
            if (def.id().toString().equals(to.location().toString())) {
                if (!hasRequiredSuit(player, def.requiredSuitTag())) {
                    if (CHEXConfig.suitBounceBack()) {
                        // Attempt to bounce the player back to overworld spawn as a safe default
                        var server = player.getServer();
                        if (server != null) {
                            var overworld = server.getLevel(Level.OVERWORLD);
                            if (overworld != null) {
                                var pos = overworld.getSharedSpawnPos();
                                player.changeDimension(overworld);
                                player.teleportTo(overworld, pos.getX() + 0.5, (double) pos.getY(), pos.getZ() + 0.5,
                                        java.util.Set.of(), player.getYRot(), player.getXRot());
                                CHEX.LOGGER.info("Bounced {} back to overworld due to under-tier suit for {}",
                                        player.getGameProfile().getName(), def.id());
                            }
                        }
                    } else {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 20, 3));
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 20, 3));
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 20, 1));
                        CHEX.LOGGER.info("Applied under-tier suit debuffs to {} for {}",
                                player.getGameProfile().getName(), def.id());
                    }
                }
                break;
            }
        }
    }

    private boolean hasRequiredSuit(Player player, String requiredSuitTag) {
        if (requiredSuitTag == null || requiredSuitTag.isEmpty())
            return true;
        int idx = requiredSuitTag.indexOf(':');
        String path = idx >= 0 ? requiredSuitTag.substring(idx + 1) : requiredSuitTag;
        net.minecraft.resources.ResourceLocation tagId = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                CHEX.MOD_ID,
                "suits/" + path);
        net.minecraft.tags.TagKey<net.minecraft.world.item.Item> tag = net.minecraft.tags.TagKey
                .create(net.minecraft.core.registries.Registries.ITEM, tagId);
        var chest = player.getInventory().armor.get(2);
        if (chest.isEmpty())
            return false;
        return chest.is(tag);
    }
}
