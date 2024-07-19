/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.base.templates;

import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.common.PropertiesPoolSerializer;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.pololcoud.common.files.FileUtils;
import dev.httpmarco.pololcoud.common.document.Document;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class TemplatesService {

    public static final Path TEMPLATES = FileUtils.createDirectory("templates");

    @Getter(AccessLevel.PACKAGE)
    private final Document<TemplatesConfig> document;

    public TemplatesService() {
        this.document = new Document<>(Path.of("templates/templates.json"), new TemplatesConfig(), PropertiesPoolSerializer.ADAPTER);

        //todo check if a static service is present
        FileUtils.createDirectory("static");

        this.createTemplates("every");
        this.createTemplates("every_server");
        this.createTemplates("every_proxy");
    }

    public boolean createTemplates(String name, String... mergedTemplates) {
        if (isTemplate(name)) {
            return false;
        }

        var template = new Template(name, mergedTemplates);
        FileUtils.createDirectory(TEMPLATES.resolve(name));
        templates().add(template);
        this.document.save();
        return true;
    }

    public boolean isTemplate(String id) {
        return templates().stream().anyMatch(it -> it.id().equalsIgnoreCase(id));
    }

    public void cloneTemplate(LocalCloudService service) {
        if (service.group().properties().has(GroupProperties.TEMPLATES)) {
            var temp = Node.instance().templatesService().template(service.group().properties().property(GroupProperties.TEMPLATES));
            if (temp != null) {
                temp.copy(service);
            }
        }
    }

    @SneakyThrows
    public boolean deleteTemplate(String name) {
        if (!isTemplate(name)) {
            return false;
        }

        for (var file : TEMPLATES.toFile().listFiles()) {
            if (!file.getName().equalsIgnoreCase(name)) {
                return false;
            }
            FileUtils.delete(file);
            this.templates().removeIf(template -> template.id().equals(name));
            this.document.value().templates().removeIf(template -> template.id().equals(name));
            this.document.update();
        }
        return true;
    }

    public List<Template> templates() {
        return this.document.value().templates();
    }

    public Template template(String name) {
        return templates().stream().filter(it -> it.id().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
