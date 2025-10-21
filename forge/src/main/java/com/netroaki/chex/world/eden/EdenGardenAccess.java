package com.netroaki.chex.world.eden;

import com.netroaki.chex.CHEX;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EdenGardenAccess {
  private static final ResourceLocation EDEN_GARDEN_DIMENSION =
      new ResourceLocation(CHEX.MOD_ID, "eden_garden");
  private static final int REQUIRED_ENDER_PEARLS = 8;
  private static final int REQUIRED_BLAZE_POWDER = 4;
  private static final int REQUIRED_GLOWSTONE_DUST = 16;

  @SubscribeEvent
  public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    Player player = event.getEntity();
    Level level = event.getLevel();
    ItemStack itemStack = event.getItemStack();
    BlockPos pos = event.getPos();

    // Check if the portal frame is being activated with a special item
    if (itemStack.is(Items.ENDER_EYE) && isPortalFrame(level, pos)) {
      if (level.dimension().location().equals(EDEN_GARDEN_DIMENSION)) {
        // Already in Eden's Garden, do nothing or provide feedback
        return;
      }

      // Check if the player has the required items
      if (hasRequiredItems(player)) {
        // Consume items
        consumeRequiredItems(player);

        // Teleport to Eden's Garden
        if (player instanceof ServerPlayer serverPlayer) {
          teleportToEdenGarden(serverPlayer);
        }

        // Cancel the event to prevent default behavior
        event.setCanceled(true);
      } else {
        // Provide feedback about missing requirements
        if (!player.getInventory().hasAnyMatching(stack -> stack.is(Items.ENDER_PEARL))) {
          player.displayClientMessage(
              Component.literal(
                  "You need " + REQUIRED_ENDER_PEARLS + " Ender Pearls to open the portal"),
              true);
        } else if (!player.getInventory().hasAnyMatching(stack -> stack.is(Items.BLAZE_POWDER))) {
          player.displayClientMessage(
              Component.literal(
                  "You need " + REQUIRED_BLAZE_POWDER + " Blaze Powder to open the portal"),
              true);
        } else if (!player.getInventory().hasAnyMatching(stack -> stack.is(Items.GLOWSTONE_DUST))) {
          player.displayClientMessage(
              Component.literal(
                  "You need " + REQUIRED_GLOWSTONE_DUST + " Glowstone Dust to open the portal"),
              true);
        }
      }
    }
  }

  private static boolean isPortalFrame(Level level, BlockPos pos) {
    // Check if the block being clicked is part of a valid portal frame
    // This is a simplified check - you might want to implement more robust validation
    return level.getBlockState(pos).is(/* Your portal frame block */ );
  }

  private static boolean hasRequiredItems(Player player) {
    int enderPearls = countItems(player, Items.ENDER_PEARL);
    int blazePowder = countItems(player, Items.BLAZE_POWDER);
    int glowstoneDust = countItems(player, Items.GLOWSTONE_DUST);

    return enderPearls >= REQUIRED_ENDER_PEARLS
        && blazePowder >= REQUIRED_BLAZE_POWDER
        && glowstoneDust >= REQUIRED_GLOWSTONE_DUST;
  }

  private static int countItems(Player player, Item item) {
    int count = 0;
    for (ItemStack stack : player.getInventory().items) {
      if (stack.is(item)) {
        count += stack.getCount();
      }
    }
    return count;
  }

  private static void consumeRequiredItems(Player player) {
    consumeItems(player, Items.ENDER_PEARL, REQUIRED_ENDER_PEARLS);
    consumeItems(player, Items.BLAZE_POWDER, REQUIRED_BLAZE_POWDER);
    consumeItems(player, Items.GLOWSTONE_DUST, REQUIRED_GLOWSTONE_DUST);
  }

  private static void consumeItems(Player player, Item item, int amount) {
    int remaining = amount;
    for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
      ItemStack stack = player.getInventory().getItem(i);
      if (stack.is(item)) {
        int toRemove = Math.min(stack.getCount(), remaining);
        stack.shrink(toRemove);
        remaining -= toRemove;
      }
    }
  }

  private static void teleportToEdenGarden(ServerPlayer player) {
    ServerLevel destination = player.getServer().getLevel(EdenDimension.EDEN_DIMENSION);
    if (destination != null) {
      // Find a safe spawn location
      BlockPos spawnPos = destination.getSharedSpawnPos();

      // Create a custom teleporter for the dimension
      ITeleporter teleporter =
          new ITeleporter() {
            @Override
            public PortalInfo getPortalInfo(
                Entity entity,
                ServerLevel destWorld,
                Function<ServerLevel, PortalInfo> defaultPortalInfo) {
              return new PortalInfo(
                  new Vec3(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5),
                  Vec3.ZERO,
                  entity.getYRot(),
                  entity.getXRot());
            }
          };

      // Perform the teleportation
      player.changeDimension(destination, teleporter);

      // Send a welcome message
      player.displayClientMessage(
          Component.literal("Welcome to Eden's Garden, a sanctuary of peace and abundance!"),
          false);
    }
  }
}
