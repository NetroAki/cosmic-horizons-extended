package com.netroaki.chex.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Utility class for file operations.
 */
public class FileUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    /**
     * Gets the base directory for CHEX data files.
     * @return The base directory path as a Path object.
     */
    public static Path getChexDataDir() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            // Use the world directory for server-side operations
            return server.getWorldPath(LevelResource.ROOT).resolve("chex");
        } else {
            // Fallback to config directory for client-side or when server isn't available
            return Paths.get("config/chex");
        }
    }

    /**
     * Ensures the CHEX data directory exists.
     * @return The Path to the CHEX data directory.
     * @throws IOException If the directory cannot be created.
     */
    public static Path ensureChexDataDir() throws IOException {
        Path dir = getChexDataDir();
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        return dir;
    }

    /**
     * Writes a string to a file, creating parent directories if needed.
     * @param path The path to write to.
     * @param content The content to write.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeToFile(Path path, String content) throws IOException {
        ensureChexDataDir();
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(content);
        }
    }

    /**
     * Writes a JSON object to a file.
     * @param path The path to write to.
     * @param json The JSON object to write.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeJsonToFile(Path path, JsonElement json) throws IOException {
        writeToFile(path, GSON.toJson(json));
    }

    /**
     * Creates a timestamped filename with the given prefix and extension.
     * @param prefix The filename prefix.
     * @param extension The file extension (without the dot).
     * @return A formatted filename.
     */
    public static String createTimestampedFilename(String prefix, String extension) {
        String timestamp = DATE_FORMAT.format(new Date());
        return String.format("%s_%s.%s", prefix, timestamp, extension);
    }

    /**
     * Converts a ResourceLocation to a filesystem-safe string.
     * @param location The ResourceLocation to convert.
     * @return A filesystem-safe string.
     */
    public static String toFilesystemPath(ResourceLocation location) {
        return String.format("%s_%s", location.getNamespace(), location.getPath().replace(':', '_'));
    }
}
