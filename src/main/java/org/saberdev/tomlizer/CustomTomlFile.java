package org.saberdev.tomlizer;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomTomlFile {
    private final File file;
    private final Toml toml;
    private final Map<String, Object> cachedObjects = new HashMap<>();

    public CustomTomlFile(File file) {
        this.file = file;
        this.toml = new Toml().read(file);
    }

    public void saveToFile() throws IOException {
        try (Writer writer = new FileWriter(file)) {
            new TomlWriter().write(toml, writer);
        }
    }

    public Toml getToml() {
        return toml;
    }

    public boolean containsKey(String key) {
        return cachedObjects.containsKey(key) || toml.contains(key);
    }

    public String fetchString(String key) {
        return (String) getObj(key, DataTypes.STRING);
    }

    public long fetchLong(String key) {
        return (long) getObj(key, DataTypes.LONG);
    }

    public double fetchDouble(String key) {
        return (double) getObj(key, DataTypes.DOUBLE);
    }

    public List<String> fetchStringList(String key) {
        return (List<String>) getObj(key, DataTypes.STRINGLIST);
    }

    public boolean fetchBoolean(String key) {
        return (boolean) getObj(key, DataTypes.BOOLEAN);
    }

    public Map<String, Object> fetchMap(String key) {
        return (Map<String, Object>) getObj(key, DataTypes.MAP);
    }

    private Object getObj(String key, DataTypes data) {
        if (cachedObjects.containsKey(key)) {
            return cachedObjects.get(key);
        }

        Object result = switch (data) {
            case STRING -> toml.getString(key);
            case LONG -> toml.getLong(key);
            case DOUBLE -> toml.getDouble(key);
            case BOOLEAN -> toml.getBoolean(key);
            case STRINGLIST -> toml.getList(key);
            default -> null;
        };

        cachedObjects.put(key, result);
        return result;
    }

    public enum DataTypes {
        STRING, LONG, DOUBLE, STRINGLIST, BOOLEAN, MAP
    }
}