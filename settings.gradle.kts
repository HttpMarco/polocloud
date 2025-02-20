rootProject.name = "polocloud"
include("polocloud-suite")
include("polocloud-api")
include("polocloud-grpc")
include("polocloud-launcher")
include("polocloud-components")
include("polocloud-installer")
include("polocloud-components:component-terminal")
findProject(":polocloud-components:component-terminal")?.name = "component-terminal"
include("polocloud-components:component-factory-docker")
findProject(":polocloud-components:component-factory-docker")?.name = "component-factory-docker"
