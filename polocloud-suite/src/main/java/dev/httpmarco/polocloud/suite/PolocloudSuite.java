package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.suite.i18n.I18n;
import dev.httpmarco.polocloud.suite.i18n.impl.I18nPolocloudSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PolocloudSuite extends Polocloud {

    private static final Logger log = LogManager.getLogger(PolocloudSuite.class);

    private static final I18n translation = new I18nPolocloudSuite();

    public PolocloudSuite() {
        // todo start context
        log.info(translation.get("suite.starting"));
    }
}