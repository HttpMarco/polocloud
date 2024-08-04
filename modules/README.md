# Module System

## Table of Contents

1. [Steps for Creating a Module](#steps-for-creating-a-module)
    - [Detailed Steps](#detailed-steps-for-creating-a-module)
2. [Configuration](#configuration)
3. [Additional Features](#additional-features)
    - [Logging](#logging)

## Steps for Creating a Module
1. Implement the `CloudModule` Interface: Start by creating a new Java class that implements the CloudModule interface. This requires you to define the `onEnable` and `onDisable` methods.


2. Configure the Module: Create a module.json file in your resources folder with the necessary metadata (name, author, main class).


3. Build the Module: Compile your module into a JAR file.


4. Deploy the Module: Place the compiled JAR file into the local/modules directory of your Polocloud installation.


5. Start Polocloud: Launch Polocloud, which will automatically detect and load your module.


6. Implement Additional Features: Enhance your module with logging, event handling, configuration management, and other features as needed.

### Enjoy Building Your Module
Developing modules for Polocloud allows you to extend its functionality and customize it to your needs. Happy coding and enjoy building your module! 🤩

By following these steps and utilizing the examples provided, you can efficiently create and manage your own modules within the Polocloud environment, adding various features to enhance their capabilities.

---

# Detailed Steps for Creating a Module
To develop a module, you need to implement the `CloudModule` interface.
This interface requires you to define what happens when the module is enabled and disabled.

## Example

Here is a simple example of how to create a module:
```java
public class ExampleModule implements CloudModule {

    @Override
    public void onEnable() {
        // Code to execute when the module is enabled
    }

    @Override
    public void onDisable() {
        // Code to execute when the module is disabled
    }
}
```

## Configuration
To make your module recognized by the system, you need to create a configuration file named `module.json` in your resources folder.
This file should contain the following parameters:

- `name`: The name of your module
- `author`: Your name
- `main`: The fully qualified name of the main class of your module

### Example
Below is an example configuration for a module:
```json
{
  "name": "Example Module",
  "author": "RECHERGG",
  "main": "dev.httpmarco.polocloud.modules.example.ExampleModule"
}
```

After creating this file, place your compiled module in the local/modules directory.
When you start Polocloud, it will automatically detect and load your module.

# Additional Features
While the basic implementation of a module is straightforward, you might want to include additional features to enhance its functionality.
Here are some suggestions:

## Logging
Although logging is not required, it is highly recommended for debugging and monitoring purposes.
To log messages to the Cloud Console, you can use Log4j2. This is done by annotating your class with `@Log4j2`.
Once this annotation is added, you can access the `log` variable to output messages.

### Example
Here is how you can implement logging in your module:
```java
@Log4j2
public class ExampleModule implements CloudModule {

    @Override
    public void onEnable() {
        log.info("Example Module enabled");
        // Additional code for enabling the module
    }

    @Override
    public void onDisable() {
        log.info("Example Module disabled");
        // Additional code for disabling the module
    }
}
```

//TODO create commands<br>
//TODO use events
//TODO dependency