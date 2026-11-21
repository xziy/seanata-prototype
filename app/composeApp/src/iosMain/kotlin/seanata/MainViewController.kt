package seanata

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/**
 * The whole app as one UIKit controller, which `iosApp/iosApp/ContentView.swift` wraps for SwiftUI.
 * The web build picks the first screen from the URL hash (Main.kt); a phone starts where a person
 * does, on Welcome.
 */
fun MainViewController(): UIViewController = ComposeUIViewController {
    App(start = Route.Welcome, onRouteChange = {})
}
