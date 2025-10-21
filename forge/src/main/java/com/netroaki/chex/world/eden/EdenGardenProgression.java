package com.netroaki.chex.world.eden;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.eden.entity.LumiflyEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EdenGardenProgression {
  private static final Map<UUID, Integer> playerProgress = new HashMap<>();
  private static final int MAX_PROGRESS = 3;

  // Advancement IDs that unlock Eden's Garden progression
  private static final String[] REQUIRED_ADVANCEMENTS = {
    "story/enter_the_nether", "story/enter_the_end", "nether/brew_potion"
  };

  @SubscribeEvent
  public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    // Initialize player progress if not already set
    playerProgress.putIfAbsent(event.getEntity().getUUID(), 0);
  }

  @SubscribeEvent
  public static void onAdvancement(AdvancementEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }

    // Check if the advancement is one that progresses Eden's Garden access
    String advancementId = event.getAdvancement().getId().toString();
    for (String requiredAdvancement : REQUIRED_ADVANCEMENTS) {
      if (advancementId.contains(requiredAdvancement)) {
        incrementProgress(player);
        break;
      }
    }
  }

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
      return;
    }

    Player player = event.player;
    int progress = getProgress(player);

    // Check for special conditions that might unlock abilities
    if (progress >= 1
        && !EdenGardenAbilities.hasAbility(player, EdenGardenAbilities.HEALING_AURA)) {
      // Unlock healing aura after first progress point
      EdenGardenAbilities.unlockAbility(player, EdenGardenAbilities.HEALING_AURA);
    }

    if (progress >= 2
        && !EdenGardenAbilities.hasAbility(player, EdenGardenAbilities.NATURES_BOUNTY)) {
      // Unlock nature's bounty after second progress point
      EdenGardenAbilities.unlockAbility(player, EdenGardenAbilities.NATURES_BOUNTY);
    }

    if (progress >= MAX_PROGRESS
        && !EdenGardenAbilities.hasAbility(player, EdenGardenAbilities.CELESTIAL_PROTECTION)) {
      // Unlock celestial protection after completing all progress
      EdenGardenAbilities.unlockAbility(player, EdenGardenAbilities.CELESTIAL_PROTECTION);
      player.displayClientMessage(
          Component.literal("You have unlocked the full power of Eden's Garden!"), false);
    }
  }

  @SubscribeEvent
  public static void onEntityDeath(LivingDeathEvent event) {
    LivingEntity entity = event.getEntity();
    DamageSource source = event.getSource();

    // Check if a Lumifly was killed by a player
    if (entity instanceof LumiflyEntity && source.getEntity() instanceof Player player) {
      player.displayClientMessage(
          Component.literal("The peaceful creatures of Eden's Garden are not to be harmed!"),
          false);
      // Apply a negative effect for harming peaceful creatures
      player.addEffect(
          new MobEffectInstance(
              MobEffects.WEAKNESS,
              600, // 30 seconds
              1, // Level II
              false,
              true,
              true));
    }
  }

  public static int getProgress(Player player) {
    return playerProgress.getOrDefault(player.getUUID(), 0);
  }

  private static void incrementProgress(Player player) {
    int currentProgress = getProgress(player);
    if (currentProgress < MAX_PROGRESS) {
      playerProgress.put(player.getUUID(), currentProgress + 1);

      // Notify player of progress
      player.displayClientMessage(
          Component.literal(
              "Eden's Garden progress: " + (currentProgress + 1) + "/" + MAX_PROGRESS),
          false);

      // If this was the final progress step, unlock the portal
      if (currentProgress + 1 == MAX_PROGRESS) {
        player.displayClientMessage(
            Component.literal("The path to Eden's Garden is now open to you!"), false);
      }
    }
  }

  public static boolean canAccessEdenGarden(Player player) {
    // Players can access Eden's Garden after completing all required advancements
    return getProgress(player) >= MAX_PROGRESS;
  }

  // Helper method to check if a player has all required items to enter Eden's Garden
  public static boolean hasRequiredItems(Player player) {
    return player.getInventory().countItem(Items.ENDER_PEARL) >= 8
        && player.getInventory().countItem(Items.BLAZE_POWDER) >= 4
        && player.getInventory().countItem(Items.GLOWSTONE_DUST) >= 16;
  }
}
