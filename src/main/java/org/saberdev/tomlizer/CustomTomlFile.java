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

    public CustomTomlFile(File file) {
        this.file = file;
        this.toml = new Toml().read(file);
    }

    public void saveToFile() throws IOException {
        try (Writer writer = new FileWriter(file)) {
            new TomlWriter().write(toml, writer);
        }
    }

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

    public boolean containsKey(String key) {
        return cachedObjects.containsKey(key) || toml.contains(key);
    }
}