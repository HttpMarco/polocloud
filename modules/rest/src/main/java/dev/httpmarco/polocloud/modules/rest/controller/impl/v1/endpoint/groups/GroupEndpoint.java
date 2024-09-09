package dev.httpmarco.polocloud.modules.rest.controller.impl.v1.endpoint.groups;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.controller.Controller;

public class GroupEndpoint extends Controller {

    public GroupEndpoint(RestModule restModule) {
        super(path, restModule);
    }
}
