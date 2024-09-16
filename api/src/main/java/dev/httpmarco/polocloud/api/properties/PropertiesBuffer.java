package dev.httpmarco.polocloud.api.properties;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.utils.JsonPoint;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public final class PropertiesBuffer {

    public void write(@NotNull PropertiesPool pool, @NotNull PacketBuffer buffer) {
        buffer.writeInt(pool.pool().size());
        pool.pool().forEach((property, o) -> {
            buffer.writeString(property.name());
            buffer.writeString(property.classType());
            buffer.writeString(JsonPoint.GSON.toJson(o));
        });
    }

    public void read(@NotNull PacketBuffer buffer, PropertiesPool pool) {
        var size = buffer.readInt();

        for (int i = 0; i < size; i++) {
            var property = new Property<>(buffer.readString(), buffer.readString());
            pool.pool().put(property, JsonPoint.GSON.fromJson(buffer.readString(), property.clazz()));
        }
    }

}
