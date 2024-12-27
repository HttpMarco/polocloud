plugins {
    id("polocloud.publishing")
    id("java-platform")
}

indra {
    configurePublications {
        from(components["javaPlatform"])
    }
}

dependencies {
    constraints {
        for (subproject in rootProject.subprojects) {
            if (subproject != project) {
                api(project(subproject.path))
            }
        }
    }
}