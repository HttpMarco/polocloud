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

package dev.httpmarco.polocloud.base.logging;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Getter
@Accessors(fluent = true)
public final class Logger {

    private final LoggerFactory factory = new LoggerFactory();

    public Logger() {
        System.setErr(new PrintStream(new LoggerOutPutStream(true), true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(new LoggerOutPutStream(), true, StandardCharsets.UTF_8));
    }

    public void info(String message, Object... objects) {
        this.log(LogLevel.INFO, message, null, objects);
    }

    public void error(String message, Throwable throwable, Object... objects) {
        this.log(LogLevel.ERROR, message, throwable, objects);
    }

    public void success(String message, Object... objects) {
        this.log(LogLevel.SUCCESS, message, null, objects);
    }

    public void warn(String message, Object... objects) {
        this.log(LogLevel.WARN, message, null, objects);
    }

    private void log(LogLevel level, String message, Throwable throwable, Object... objects) {
        factory.print(level, message, throwable, objects);
    }
}
