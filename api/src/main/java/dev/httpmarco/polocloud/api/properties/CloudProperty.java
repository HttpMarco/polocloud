package dev.httpmarco.polocloud.api.properties;

public final class CloudProperty<T> extends Property<T> {

    public CloudProperty(String id, Class<T> type) {
        super(id, type);
    }

    public static CloudProperty<String> PROMPT = new CloudProperty<>("prompt", String.class);

}
