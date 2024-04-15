package org.saberdev.tomlizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class CustomTomlFileBuilder {
    /**
     * The path to the data folder where the custom TOML file will be created.
     */
    private final Path dataFolder;

    /**
     * The name of the custom TOML file, without the ".toml" extension.
     */
    private final String fileName;

    /**
     * A flag indicating whether the custom TOML file should be loaded from the project.
     */
    private final boolean loadFromProject;

    /**
     * The folder name where the custom TOML file will be created, if it's loaded from the project.
     */
    private final String inFolder;

    /**
     * Constructs a new instance of the {@code CustomTomlFileBuilder} class.
     *
     * @param builder the builder instance
     */
    public CustomTomlFileBuilder(Builder builder) {
        this.dataFolder = Objects.requireNonNull(builder.dataFolder);
        this.fileName = Objects.requireNonNull(builder.fileName) + ".toml";
        this.loadFromProject = builder.loadFromProject;
        this.inFolder = builder.inFolder != null ? builder.inFolder : "";
    }

    /**
     * A static nested class representing a builder for creating a custom TOML file.
     */
    public static class Builder {

        /**
         * The path to the data folder where the custom TOML file will be created.
         */
        private final Path dataFolder;

        /**
         * The name of the custom TOML file, without the ".toml" extension.
         */
        private String fileName;

        /**
         * A flag indicating whether the custom TOML file should be loaded from the project.
         */
        private boolean loadFromProject = false;

        /**
         * The folder name where the custom TOML file will be created, if it's loaded from the project.
         */
        private String inFolder;

        /**
         * Constructs a new instance of the {@code Builder} class.
         *
         * @param dataFolder the path to the data folder
         */
        public Builder(Path dataFolder) {
            this.dataFolder = Objects.requireNonNull(dataFolder);
        }

        /**
         * Sets the name of the custom TOML file.
         *
         * @param fileName the name of the custom TOML file
         * @return the builder instance
         */
        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * Sets the flag indicating whether the custom TOML file should be loaded from the project.
         *
         * @return the builder instance
         */
        public Builder loadFromProject() {
            this.loadFromProject = true;
            return this;
        }

        /**
         * Sets the folder name where the custom TOML file will be created, if it's loaded from the project.
         *
         * @param folderName the folder name
         * @return the builder instance
         */
        public Builder inFolder(String folderName) {
            this.inFolder = folderName;
            return this;
        }

        /**
         * Builds the custom TOML file builder and returns the {@code CustomTomlFile} instance.
         *
         * @return the {@code CustomTomlFile} instance
         * @throws IOException if an error occurs while building the custom TOML file
         */
        public CustomTomlFile build() throws IOException {
            return new CustomTomlFileBuilder(this).buildInternal();
        }
    }

    /**
     * Builds the custom TOML file.
     *
     * @return the {@code CustomTomlFile} instance
     * @throws IOException if an error occurs while building the custom TOML file
     */
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

    /**
     * Creates a new file in the specified directory.
     *
     * @param filePath the path to the file
     * @throws IOException if an error occurs while creating the file
     */
    private void createNewFile(Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);
    }

    /**
     * Extracts a resource from the classpath and saves it to the specified file path.
     *
     * @param filePath the path to the file
     * @throws IOException if an error occurs while extracting the resource
     */
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