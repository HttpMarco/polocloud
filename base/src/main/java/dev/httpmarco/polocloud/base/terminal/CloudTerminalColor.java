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
public enum CloudTerminalColor {

    DEFAULT(209, 209, 209),
    DARK_GRAY(69, 69, 69),
    WHITE(255, 255, 255),
    INFO(125, 246, 255),
    WARNING(232, 164, 77),
    ERROR( 247, 74, 74),
    PROMPT(130, 234, 255),
    SUCCESS(157, 250, 178);

    public static final CloudTerminalColor[] colors = values();

    @Accessors(fluent = true)
    private final String ansiCode;

    CloudTerminalColor(int red, int green, int blue) {
        this.ansiCode = Ansi.ansi().a(Ansi.Attribute.RESET).fgRgb(red, green, blue).toString();
    }
}
