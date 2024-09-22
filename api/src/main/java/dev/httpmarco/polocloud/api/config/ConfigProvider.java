package dev.httpmarco.polocloud.api.config;

public interface ConfigProvider {

    <T> void createConfig(String fileName, T defaultValue);

    <T> T readConfig(String fileName, Class<T> tClass);

    <T> T readConfigOrCreate(String fileName, Class<T> tClass, T defaultValue);

    boolean configExists(String fileName);

}
