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

import dev.httpmarco.polocloud.api.properties.PropertyPool;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.pololcoud.common.files.FileUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class Template {

    private final String id;
    private final boolean canUsed;
    private final String[] mergedTemplates;
    private final PropertyPool properties;

    public Template(String id, String... mergedTemplates) {
        this.id = id;
        this.canUsed = true;
        this.mergedTemplates = mergedTemplates;
        this.properties = new PropertyPool();
    }

    @SneakyThrows
    public void copy(LocalCloudService localCloudService) {
        if (canUsed) {
            FileUtils.copyDirectoryContents(TemplatesService.TEMPLATES.resolve(id), localCloudService.runningFolder());
        }
        for (var templateName : this.mergedTemplates) {
            var template = Node.instance().templatesService().template(templateName);

            if (template != null) {
                template.copy(localCloudService);
            }
        }
    }
}
