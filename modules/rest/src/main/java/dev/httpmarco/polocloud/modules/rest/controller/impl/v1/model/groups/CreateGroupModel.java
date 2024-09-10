package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.model.groups;

public record CreateGroupModel(String name, String platform, String version, int maxMemory, boolean staticService, int minOnlineServices, int maxOnlineServices, boolean fallback) {
}
