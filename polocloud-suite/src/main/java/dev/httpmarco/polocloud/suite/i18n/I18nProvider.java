package dev.httpmarco.polocloud.suite.i18n;

import dev.httpmarco.polocloud.component.api.logging.LoggingColors;
import dev.httpmarco.polocloud.suite.i18n.utils.UTF8Control;

import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18nProvider implements I18n {

    private final String resourceBundlePrefix;

    public I18nProvider(String resourceBundlePrefix) {
        this.resourceBundlePrefix = resourceBundlePrefix;
    }

    @Override
    public String get(String key) {
        return get(key, Collections.emptyList());
    }

    @Override
    public String get(String key, Object... format) {
        return getDefault(key, format);
    }

    @Override
    public String get(String key, Locale locale, Object... format) {
        var resourceBundle = this.resourceBundle(locale);

        if(!resourceBundle.containsKey(key)) {
            return key;
        }
        if(format.length == 0) {
            return LoggingColors.translate(resourceBundle.getString(key));
        }
        return LoggingColors.translate(String.format(resourceBundle.getString(key), format));
    }

    @Override
    public String getDefault(String key, Object... format) {
        return get(key, Locale.ENGLISH, format);
    }

    @Override
    public ResourceBundle resourceBundle(Locale locale) {
        try {
            return this.localBundle(locale);
        }catch (MissingResourceException exception) {
            return defaultResourceBundle();
        }
    }

    @Override
    public ResourceBundle defaultResourceBundle() {
        return this.localBundle(Locale.ENGLISH);
    }

    public ResourceBundle localBundle(Locale locale) {
        return ResourceBundle.getBundle(this.resourceBundlePrefix, locale, this.getClass().getClassLoader(), UTF8Control.INSTANCE);
    }

    @Override
    public String resourceBundlePrefix() {
        return this.resourceBundlePrefix;
    }

}