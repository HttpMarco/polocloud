package dev.httpmarco.polocloud.agent.security

import java.util.UUID

class SecurityProvider {

    val proxySecureToken = UUID.randomUUID().toString().substring(0,8)

}