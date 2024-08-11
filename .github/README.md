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



Properties example:
```java
PropertyRegister.register(Property.ofInteger("max_players"));
```

