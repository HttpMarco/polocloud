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

package dev.httpmarco.polocloud.proxy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.httpmarco.polocloud.api.CloudAPI;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File file;

    public Config() {
        File dir = new File("./plugins/PoloCloud-Proxy");
        if (!dir.exists()) dir.mkdirs();

        this.file = new File(dir,"config.json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                CloudAPI.instance().logger().error("Execption while creating config.yml for PoloCloud-Plugin", e);
            }
        }
    }

    public <T> T load(Class<T> clazz) {
        try {
            var reader = new FileReader(this.file);
            var result = GSON.fromJson(reader, clazz);
            reader.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void save(T config) {
        try {
            var writer = new FileWriter(this.file);
            GSON.toJson(config, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
