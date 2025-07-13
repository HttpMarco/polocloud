package dev.httpmarco.polocloud.agent.security

import java.util.UUID

class SecurityProvider {

    private val proxySecureToken = UUID.randomUUID().toString().substring(10)

}