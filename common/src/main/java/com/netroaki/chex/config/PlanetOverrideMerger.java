package com.netroaki.chex.config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public final class PlanetOverrideMerger {
  public record PlanetInfo(
      String name,
      int requiredRocketTier,
      String requiredSuitTag,
      String fuel,
      String description,
      Set<String> hazards) {
    public PlanetInfo {
      name = sanitize(name, "");
      requiredSuitTag = sanitize(requiredSuitTag, "chex:suits/suit1");
      fuel = sanitize(fuel, "");
      description = description == null ? "" : description;
      hazards =
          hazards == null ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(hazards));
    }

    private static String sanitize(String value, String fallback) {
      if (value == null) return fallback;
      String trimmed = value.trim();
      return trimmed.isEmpty() ? fallback : trimmed;
    }
  }

  private PlanetOverrideMerger() {}

  public static PlanetInfo merge(PlanetInfo base, PlanetOverridesCore.Entry override) {
    if (override == null) {
      return base;
    }

    int tier = base.requiredRocketTier();
    if (override.requiredRocketTier() != null) {
      tier = clamp(override.requiredRocketTier(), 1, 10);
    }

    String suitTag = base.requiredSuitTag();
    String overrideSuitTag = sanitizeOverride(override.requiredSuitTag());
    if (overrideSuitTag != null) {
      suitTag = overrideSuitTag;
    } else if (override.requiredSuitTier() != null) {
      suitTag = buildSuitTag(override.requiredSuitTier());
    }

    String fuel = base.fuel();
    String overrideFuel = sanitizeOverride(override.fuel());
    if (overrideFuel != null) {
      fuel = overrideFuel;
    }

    String description = base.description();
    String overrideDescription = sanitizeOverride(override.description());
    if (overrideDescription != null) {
      description = overrideDescription;
    }

    String name = base.name();
    String overrideName = sanitizeOverride(override.name());
    if (overrideName != null) {
      name = overrideName;
    }

    Set<String> hazards = base.hazards();
    if (override.hazards() != null) {
      LinkedHashSet<String> mergedHazards = new LinkedHashSet<>(base.hazards());
      for (String hazard : override.hazards()) {
        if (hazard == null) {
          continue;
        }
        String normalized = hazard.trim();
        if (!normalized.isEmpty()) {
          mergedHazards.add(normalized.toLowerCase(Locale.ROOT));
        }
      }
      hazards = Collections.unmodifiableSet(mergedHazards);
    }

    return new PlanetInfo(name, tier, suitTag, fuel, description, hazards);
  }

  private static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  private static String sanitizeOverride(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private static String buildSuitTag(int tier) {
    int clamped = clamp(tier, 1, 5);
    return String.format(Locale.ROOT, "chex:suits/suit%d", clamped);
  }
}
