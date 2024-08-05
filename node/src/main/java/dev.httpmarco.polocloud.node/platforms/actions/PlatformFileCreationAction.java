package dev.httpmarco.polocloud.node.platforms.actions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class PlatformFileCreationAction implements PlatformAction {

    private final String propertyId = "file-creation";
    private final String fileName;
    private final String content;

}
