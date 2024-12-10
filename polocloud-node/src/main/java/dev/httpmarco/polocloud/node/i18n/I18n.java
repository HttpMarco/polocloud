package dev.httpmarco.polocloud.node.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public interface I18n {

    /**
     * @return the resource bundle prefix
     */
    String resourceBundlePrefix();

    /**
     * The default language is used here.
     * @param key the key of the translation
     * @return the translation for the key
     */
    String get(String key);

    /**
     * The default language is used here.
     * @param key the key of the translation
     * @param format format the translation with arguments {0} -> format
     * @return the translation for the key
     */
    String get(String key, Object... format);

    /**
     * The default language is used here.
     * @param key the key of the translation
     * @return the translation for the key
     */
    String getDefault(String key, Object... format);

    /**
     * @param key the key of the translation
     * @param locale the locale of the translation
     * @param format format the translation with arguments {0} -> format
     * @return the translation for the key
     */
    String get(String key, Locale locale, Object... format);

    /**
     * @param locale the locale of the translation
     * @return the translation resource bundle
     */
    ResourceBundle resourceBundle(Locale locale);

    /**
     * @return the resource bundle of the default language
     */
    ResourceBundle defaultResourceBundle();
}
