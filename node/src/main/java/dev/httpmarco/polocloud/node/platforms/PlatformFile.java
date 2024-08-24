package dev.httpmarco.polocloud.node.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class PlatformFile {

    private String file;
    private PlatformFileStrategy strategy;
    private List<PlatformFileReplacement> replacements;
    private List<String> appends;

}
