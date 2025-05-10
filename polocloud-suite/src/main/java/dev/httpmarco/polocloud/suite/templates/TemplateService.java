package dev.httpmarco.polocloud.suite.templates;

import dev.httpmarco.polocloud.suite.services.ClusterLocalService;
import dev.httpmarco.polocloud.suite.utils.PathUtils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
public final class TemplateService {

    public static final List<String> DEFAULT_TEMPLATES = List.of("EVERY", "EVERY_PROXY", "EVERY_SERVER", "EVERY_SERVICE");
    private static final Path TEMPLATES_PATH = Path.of("local").resolve("templates");

    private final List<Template> templates = new ArrayList<>();

    public TemplateService() {
        PathUtils.defineDirectory(TEMPLATES_PATH);

        // load default if not exists
        for (var template : DEFAULT_TEMPLATES) {
            PathUtils.defineDirectory(pathOfTemplate(template));
        }

        for (var templateFolder : Objects.requireNonNull(TEMPLATES_PATH.toFile().listFiles())) {
            if (!templateFolder.isDirectory()) {
                continue;
            }
            this.templates.add(new Template(templateFolder.getName()));
        }
    }

    public void register(String templateName) {
        this.templates.add(new Template(templateName));

        // create a new empty directory
        PathUtils.defineDirectory(pathOfTemplate(templateName));
    }

    @Contract(pure = true)
    private @NotNull Path pathOfTemplate(String template) {
        return TEMPLATES_PATH.resolve(template);
    }

    private Template find(String name) {
        return this.templates.stream().filter(it -> it.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void bindTemplates(@NotNull ClusterLocalService service) {
        for (var template : service.group().templates()) {
            find(template).bind(service);
        }
    }
}
