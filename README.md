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

### Parsing a TOML File and Fetching Values

```java
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Specify the path to the TOML file
        File file = new File("/path/to/toml/file");

        // Create a CustomTomlFile instance
        CustomTomlFile tomlFile = new CustomTomlFile(file);

        // Fetch values from the TOML file
        String stringValue = tomlFile.fetch("stringKey", String.class);
        long longValue = tomlFile.fetch("longKey", Long.class);
        double doubleValue = tomlFile.fetch("doubleKey", Double.class);
        boolean booleanValue = tomlFile.fetch("booleanKey", Boolean.class);
        List<String> listValue = tomlFile.fetch("listKey", List.class);
        Map<String, Object> mapValue = tomlFile.fetch("mapKey", Map.class);

        // Access values from the TOML file
        System.out.println("String Value: " + stringValue);
        System.out.println("Long Value: " + longValue);
        System.out.println("Double Value: " + doubleValue);
        System.out.println("Boolean Value: " + booleanValue);
        System.out.println("List Value: " + listValue);
        System.out.println("Map Value: " + mapValue);
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
