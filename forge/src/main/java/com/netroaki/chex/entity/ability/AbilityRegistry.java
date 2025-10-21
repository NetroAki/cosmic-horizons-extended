package com.netroaki.chex.entity.ability;

/**
 * Simple ability contract used by custom mobs that expose runtime abilities such as particle bursts
 * or area effects. Only the Sporefly currently implements this, but the structure allows future
 * expansion without touching mob logic.
 */
public final class AbilityRegistry {

  private AbilityRegistry() {}

  public interface IAbilityUser {
    AbilityManager getAbilityManager();

    void setAbilityManager(AbilityManager manager);
  }
}
