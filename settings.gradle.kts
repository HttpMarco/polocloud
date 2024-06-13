rootProject.name = "polocloud"
include("api")
include("runner")
include("base")
include("instance")
include("plugin")
include("addon")
include("modules")
include("modules:syncproxy")
findProject(":modules:syncproxy")?.name = "syncproxy"
