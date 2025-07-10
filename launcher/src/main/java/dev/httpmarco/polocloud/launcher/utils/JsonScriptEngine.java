package dev.httpmarco.polocloud.launcher.utils;

import dev.httpmarco.polocloud.launcher.dependencies.Dependency;
import lombok.experimental.UtilityClass;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class JsonScriptEngine {

    public static List<Dependency> loadDependencies(File jsonFile) throws Exception {
        // Read the file content as a single JSON string
        var json = Files.readString(jsonFile.toPath());

        // Use JavaScript engine to parse JSON into a JS array of objects
        var engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("jsonText", json);

        var raw = engine.eval("Java.asJSONCompatible(JSON.parse(jsonText))");

        if (!(raw instanceof List<?> list)) {
            throw new RuntimeException("Parsed JSON is not a list!");
        }

        List<Dependency> result = new ArrayList<>();
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) continue;

            result.add(new Dependency(
                    (String) map.get("group"),
                    (String) map.get("name"),
                    (String) map.get("version"),
                    (String) map.get("file"),
                    (String) map.get("guessedUrl"),
                    (String) map.get("sha256")
            ));
        }
        return result;
    }
}
