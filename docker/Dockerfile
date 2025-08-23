FROM eclipse-temurin:21-jre

WORKDIR /cloud

COPY ../agent/build/libs/polocloud-agent-3.0.0-pre.6-SNAPSHOT-all.jar /cloud/polocloud-launcher.jar

ENTRYPOINT ["java", "-jar", "/cloud/polocloud-launcher.jar"]