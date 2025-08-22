package dev.httpmarco.polocloud.modules.rest.controller

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.AuthProvider
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.AliveController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.AuthController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.SystemInformationController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.group.GroupController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.platform.PlatformController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.player.PlayerController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.role.RoleController
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.service.ServiceController
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
            AliveController(),
            UserController(),
            AuthController(),
            RoleController(),
            GroupController(),
            ServiceController(),
            PlayerController(),
            PlatformController(),
            SystemInformationController(),
            TerminalController()
        )

        this.controllers.forEach { handle(it) }
    }

    private fun registerControllers(vararg controller: Controller) {
        this.controllers.addAll(controller)
    }

    private fun handle(controller: Controller) {
       val basePath = API_PATH + controller.path
        controller::class.java.methods
            .filter { it.isAnnotationPresent(Request::class.java) }
            .forEach { registerRoute(it, controller, basePath) }
    }

    private fun registerRoute(method: Method, controller: Controller, basePath: String) {
        val annotation = method.getAnnotation(Request::class.java)
        val type = HandlerType.valueOf(annotation.requestType.name)
        val path = basePath + annotation.path
        val permission = annotation.permission

        RestModule.instance.httpServer.app.addHttpHandler(type, path) { ctx ->
            AuthProvider(RequestMethodData(method, controller, permission)).handle(ctx)
        }
    }

    fun processRequest(method: Method, controller: Controller, ctx: Context, user: User?, token: Token?) {
        try {
            if (ctx.result() == null) {
                val params = mutableListOf<Any?>()

                method.parameters.forEach { param ->
                    when (param.type) {
                        Context::class.java -> params.add(ctx)
                        User::class.java -> params.add(user)
                        Token::class.java -> params.add(token)
                        else -> params.add(null)
                    }
                }

                method.invoke(controller, *params.toTypedArray())
            }
        } catch (e: Exception) {
            ctx.status(500).result("Internal Server Error")
            e.printStackTrace()
        }
    }
}