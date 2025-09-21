package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PlanetOverridesCoreTest {

  @TempDir Path tempDir;

  @Test
  void loadParsesFlatOverrideLayout() throws IOException {
    Path file = tempDir.resolve("chex-planets.json5");
    Files.writeString(
        file,
        "{\n"
            + "  \"cosmos:earth_moon\": {\n"
            + "    \"requiredRocketTier\": 3,\n"
            + "    \"requiredSuitTag\": \"chex:suits/suit3\",\n"
            + "    \"fuel\": \"chex:lox\",\n"
            + "    \"description\": \"Custom moon description\",\n"
            + "    \"hazards\": [\"Vacuum\", \"Radiation\"]\n"
            + "  }\n"
            + "}\n");

    Optional<Map<String, PlanetOverridesCore.Entry>> result = PlanetOverridesCore.load(file);
    assertTrue(result.isPresent());

    PlanetOverridesCore.Entry entry = result.get().get("cosmos:earth_moon");
    assertNotNull(entry);
    assertEquals(3, entry.requiredRocketTier());
    assertNull(entry.requiredSuitTier());
    assertEquals("chex:suits/suit3", entry.requiredSuitTag());
    assertEquals("Custom moon description", entry.description());
    assertEquals("chex:lox", entry.fuel());
    assertEquals(Set.of("vacuum", "radiation"), entry.hazards());
  }

  @Test
  void loadParsesSectionedOverrideLayout() throws IOException {
    Path file = tempDir.resolve("chex-planets-sectioned.json5");
    Files.writeString(
        file,
        "{\n"
            + "  \"cosmic_horizons\": {\n"
            + "    \"cosmos:mercury_wasteland\": {\n"
            + "      \"requiredSuitTier\": 2\n"
            + "    }\n"
            + "  },\n"
            + "  \"chex\": {\n"
            + "    \"chex:pandora\": {\n"
            + "      \"fuel\": \"chex:rp1\",\n"
            + "      \"hazards\": [\"Toxic\"]\n"
            + "    }\n"
            + "  }\n"
            + "}\n");

    Optional<Map<String, PlanetOverridesCore.Entry>> result = PlanetOverridesCore.load(file);
    assertTrue(result.isPresent());

    Map<String, PlanetOverridesCore.Entry> entries = result.get();
    PlanetOverridesCore.Entry mercury = entries.get("cosmos:mercury_wasteland");
    assertNotNull(mercury);
    assertEquals(2, mercury.requiredSuitTier());
    assertNull(mercury.requiredSuitTag());

    PlanetOverridesCore.Entry pandora = entries.get("chex:pandora");
    assertNotNull(pandora);
    assertEquals("chex:rp1", pandora.fuel());
    assertEquals(Set.of("toxic"), pandora.hazards());
  }
}
