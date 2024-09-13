package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.model.groups;

import dev.httpmarco.polocloud.api.platforms.PlatformType;

public record PlatformModel(String platform, String version, PlatformType type) {
}
