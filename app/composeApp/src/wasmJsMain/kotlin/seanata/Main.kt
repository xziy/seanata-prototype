@file:OptIn(ExperimentalWasmJsInterop::class)

package seanata

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

/**
 * Entry point. The URL hash picks the first screen, so each one can be opened (and screenshotted)
 * directly: `#welcome`, `#home`, `#courses`, `#course`, `#program`, `#schedule`, `#training`, `#booked`,
 * `#logbook`, `#profile`.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val start = Route.fromHash(locationHash())
    ComposeViewport {
        App(start = start, onRouteChange = { setLocationHash(it.hash) })
    }
}

private fun locationHash(): String = js("window.location.hash")

private fun setLocationHash(hash: String): Unit = js("{ history.replaceState(null, '', '#' + hash); }")
