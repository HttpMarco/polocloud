FROM eclipse-temurin:21-jre

WORKDIR /cloud

ARG POLOCLOUD_VERSION=dev
ENV POLOCLOUD_VERSION=$POLOCLOUD_VERSION

COPY /build/libs/polocloud-launcher.jar /cloud/polocloud-launcher.jar

ENTRYPOINT ["java", "-jar", "/cloud/polocloud-launcher.jar"]