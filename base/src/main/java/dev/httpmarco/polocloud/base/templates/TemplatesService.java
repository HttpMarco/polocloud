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

import dev.httpmarco.osgan.files.OsganFile;
import dev.httpmarco.osgan.files.OsganFileCreateOption;
import dev.httpmarco.osgan.files.OsganFileDocument;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class TemplatesService {

    public static final Path TEMPLATES = OsganFile.define("templates", OsganFileCreateOption.CREATION).path();
    public static final Path STATIC = OsganFile.define("static", OsganFileCreateOption.CREATION).path();

    @Getter(AccessLevel.PACKAGE)
    private final OsganFileDocument<TemplatesConfig> document;

    public TemplatesService() {
        this.document = OsganFile.define("templates/templates.json").asDocument(new TemplatesConfig());

        this.createTemplates("every");
        this.createTemplates("every_server");
        this.createTemplates("every_proxy");
    }

    public boolean createTemplates(String name, String... mergedTemplates) {
        if (isTemplate(name)) {
            return false;
        }

        var directory = TEMPLATES.resolve(name);
        var template = new Template(name, mergedTemplates);
        OsganFile.create(directory);
        templates().add(template);
        this.document.update();
        return true;
    }

    public boolean isTemplate(String id) {
        return templates().stream().anyMatch(it -> it.id().equalsIgnoreCase(id));
    }

    public void cloneTemplate(LocalCloudService service) {
        if (service.group().properties().has(GroupProperties.TEMPLATES)) {
            var temp = CloudBase.instance().templatesService().template(service.group().properties().property(GroupProperties.TEMPLATES));
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
            FileUtils.deleteDirectory(file);
            //todo check work? String? Object remove?
            this.templates().remove(name);
            this.document.content().templates().remove(name);
            this.document.update();
        }
        return true;
    }

    public List<Template> templates() {
        return this.document.content().templates();
    }

    public Template template(String name) {
        return templates().stream().filter(it -> it.id().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
