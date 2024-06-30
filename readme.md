# Properties4J 📁

Properties4J is a Java library designed to monitor and manage changes in property files in real-time. It functions
similarly to an ORM (Object-Relational Mapping) framework, but for property files. This tool allows users to keep track
of property values and handle updates dynamically.

## Features ✨

- **Real-time Monitoring:** Automatically observe and respond to changes in property files.
- **Annotation-based Configuration:** Use annotations to define properties and observers.
- **Serializable Fields:** Supports Base64 encoding for serializable fields.
- **Dynamic Registration:** Register and manage configurations and observers at runtime.

## Installation 🚀

To include Properties4J in your project, add the following dependency to your `pom.xml` if you are using Maven:

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>com.github.queerzard</groupId>
    <artifactId>Properties4J</artifactId>
    <version>772b58ca8a</version>
</dependency>
</dependencies>
```

## Usage 🛠️

### Basic Setup

**Define Properties Class:**

```java
import java.util.HashMap;

@Properties(name = "example")
@Observe
public class ExampleProperties extends PropertiesBase {
    @ObjB64
    private HashMap<String, String> map = new HashMap<>();
    private String someProperty;

    // Getter and Setter
}
```

**Register Properties:**

```java
public class Main {
    public static void main(String[] args) {
        JProperties jProperties = new JProperties();
        jProperties.registerConfig(ExampleProperties.class);
    }
}
```

### Observers

Observers are automatically registered when the `@Observe` annotation is found.

**Default Observer:**

```java

public @interface Observe {
    Class<? extends AsyncPropertiesObserver> observer() default GlobalPropertiesObserver.class;
}
```

To create a custom observer:

```java
public class ExampleObserver extends AsyncPropertiesObserver {
    @Override
    public <T extends PropertiesBase> boolean onChange(T propertiesObject, Field field, Object oldValue, Object newValue) {
        // Handle property change
        return true;
    }

    @Override
    protected void onPropertyUnchanged(PropertiesBase propertiesBase, String key, String currentValue) {
        // Handle unchanged property
    }
}
```

### Configuration Management

- **Load Properties:** Automatically loaded and parsed.
- **Save Properties:** Changes to properties can be saved back to the file.

**Utility Methods:**

**Obtain Configuration:**

```java
ExampleProperties config = (ExampleProperties) jProperties.obtainConfig(ExampleProperties.class);
```

**Unregister Configuration:**

```java
jProperties.unregisterConfig(ExampleProperties .class);
```

## Classes and Interfaces 📚

- **JProperties:** Manages the registration and monitoring of properties and observers.
- **PropertiesBase:** Base class for defining property configurations.
- **PropertiesObserverWorker:** Handles observation logic in a separate thread.
- **AsyncPropertiesObserver:** Abstract class for creating observers that handle property changes asynchronously.
- **GlobalPropertiesObserver:** Example implementation of a global properties observer.
- **PropertiesFile:** Handles reading and writing of property files.
- **PropertiesState:** Maintains the state of properties for comparison.
- **AnnotationProcessor:** Processes annotations for serialization and deserialization.
- **IObserver & ISubInstanceHandover:** Interfaces defining observer pattern and post-construction handover methods.

## Annotations 🎨

- `@Properties`: Defines a properties class with a specific name.
- `@ObjB64`: Marks a field as serializable with Base64 encoding.
- `@Observe`: Indicates that a properties class or field should be observed for changes. Allows specifying a custom
  observer class, with GlobalPropertiesObserver as the default.
- `@Register`: Marks a class for automatic registration.

## Contributing 🤝

Contributions are welcome! Please fork the repository and submit pull requests for any enhancements or bug fixes.

## License 📜

This project is licensed under the GNU General Public License v3.0. See the LICENSE file for details.



