package dev.httpmarco.polocloud.api.platform;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.Version;

public record SharedPlatform(String name, Version version, PlatformType type) implements Named {

}
