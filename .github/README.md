<h1 align="center">PoloCloud</h1>
<h3 align="center">The simplest and easiest Cloud for Minecraft</h3>


## Installation

To install PoloCloud, follow these steps:

1. Download the latest release from the [releases page](https://github.com/HttpMarco/polocloud/releases/).
2. Go to the folder in which the downloaded jar is located and execute the following command there:
```bash
  java -jar polocloud.jar
```
3. Create a Proxy and Lobby Group using 'group create'
4. Set lobby as a fallback service with 'group lobby property set fallback true'
5. Connect to your Server using ip-address:DEFAULT-PROXY-PORT

## Developement API

⚠️ You need following repository:

1.1 Maven repository:
```xml
<repository>
    <id>polocloud-maven-snapshot</id>
    <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```

1.2 Gradle repository:
```groovy
maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
```

2.1. Maven dependency
```xml
<dependency>
  <groupId>dev.httpmarco.polocloud</groupId>
  <artifactId>api</artifactId>
  <version>1.0.0.5-SNAPSHOT</version>
</dependency>
```

2.2. Gradle dependency
```groovy
implementation 'dev.httpmarco.polocloud:api:1.0.0.5-SNAPSHOT'
```

2.3. Gradle Kotlin DLS Dependency
```groovy
implementation("dev.httpmarco.polocloud:api:1.0.0.5-SNAPSHOT")
```
