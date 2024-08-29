package dev.httpmarco.polocloud.node.platforms.patcher;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public final class PlatformPool {

    private static final Map<String, PlatformPatcher> patcherList = new HashMap<>();

    static {
        register("zip", new PlatformZipPatcher());
    }

    public void register(String id, PlatformPatcher patcher) {
        patcherList.put(id, patcher);
    }

    public PlatformPatcher patcher(String id) {
        return patcherList.get(id);
    }
}
