```kotlin
// Find the server group "lobby" from the runtime's group storage
val group = Agent.runtime.groupStorage().find("lobby")

// Define a property key for the BedWars variant, using a String type
val BW_VARIANT = Property<String>("bw-variant")

// Assign the property "bw-variant" with the value "8x1" to the group's properties
group.properties().with(BW_VARIANT, "8x1")

// Check if the property "bw-variant" exists in the group's properties
// -> returns TRUE, because it was set above
group.properties().has(BW_VARIANT)

// Retrieve the value of the property "bw-variant"
// -> returns "8x1"
val variant = group.properties().get(BW_VARIANT)

// Remove the property "bw-variant" from the group's properties
group.properties().remove(BW_VARIANT)
```