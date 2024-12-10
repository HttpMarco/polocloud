package dev.httpmarco.polocloud.node.i18n;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Accessors(fluent = true)
@AllArgsConstructor
public class I18nProvider implements I18n {

    @Getter
    private final String resourceBundlePrefix;
    private final ClassLoader classLoader = getClass().getClassLoader();

    @Override
    public String get(String key) {
        return get(key, Collections.emptyList());
    }

    @Override
    public String get(String key, Object... format) {
        return getDefault(key, format);
    }

    @Override
    public String getDefault(String key, Object... format) {
        return get(key, Locale.ENGLISH, format);
    }

    @Override
    public String get(String key, Locale locale, Object... format) {
        var resourceBundle = resourceBundle(locale);

        if (!resourceBundle.containsKey(key)) {
            return key;
        }

        if (format.length == 0) {
            return resourceBundle.getString(key);
        }

        return String.format(resourceBundle.getString(key), format);
    }

    @Override
    public ResourceBundle resourceBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle(this.resourceBundlePrefix, locale, this.classLoader);
        } catch (MissingResourceException e) {
            return defaultResourceBundle();
        }
    }

    @Override
    public ResourceBundle defaultResourceBundle() {
        return ResourceBundle.getBundle(this.resourceBundlePrefix, Locale.ENGLISH, this.classLoader);
    }
}
