rootProject.name = "polocloud"
include("api")
include("runner")
include("base")
include("instance")
include("plugin")
include("addons")
include("addons:proxy-addon")
findProject(":addons:proxy-addon")?.name = "proxy-addon"
include("addons:sign-addon")
findProject(":addons:sign-addon")?.name = "sign-addon"
