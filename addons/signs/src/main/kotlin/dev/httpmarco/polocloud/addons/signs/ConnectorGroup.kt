package dev.httpmarco.polocloud.addons.signs

/**
 * Represents a group of connectors identified by a unique ID.
 *
 * @property id The unique identifier for the connector group.
 */
class ConnectorGroup(val id: String) {

    // List of connectors that are bound to this group. -> no else
    private val bindConnectors = mutableListOf<Connector>()

}