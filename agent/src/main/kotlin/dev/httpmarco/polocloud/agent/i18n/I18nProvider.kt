package dev.httpmarco.polocloud.agent.i18n

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.LoggingColor
import java.util.*

open class I18nProvider(private val resourceBundlePrefix: String) : I18n {

    private val utf8ResourceBundleControl = UTF8ResourceBundleControl()
    var locale = Agent.instance.config.locale // default/backup locale is English

    val SUPPORTED_LOCALES: List<Locale> = listOf(
        Locale.forLanguageTag("af"),
        Locale.forLanguageTag("ar"),
        Locale.forLanguageTag("ca"),
        Locale.SIMPLIFIED_CHINESE,
        Locale.TRADITIONAL_CHINESE,
        Locale.forLanguageTag("cs"),
        Locale.forLanguageTag("da"),
        Locale.GERMAN,
        Locale.forLanguageTag("el"),
        Locale.ENGLISH,
        Locale.forLanguageTag("es"),
        Locale.forLanguageTag("fi"),
        Locale.FRENCH,
        Locale.forLanguageTag("he"),
        Locale.forLanguageTag("hu"),
        Locale.ITALIAN,
        Locale.JAPANESE,
        Locale.forLanguageTag("ko"),
        Locale.forLanguageTag("nl"),
        Locale.forLanguageTag("no"),
        Locale.forLanguageTag("pl"),
        Locale.forLanguageTag("pt"),
        Locale.forLanguageTag("ro"),
        Locale.forLanguageTag("ru"),
        Locale.forLanguageTag("sr"),
        Locale.forLanguageTag("sv"),
        Locale.forLanguageTag("tr"),
        Locale.forLanguageTag("uk"),
        Locale.forLanguageTag("vi"),
        Locale.forLanguageTag("zh")
    )

    override fun get(key: String): String {
        return get(key, mutableListOf<Any?>())
    }

    fun info(key: String, vararg format: Any?) {
        logger.info(get(key, *format))
    }

    fun warn(key: String, vararg format: Any?) {
        logger.warn(get(key, *format))
    }

    fun error(key: String, vararg format: Any?) {
        logger.error(get(key, *format))
    }

    fun debug(key: String, vararg format: Any?) {
        logger.debug(get(key, *format))
    }

    override fun get(key: String, vararg format: Any?): String {
        val resourceBundle: ResourceBundle = this.resourceBundle(locale)

        if (!resourceBundle.containsKey(key)) {
            return key
        }
        val value = resourceBundle.getString(key)
        if (format.isEmpty()) {
            return LoggingColor.translate(value)
        }

        return LoggingColor.translate(String.format(value, *format))
    }

    override fun get(key: String, locale: Locale, vararg format: Any): String {
        return getDefault(key, *format)
    }

    override fun getDefault(key: String, vararg format: Any): String {
        return get(key, this.locale, format)
    }

    override fun resourceBundle(locale: Locale): ResourceBundle {
        return try {
            this.localBundle(locale)
        } catch (_: MissingResourceException) {
            defaultResourceBundle()
        }
    }

    override fun defaultResourceBundle(): ResourceBundle {
        return this.localBundle(this.locale)
    }

    fun localBundle(locale: Locale): ResourceBundle {
        return ResourceBundle.getBundle(
            this.resourceBundlePrefix,
            locale,
            this.javaClass.classLoader,
            utf8ResourceBundleControl
        )
    }
}