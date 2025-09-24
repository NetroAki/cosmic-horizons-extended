package com.netroaki.chex.quest;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public abstract class Quest {
    public enum QuestType {
        MAIN_STORY("main_story", "Main Story"),
        SIDE("side", "Side Quest"),
        REPEATABLE("repeatable", "Repeatable"),
        SECRET("secret", "Secret Quest");

        private final String id;
        private final String displayName;

        QuestType(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static class Reward {
        public enum Type {
            ITEM,
            EXPERIENCE,
            REPUTATION,
            COMMAND
        }

        private final Type type;
        private final ItemStack item;
        private final int amount;
        private final String command;
        private final String faction; // For reputation rewards

        public String getFaction() {
            return faction;
        }

        public Reward(Type type, ItemStack item, int amount) {
            this(type, item, amount, null);
        }

        public Reward(Type type, ItemStack item, int amount, String faction) {
            this.type = type;
            this.item = item;
            this.amount = amount;
            this.command = null;
            this.faction = faction;
        }

        public Reward(Type type, ItemStack item, float amount) {
            this(type, item, (int) amount, null);
        }

        public Reward(Type type, String faction, int amount) {
            this.type = type;
            this.item = null;
            this.amount = amount;
            this.command = null;
            this.faction = faction;
        }

        public Reward(String command) {
            this.type = Type.COMMAND;
            this.item = null;
            this.amount = 0;
            this.command = command;
            this.faction = null;
        }

        public Type getType() {
            return type;
        }

        public ItemStack getItem() {
            return item != null ? item.copy() : null;
        }

        public int getAmount() {
            return amount;
        }

        public String getCommand() {
            return command;
        }
    }

    protected final ResourceLocation id;
    protected final Component title;
    protected final Component description;
    protected final QuestType type;
    protected final QuestStatus status;
    protected final List<QuestObjective> objectives;
    protected final List<Reward> rewards;
    protected final List<ResourceLocation> prerequisites;
    protected final int level; // Quest level/difficulty
    protected final List<ItemStack> itemRewards;
    protected final boolean repeatable;
    protected final int cooldownTicks; // Cooldown before quest can be repeated (in ticks)

    protected Quest(ResourceLocation id, Component title, Component description, QuestType type,
            List<QuestObjective> objectives, List<Reward> rewards, int level,
            List<ResourceLocation> prerequisites) {
        this(id, title, description, type, objectives, rewards, level, prerequisites, false, 0);
    }

    protected Quest(ResourceLocation id, Component title, Component description, QuestType type,
            List<QuestObjective> objectives, List<Reward> rewards, int level,
            List<ResourceLocation> prerequisites, boolean repeatable, int cooldownTicks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = QuestStatus.AVAILABLE;
        this.objectives = objectives != null ? new ArrayList<>(objectives) : new ArrayList<>();
        this.rewards = rewards != null ? new ArrayList<>(rewards) : new ArrayList<>();
        this.prerequisites = prerequisites != null ? new ArrayList<>(prerequisites) : new ArrayList<>();
        this.level = level;
        this.itemRewards = new ArrayList<>();
        this.repeatable = repeatable;
        this.cooldownTicks = cooldownTicks;

        // Extract item rewards from rewards list
        if (rewards != null) {
            for (Reward reward : rewards) {
                if (reward.getType() == Reward.Type.ITEM && reward.getItem() != null) {
                    this.itemRewards.add(reward.getItem());
                }
            }
        }
    }

    public abstract boolean isComplete(ServerPlayer player);

    public abstract void onComplete(ServerPlayer player);

    public void addReward(String rewardType, int amount) {
        rewards.put(rewardType, amount);
    }

    public void addPrerequisite(Quest quest) {
        prerequisites.add(quest);
    }

    public void addObjective(QuestObjective objective) {
        objectives.add(objective);
    }

    public boolean arePrerequisitesMet(ServerPlayer player) {
        return prerequisites.stream().allMatch(quest -> quest.isComplete(player));
    }

    public Component getTitleComponent() {
        return Component.literal(title);
    }

    public Component getDescriptionComponent() {
        return Component.literal(description);
    }

    public enum QuestStatus {
        LOCKED,
        AVAILABLE,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }

    public enum QuestType {
        MAIN_STORY,
        SIDE_QUEST,
        REPEATABLE,
        DAILY,
        ACHIEVEMENT
    }
}
