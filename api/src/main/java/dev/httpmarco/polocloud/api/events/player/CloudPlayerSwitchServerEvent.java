package dev.httpmarco.polocloud.api.events.player;

import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CloudPlayerSwitchServerEvent implements CloudPlayerEvent {
    private final CloudPlayer cloudPlayer;
    private final CloudService cloudService;
}
