package dev.httpmarco.polocloud.modules.rest.auth;

import java.util.UUID;

public record TokenInformation(UUID userUUID, String ip, String userAgent) {

}
