package dev.httpmarco.polocloud.suite.platforms.files;

import dev.httpmarco.polocloud.common.files.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class FilePrepareProcess {

    private FileType type;
    private String name;
    private FilePrepareFlag flag;
    private String content;

    @Contract(" -> new")
    public @NotNull File file() {
        return new File(name + type.suffix());
    }
}
