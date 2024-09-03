package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.properties.Property;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupProperties {

    public final Property<Integer> MAX_PLAYERS = Property.ofInteger("maxPlayers");

}
