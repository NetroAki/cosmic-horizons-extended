package com.netroaki.chex.entity.ability;

import com.netroaki.chex.entity.ability.AbilityRegistry.IAbilityUser;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;

/** Minimal runtime registry that keeps track of a mob's active abilities. */
public class AbilityManager {
  private final Mob owner;
  private final Map<Class<? extends MobAbility>, MobAbility> abilities = new HashMap<>();

  public AbilityManager(Mob owner) {
    this.owner = owner;
  }

  public AbilityManager(IAbilityUser user) {
    this((Mob) user);
  }

  public <T extends MobAbility> void registerAbility(Class<T> abilityClass) {
    if (abilities.containsKey(abilityClass)) {
      return;
    }
    try {
      Constructor<T> ctor = abilityClass.getDeclaredConstructor(owner.getClass());
      ctor.setAccessible(true);
      abilities.put(abilityClass, ctor.newInstance(owner));
    } catch (NoSuchMethodException e) {
      try {
        Constructor<T> ctor = abilityClass.getDeclaredConstructor(Mob.class);
        ctor.setAccessible(true);
        abilities.put(abilityClass, ctor.newInstance(owner));
      } catch (Exception inner) {
        throw new IllegalStateException(
            "Unable to construct ability " + abilityClass.getName(), inner);
      }
    } catch (Exception ex) {
      throw new IllegalStateException("Unable to construct ability " + abilityClass.getName(), ex);
    }
  }

  public void tick() {
    abilities.values().forEach(MobAbility::tick);
  }

  public void save(CompoundTag tag) {
    abilities.forEach(
        (clazz, ability) -> {
          CompoundTag abilityTag = new CompoundTag();
          ability.save(abilityTag);
          tag.put(clazz.getName(), abilityTag);
        });
  }

  public void load(CompoundTag tag) {
    abilities.forEach(
        (clazz, ability) -> {
          if (tag.contains(clazz.getName())) {
            ability.load(tag.getCompound(clazz.getName()));
          }
        });
  }

  public Collection<MobAbility> getAbilities() {
    return abilities.values();
  }
}
