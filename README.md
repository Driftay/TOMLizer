# TOMLizer

TOMLizer is a Java utility library for easy creation, manipulation, and parsing of TOML (Tom's Obvious, Minimal Language) files. It provides a fluent builder interface for generating TOML files programmatically, along with utilities for parsing TOML into Java objects.

## Features

- Fluent builder interface for creating TOML files
- Support for specifying custom file paths
- Option to load TOML files from project resources
- Exception handling for file creation and resource extraction

## Usage

### Creating a TOML File

```java
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Create a builder instance with the data folder
        CustomTomlFileBuilder.Builder builder = new CustomTomlFileBuilder.Builder(Paths.get("/path/to/data/folder"));

        // Customize the builder with additional options
        builder.fileName("example")           // Specify the file name
               .loadFromProject()            // Load from project resources
               .inFolder("config");          // Specify the folder within resources

        // Build the TOML file
        CustomTomlFile tomlFile = builder.build();

        // Use the tomlFile instance as needed
        // ...
    }
}
```

### Parsing a TOML File

```java
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Specify the path to the TOML file
        Path filePath = Paths.get("/path/to/toml/file");

        // Create a CustomTomlFile instance
        CustomTomlFile tomlFile = new CustomTomlFile(filePath.toFile());

        // Access values from the TOML file
        String value = tomlFile.getToml().getString("key");
        System.out.println("Value: " + value);
    }
}
```

## Installation

To use TOMLizer in your Maven project, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.saberdev</groupId>
    <artifactId>TOMLizer</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Support & Help

For support and assistance, join our community on Discord: [discord.gg/saber](discord.gg/saber)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
