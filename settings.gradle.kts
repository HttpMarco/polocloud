rootProject.name = "polocloud"
include("api")
include("node")
include("launcher")
include("instance")
include("modules")
include("modules:example")
findProject(":modules:example")?.name = "example"
include("plugin")
include("modules:rest")
findProject(":modules:rest")?.name = "rest"
include("addons")
include("addons:proxy")
findProject(":addons:proxy")?.name = "proxy"
include("addons:hub")
findProject(":addons:hub")?.name = "hub"