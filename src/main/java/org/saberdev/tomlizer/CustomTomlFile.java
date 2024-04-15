package org.saberdev.tomlizer;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class CustomTomlFile {
    private final File file;
    private final Toml toml;
    private Map<String, Object> cachedObjects = new HashMap<>();

    /**
     * Constructor for creating a new instance of CustomTomlFile.
     *
     * @param file The file to be read and written.
     */
    public CustomTomlFile(File file) {
        this.file = file;
        this.toml = new Toml().read(file);
    }

    /**
     * Saves the current TOML data to the specified file.
     *
     * @throws IOException If an error occurs while writing to the file.
     */
    public void saveToFile() throws IOException {
        try (Writer writer = new FileWriter(file)) {
            new TomlWriter().write(toml, writer);
        }
    }

    /**
     * Fetches the value of a specified key from the TOML data.
     *
     * @param key   The key to fetch the value for.
     * @param clazz The expected type of the value.
     * @return The value of the specified key, cast to the specified type.
     * @throws IllegalArgumentException If the key is not found or the value is not of the expected type.
     */
    public <T> T fetch(String key, Class<T> clazz) {
        if (cachedObjects.containsKey(key) && cachedObjects.get(key).getClass().equals(clazz)) {
            return clazz.cast(cachedObjects.get(key));
        }

        Object result = switch (clazz.getSimpleName()) {
            case "String" -> toml.getString(key);
            case "Long" -> toml.getLong(key);
            case "Double" -> toml.getDouble(key);
            case "Boolean" -> toml.getBoolean(key);
            case "List" -> toml.getList(key);
            case "Map" -> toml.getTable(key);
            default -> null;
        };

        if (result == null) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        if (!clazz.isInstance(result)) {
            throw new IllegalArgumentException("Value for key " + key + " is not of type " + clazz.getSimpleName());
        }

        cachedObjects.put(key, result);
        return clazz.cast(result);
    }

    /**
     * Checks if the specified key exists in the TOML data.
     *
     * @param key The key to check for.
     * @return True if the key exists, false otherwise.
     */
    public boolean containsKey(String key) {
        return cachedObjects.containsKey(key) || toml.contains(key);
    }
}