package dev.httpmarco.polocloud.suite.dependencies.external.pom;

import dev.httpmarco.polocloud.suite.dependencies.Dependency;
import dev.httpmarco.polocloud.suite.dependencies.exception.DependencyChecksumNotMatchException;
import dev.httpmarco.polocloud.suite.dependencies.external.ExternalDependency;
import dev.httpmarco.polocloud.suite.utils.downloading.Downloader;
import dev.httpmarco.polocloud.suite.utils.validator.ChecksumValidator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class Pom {

    private static final String POM_URL_PATTERN = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s";

    private final ExternalDependency parentDependency;
    private final String parentDependencyUrl;

    // all declared dependencies
    private final List<Dependency> dependencies = new ArrayList<>();

    public Pom(ExternalDependency dependency) {
        this.parentDependency = dependency;
        this.parentDependencyUrl = POM_URL_PATTERN.formatted("", "", "", "", "");

        // load pom, dependencies and versions
        this.loadContext(0);
    }

    private void loadContext(int retries) {
        // maximum retries are 3
        if (retries > 3) {
            // at the moment the checksum is not correct
            throw new DependencyChecksumNotMatchException(parentDependency);
        }

        var pomChecksum = Downloader.of(this.parentDependencyUrl).plain();

        try {
            var pomXml = Downloader.of("").xml();


            if(!ChecksumValidator.validateSHA1Checksum(pomXml, pomChecksum)) {
                this.loadContext(++retries);
            }

            // all fine we can download the dependencies

        } catch (ParserConfigurationException | NoSuchAlgorithmException | TransformerException e) {
            // another try for the downloading
            this.loadContext(++retries);
        }
    }
}