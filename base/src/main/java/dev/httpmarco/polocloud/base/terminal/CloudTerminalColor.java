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

package dev.httpmarco.polocloud.base.terminal;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jline.jansi.Ansi;

@Getter
@Accessors(fluent = true)
public enum CloudTerminalColor {

    LIGHT_GRAY('7', Ansi.ansi().reset().fg(Ansi.Color.WHITE)),
    LIME('a', Ansi.ansi().reset().fg(Ansi.Color.GREEN)),
    GRAY('8', Ansi.ansi().reset().fg(Ansi.Color.BLACK).bold()),
    AQUA('b', Ansi.ansi().reset().fg(Ansi.Color.CYAN).bold());

    public static final CloudTerminalColor[] colors = values();

    private final char key;
    private final String ansiCode;

    CloudTerminalColor(char key, Ansi ansi) {
        this.key = key;
        this.ansiCode = ansi.toString();
    }
}
