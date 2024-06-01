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

package dev.httpmarco.polocloud.api.logging;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;

public final class LoggerFactory implements LoggerHandler {

    private final Set<LoggerHandler> handlers = new LinkedHashSet<>();

    public void registerLoggers(LoggerHandler... loggerHandler) {
        this.handlers.addAll(Arrays.stream(loggerHandler).toList());
    }

    @Override
    public void print(LogLevel level, String message, Throwable throwable, Object... objects) {
        handlers.forEach(it -> it.print(level, message, throwable, objects));
    }

    @Override
    public void close() {
        this.handlers.forEach(LoggerHandler::close);
    }
}
