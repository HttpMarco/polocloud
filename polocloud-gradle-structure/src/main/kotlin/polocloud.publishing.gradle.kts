plugins {
    id("net.kyori.indra.publishing")
}

/* Set up publishing */
indra {
    apache2License()

    signWithKeyFromPrefixedProperties("polocloud")

    configurePublications {
        pom {
            inceptionYear.set("2024")

            developers {
                developer {
                    // TODO
                    id.set("polus")
                }
            }
        }
    }
}