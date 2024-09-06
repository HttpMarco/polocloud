package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.properties.Property;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupProperties {

    public static final Property<Double> PERCENTAGE_TO_START_NEW_SERVER = Property.of("percentageToStartNewService", Double.class);

    public static final Property<Boolean> MAINTENANCE = Property.of("maintenance", Boolean.class);

}
