package dev.httpmarco.polocloud.modules.rest.controller

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.AuthProvider
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.HealthController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.AuthController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.SystemInformationController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.group.GroupController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.platform.PlatformController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.player.PlayerController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.role.RoleController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.service.ServiceController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.template.TemplateController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.terminal.TerminalController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.user.UserController
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestMethodData
import io.javalin.http.Context
import io.javalin.http.HandlerType
import java.lang.reflect.Method

class ControllerProvider {

    companion object {
        const val API_PATH = "/polocloud/api/v3"
    }

    private val controllers = mutableListOf<Controller>()

    init {
        registerControllers(
            HealthController(),
            UserController(),
            AuthController(),
            RoleController(),
            PlatformController(),
            TemplateController(),
            GroupController(),
            ServiceController(),
            PlayerController(),
            SystemInformationController(),
            TerminalController()
        ).forEach(::registerControllerRoutes)
    }

    private fun registerControllers(vararg controllerInstances: Controller): List<Controller> {
        controllers.addAll(controllerInstances)
        return controllerInstances.toList()
    }

    private fun registerControllerRoutes(controller: Controller) {
        val basePath = API_PATH + controller.path

        controller::class.java.methods
            .filter { it.isAnnotationPresent(Request::class.java) }
            .forEach { method ->
                val annotation = method.getAnnotation(Request::class.java)
                val type = HandlerType.valueOf(annotation.requestType.name)
                val fullPath = basePath + annotation.path

                RestModule.instance.httpServer.app.addHttpHandler(type, fullPath) { context ->
                    AuthProvider(RequestMethodData(method, controller, annotation.permission)).handle(context)
                }
            }
    }

    fun processRequest(method: Method, controller: Controller, context: Context, user: User?, token: Token?) {
        try {
            if (context.result() != null) return

            val args = method.parameters.map { param ->
                when (param.type) {
                    Context::class.java -> context
                    User::class.java -> user
                    Token::class.java -> token
                    else -> null
                }
            }.toTypedArray()

            method.invoke(controller, *args)
        } catch (e: Exception) {
            context.defaultResponse(500,"Internal Server Error")
            e.printStackTrace()
        }
    }
}