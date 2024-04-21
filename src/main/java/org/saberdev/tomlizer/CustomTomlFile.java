package org.saberdev.tomlizer;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
        Object cachedValue = cachedObjects.get(key);
        if (cachedValue != null && clazz.isInstance(cachedValue)) {
            return clazz.cast(cachedValue);
        }

        Function<String, Object> retrievalFunction = getRetrievalFunction(clazz);
        if (retrievalFunction == null) {
            throw new IllegalArgumentException("Unsupported type: " + clazz.getSimpleName());
        }

        Object result = retrievalFunction.apply(key);
        if (result == null) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        if (!clazz.isInstance(result)) {
            throw new IllegalArgumentException("Value for key " + key + " is not of type " + clazz.getSimpleName());
        }

        cachedObjects.put(key, result);
        return clazz.cast(result);
    }

    private Function<String, Object> getRetrievalFunction(Class<?> clazz) {
        switch (clazz.getSimpleName()) {
            case "String":
                return toml::getString;
            case "Long":
                return toml::getLong;
            case "Double":
                return toml::getDouble;
            case "Boolean":
                return toml::getBoolean;
            case "List":
                return toml::getList;
            case "Map":
                return toml::getTable;
            default:
                return null;
        }
    }

    public boolean containsKey(String key) {
        return cachedObjects.containsKey(key) || toml.contains(key);
    }
}