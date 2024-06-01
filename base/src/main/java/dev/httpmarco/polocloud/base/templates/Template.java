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

import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.commons.io.FileUtils;

@Getter
@Accessors(fluent = true)
public final class Template {

    private final String id;
    private final boolean canUsed;
    private final String[] mergedTemplates;
    private final PropertiesPool<TemplateProperties<?>> properties;

    public Template(String id, String... mergedTemplates) {
        this.id = id;
        this.canUsed = true;
        this.mergedTemplates = mergedTemplates;
        this.properties = new PropertiesPool<>();
    }

    @SneakyThrows
    public void copy(LocalCloudService localCloudService) {
        if (canUsed) {
            FileUtils.copyDirectory(TemplatesService.TEMPLATES.resolve(id).toFile(), localCloudService.runningFolder().toFile());
        }

        for (var templateName : this.mergedTemplates) {
            var template = CloudBase.instance().templatesService().templates(templateName);

            if (template != null) {
                template.copy(localCloudService);
            }
        }
    }
}
