package dev.httpmarco.polocloud.updater;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class UpdateResult {

    private final UpdateState state;

    public void update() {

    }
}
