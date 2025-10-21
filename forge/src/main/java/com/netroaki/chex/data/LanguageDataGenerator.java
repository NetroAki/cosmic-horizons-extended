package com.netroaki.chex.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netroaki.chex.CHEX;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Generates language files for the translation system. */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LanguageDataGenerator {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

    // Generate English translations
    gen.addProvider(event.includeClient(), new CHEXLanguageProvider(gen, "en_us"));
  }

  private static class CHEXLanguageProvider extends LanguageProvider {
    public CHEXLanguageProvider(DataGenerator gen, String locale) {
      super(gen.getPackOutput(), CHEX.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
      // Item names
      add("item.chex.translation_tome", "Tome of Translation");
      add("item.chex.translation_tome.desc", "Used to translate ancient texts");
      add(
          "item.chex.translation_tome.usage",
          "Hold in mainhand and right-click with a book in offhand to translate");

      // Translation messages
      add(
          "chex.translate.insufficient_knowledge",
          "You don't have enough knowledge to translate this text");
      add("chex.translate.success", "Successfully translated from %s!");

      // Language names (library system disabled)

      // Book content
      add("chex.book.eldritch.title", "Eldritch Tome");
      add("chex.book.eldritch.author", "Unknown");
      add(
          "chex.book.eldritch.page1",
          "The stars whisper secrets that would drive mortals mad. The ancient ones stir in their"
              + " slumber, and their dreams shape our reality.");

      add("chex.book.draconic.title", "Draconic Codex");
      add("chex.book.draconic.author", "Dragon Sage Valthorian");
      add(
          "chex.book.draconic.page1",
          "In the age of fire and scale, when the great wyrms ruled the skies, knowledge was power,"
              + " and power was everything.");

      add("chex.book.celestial.title", "Celestial Hymns");
      add("chex.book.celestial.author", "Choir of the Celestial Spheres");
      add(
          "chex.book.celestial.page1",
          "Hearken to the music of the spheres, for they sing the song of creation. Each note a"
              + " star, each chord a galaxy.");

      add("chex.book.primordial.title", "Primordial Chronicles");
      add("chex.book.primordial.author", "Elder Elemental");
      add(
          "chex.book.primordial.page1",
          "Before time had meaning, the elements danced in the void. Fire and water, earth and air,"
              + " in perfect harmony.");

      add("chex.book.voidtongue.title", "Whispers of the Void");
      add("chex.book.voidtongue.author", "The Nameless One");
      add(
          "chex.book.voidtongue.page1",
          "In the space between stars, where light fears to tread, the void whispers of things that"
              + " were, and things yet to be.");
    }
  }
}
