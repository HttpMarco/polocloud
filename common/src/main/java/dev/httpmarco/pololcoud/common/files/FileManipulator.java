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

package dev.httpmarco.pololcoud.common.files;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.ArrayList;

@UtilityClass
public final class FileManipulator {

    public static void manipulate(final File file, final ConfigReplace configReplace) {
        final var lines = new ArrayList<String>();

        try (final var bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (final var fileWriter = new FileWriter(file)) {
            for (final var line : lines) {
                fileWriter.write(configReplace.replace(line) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public interface ConfigReplace {
        String replace(String s);
    }
}
