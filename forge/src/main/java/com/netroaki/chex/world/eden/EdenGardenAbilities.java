package com.netroaki.chex.world.eden;

import com.netroaki.chex.CHEX;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EdenGardenAbilities {
  private static final Map<UUID, Long> lastHealTime = new HashMap<>();
  private static final UUID SPEED_BOOST_UUID =
      UUID.fromString("1e1d5b2e-1e1d-4b2e-9e1d-5b2e1e1d5b2e");
  private static final UUID DAMAGE_REDUCTION_UUID =
      UUID.fromString("2f2e6c3f-2f2e-4c3f-af2e-6c3f2f2e6c3f");

  // Ability flags
  public static final int HEALING_AURA = 1;
  public static final int NATURES_BOUNTY = 2;
  public static final int CELESTIAL_PROTECTION = 4;

  // Track player abilities
  private static final Map<UUID, Integer> playerAbilities = new HashMap<>();

  public static void unlockAbility(Player player, int ability) {
    UUID playerId = player.getUUID();
    int currentAbilities = playerAbilities.getOrDefault(playerId, 0);
    playerAbilities.put(playerId, currentAbilities | ability);

    // Notify player of new ability
    String abilityName = getAbilityName(ability);
    if (abilityName != null) {
      player.displayClientMessage(Component.literal("Unlocked ability: " + abilityName), false);
    }
  }

  public static boolean hasAbility(Player player, int ability) {
    return (playerAbilities.getOrDefault(player.getUUID(), 0) & ability) != 0;
  }

  private static String getAbilityName(int ability) {
    return switch (ability) {
      case HEALING_AURA -> "Healing Aura";
      case NATURES_BOUNTY -> "Nature's Bounty";
      case CELESTIAL_PROTECTION -> "Celestial Protection";
      default -> null;
    };
  }

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
      return;
    }

    Player player = event.player;
    UUID playerId = player.getUUID();

    // Apply healing aura effect
    if (hasAbility(player, HEALING_AURA) && player.getHealth() < player.getMaxHealth()) {
      long currentTime = player.level().getGameTime();
      if (currentTime % 100 == 0) { // Every 5 seconds
        player.heal(1.0F);
        lastHealTime.put(playerId, currentTime);
      }
    }

    // Apply speed boost in Eden's Garden
    if (player.level().dimension().location().equals(EdenDimension.EDEN_DIMENSION.location())) {
      if (!player
          .getAttribute(Attributes.MOVEMENT_SPEED)
          .hasModifier(
              new AttributeModifier(
                  SPEED_BOOST_UUID,
                  "Eden's Blessing",
                  0.1,
                  AttributeModifier.Operation.MULTIPLY_TOTAL))) {
        player
            .getAttribute(Attributes.MOVEMENT_SPEED)
            .addTransientModifier(
                new AttributeModifier(
                    SPEED_BOOST_UUID,
                    "Eden's Blessing",
                    0.1,
                    AttributeModifier.Operation.MULTIPLY_TOTAL));
      }
    } else {
      // Remove speed boost when leaving Eden's Garden
      player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_BOOST_UUID);
    }
  }

  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    // Reapply abilities on respawn
    Player player = event.getEntity();
    if (hasAbility(player, CELESTIAL_PROTECTION)) {
      player.addEffect(
          new MobEffectInstance(
              MobEffects.DAMAGE_RESISTANCE,
              200, // 10 seconds
              1, // Level II
              false,
              false,
              true));
    }
  }

  @SubscribeEvent
  public static void onPlayerHurt(LivingHurtEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }

    // Apply damage reduction in Eden's Garden
    if (player.level().dimension().location().equals(EdenDimension.EDEN_DIMENSION.location())
        && hasAbility(player, CELESTIAL_PROTECTION)) {
      event.setAmount(event.getAmount() * 0.7F); // 30% damage reduction
    }
  }

  @SubscribeEvent
  public static void onHarvestDrops(PlayerEvent.HarvestCheck event) {
    Player player = event.getEntity();
    if (hasAbility(player, NATURES_BOUNTY)
        && player.level().dimension().location().equals(EdenDimension.EDEN_DIMENSION.location())) {
      // Double drops in Eden's Garden with Nature's Bounty
      event.setCanHarvest(true);
    }
  }
}
