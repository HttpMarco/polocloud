package dev.httpmarco.polocloud.suite.i18n.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public final class UTF8Control extends ResourceBundle.Control {

    public static final UTF8Control INSTANCE = new UTF8Control();

    @Override
    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {

        if (!"java.properties".equals(format)) {
            return super.newBundle(baseName, locale, format, loader, reload);
        }

        var bundleName = toBundleName(baseName, locale);
        var resourceName = toResourceName(bundleName, "properties");

        try (var inputStream = getInputStream(resourceName, loader, reload);
             InputStreamReader isr = inputStream != null ? new InputStreamReader(inputStream, StandardCharsets.UTF_8) : null) {

            return isr != null ? new PropertyResourceBundle(isr) : super.newBundle(baseName, locale, format, loader, reload);
        }
    }

    private InputStream getInputStream(String resource, ClassLoader loader, boolean reload) throws IOException {
        if (reload) {
            URL url = loader.getResource(resource);
            if (url != null) {
                var connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        }
        return loader.getResourceAsStream(resource);
    }
}
