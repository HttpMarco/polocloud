FROM eclipse-temurin:21-jre

WORKDIR /cloud

COPY ../launcher/build/libs/polocloud-launcher.jar /cloud/polocloud-launcher.jar

ENTRYPOINT ["java", "-jar", "/cloud/polocloud-launcher.jar"]