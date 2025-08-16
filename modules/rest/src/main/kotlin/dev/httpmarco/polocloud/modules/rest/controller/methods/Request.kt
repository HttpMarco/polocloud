package dev.httpmarco.polocloud.modules.rest.controller.methods

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Request(
    val requestType: RequestType,
    val path: String = "",
    val permission: String = ""
)