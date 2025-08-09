package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame

/**
 * A visual connector that displays service-related information using a defined layout and animation frames.
 * Connectors can be signs, banners, or any visual representation.
 *
 * @param A The animation frame type used by this connector
 * @property basedConnectorData The configuration data that defines how this connector behaves
 */
abstract class Connector<A : AnimationFrame>(
    val basedConnectorData: BasedConnectorData<A>
) {

    /**
     * The currently displayed service on this connector. May be null if unassigned.
     */
    var displayedService: Service? = null

    /**
     * The current visual state of the connector, determined dynamically.
     */
    var state: ConnectorState = detectState()

    /**
     * Displays the given animation frame on this connector.
     *
     * @param frame The animation frame to display
     */
    abstract fun display(frame: A)

    /**
     * Binds this connector to a specific service and updates its visual output accordingly.
     *
     * @param service The service to bind
     */
    fun bindWith(service: Service) {
        this.displayedService = service
        this.update()
    }

    /**
     * Unbinds any previously bound service from this connector and resets its visual output.
     */
    fun unbind() {
        this.displayedService = null
        this.update()
    }

    /**
     * Re-evaluates the connector's state and updates the displayed animation frame accordingly.
     */
    fun update() {
        this.state = detectState()
        val frame = basedConnectorData.layout.frames[state]?.firstOrNull()

        if (frame != null) {
            display(frame)
        } else {
            // Optionally log warning about missing frame for this state
        }
    }

    /**
     * Dynamically determines the current state of the connector based on its service.
     *
     * @return The connector state (LOADING, ONLINE, PLAYERS, FULL)
     */
    private fun detectState(): ConnectorState {
        val service = displayedService ?: return ConnectorState.LOADING

        return when {
            service.playerCount == 0 -> ConnectorState.ONLINE
            service.playerCount < service.maxPlayerCount -> ConnectorState.PLAYERS
            else -> ConnectorState.FULL
        }
    }

    /**
     * Replaces placeholders in a text line with actual service values.
     *
     * Supported placeholders:
     * - %group%
     * - %service%
     * - %online_players%
     * - %max_players%
     * - %state%
     *
     * @param line The text line containing placeholders
     * @return A string with placeholders replaced by actual values
     */
    fun replaceText(line: String): String {
        val service = displayedService ?: return line

        return line
            .replace("%group%", service.groupName)
            .replace("%service%", service.name())
            .replace("%online_players%", service.playerCount.toString())
            .replace("%max_players%", service.maxPlayerCount.toString())
            .replace("%motd%", service.motd)
            .replace("%state%", state.name)
    }
}
