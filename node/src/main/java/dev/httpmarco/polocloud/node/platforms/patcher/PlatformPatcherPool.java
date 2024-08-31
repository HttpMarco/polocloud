package dev.httpmarco.polocloud.node.platforms.patcher;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public final class PlatformPatcherPool {

    private static final Map<String, PlatformPatcher> patcherList = new HashMap<>();

    static {
        register(new PlatformZipPatcher());
    }

    public void register(PlatformPatcher patcher) {
        patcherList.put(patcher.id(), patcher);
    }

    public PlatformPatcher patcher(String id) {
        return patcherList.get(id);
    }
}
