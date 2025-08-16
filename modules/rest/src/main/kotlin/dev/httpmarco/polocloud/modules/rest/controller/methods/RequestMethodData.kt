package dev.httpmarco.polocloud.modules.rest.controller.methods

import dev.httpmarco.polocloud.modules.rest.controller.Controller
import java.lang.reflect.Method

data class RequestMethodData(
    val method: Method,
    val controller: Controller,
    val permission: String
)