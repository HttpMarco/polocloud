package dev.httpmarco.polocloud.suite.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public interface I18n {

    /**
     * @return the resource bundle prefix
     */
    String resourceBundlePrefix();

    /**
     * The default language is used here.
     * @param key the key of translation
     * @return the translation for the key
     */
    String get(String key);

    /**
     * The default language is used here.
     * @param key the key of translation
     * @param format format the translation with argument {0} -> format
     * @return the translation for the key
     */
    String get(String key, Object... format);

    /**
     * @param key the key of translation
     * @param locale the locale of the translation
     * @param format format the translation with argument {0} -> format
     * @return the translation for the key
     */
    String get(String key, Locale locale, Object... format);

    /**
     * The default langauge is used here.
     * @param key the key of translation
     * @param format format the translation with argument {0} -> format
     * @return the translation for the key
     */
    String getDefault(String key, Object... format);

    /**
     * @param locale the locale of the translation
     * @return the resource bundle for the locale
     */
    ResourceBundle resourceBundle(Locale locale);

    /**
     * @return the resource bundle for the default language
     */
    ResourceBundle defaultResourceBundle();

}