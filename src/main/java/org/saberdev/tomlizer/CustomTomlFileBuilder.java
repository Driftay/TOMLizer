package org.saberdev.tomlizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class CustomTomlFileBuilder {
    private final Path dataFolder;
    private final String fileName;
    private final boolean loadFromProject;
    private final String inFolder;

    public CustomTomlFileBuilder(Builder builder) {
        this.dataFolder = Objects.requireNonNull(builder.dataFolder);
        this.fileName = Objects.requireNonNull(builder.fileName) + ".toml";
        this.loadFromProject = builder.loadFromProject;
        this.inFolder = builder.inFolder != null ? builder.inFolder : "";
    }

    public static class Builder {
        private final Path dataFolder;
        private String fileName;
        private boolean loadFromProject = false;
        private String inFolder;

        public Builder(Path dataFolder) {
            this.dataFolder = Objects.requireNonNull(dataFolder);
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder loadFromProject() {
            this.loadFromProject = true;
            return this;
        }

        public Builder inFolder(String folderName) {
            this.inFolder = folderName;
            return this;
        }

        public CustomTomlFile build() {
            try {
                return new CustomTomlFileBuilder(this).buildInternal();
            } catch (IOException e) {
                throw new RuntimeException("Failed to build custom TOML file", e);
            }
        }
    }

    private CustomTomlFile buildInternal() throws IOException {
        Path filePath = dataFolder.resolve(fileName);
        if (!Files.exists(filePath)) {
            if (loadFromProject) {
                extractResource(filePath);
            } else {
                createNewFile(filePath);
            }
        }
        return new CustomTomlFile(filePath.toFile());
    }

    private void createNewFile(Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);
    }

    private void extractResource(Path filePath) throws IOException {
        String resourcePath = inFolder.isEmpty() ? fileName : inFolder + "/" + fileName;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource '" + resourcePath + "' not found");
            }
            Files.createDirectories(filePath.getParent());
            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}