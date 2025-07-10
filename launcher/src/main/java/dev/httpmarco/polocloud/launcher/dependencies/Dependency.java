package dev.httpmarco.polocloud.launcher.dependencies;

public record Dependency (
        String group,
        String name,
        String version,
        String file,
        String url,
        String sha256 
) {}
