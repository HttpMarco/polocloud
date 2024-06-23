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

package dev.httpmarco.polocloud.velocity.tablist;

import dev.httpmarco.polocloud.velocity.VelocityPlatform;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TablistConfiguration {

    private final static String HEADER = "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient>\n<gray>Simplest and easiest CloudSystem\n<gray>Current Server: <aqua>%server%";
    private final static String FOOTER = "<gray>Github: <white>HttpMarco <dark_gray>× <gray>Twitch: <light_purple>HttxMarco <dark_gray>× <gray>YouTube: <red>HttxMarco";
    private final VelocityPlatform platform;
    private Tablist tablist;

    public void load() {
        var result = this.platform.getConfig().load(Tablist.class);
        if (result == null) {
            this.tablist = new Tablist(HEADER, FOOTER);
            this.platform.getConfig().save(tablist);
            return;
        }

        var loadedHeader = (result.getHeader() == null) ? HEADER : result.getHeader();
        var loadedFooter = (result.getFooter() == null) ? FOOTER : result.getFooter();

        this.tablist = new Tablist(loadedHeader, loadedFooter);
        this.platform.getConfig().save(this.tablist);
    }
}
