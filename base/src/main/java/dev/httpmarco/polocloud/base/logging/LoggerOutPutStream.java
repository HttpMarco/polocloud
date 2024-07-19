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

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@AllArgsConstructor
public final class LoggerOutPutStream extends ByteArrayOutputStream {

    private boolean errorStream = false;

    @Override
    public void flush() throws IOException {
        super.flush();

        var input = this.toString(StandardCharsets.UTF_8);
        super.reset();

        if (input != null && !input.isEmpty()) {
            input = input.replace("\n", "");
            if (!errorStream) {
                CloudAPI.instance().logger().info(input);
            } else {
                CloudAPI.instance().logger().error(input, null);
            }
        }
    }
}
