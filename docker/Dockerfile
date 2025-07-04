FROM eclipse-temurin:21-jre

WORKDIR /cloud

COPY ../agent/build/libs/polocloud-agent.jar /cloud/cloud-agent.jar

ENTRYPOINT ["java", "-jar", "/cloud/cloud-agent.jar"]