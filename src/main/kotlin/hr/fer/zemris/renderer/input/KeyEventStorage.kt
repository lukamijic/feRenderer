package hr.fer.zemris.renderer.input

import java.awt.event.KeyEvent

class KeyEventStorage {

    private val lock = Any()

    private var keyEvents: MutableList<KeyEvent> = mutableListOf()

    fun add(keyEvent: KeyEvent) = synchronized(lock) {
        keyEvents.add(keyEvent)
    }

    fun takeEvents(): List<KeyEvent> = synchronized(lock) {
        val events = keyEvents
        keyEvents = mutableListOf()
        return events
    }
}