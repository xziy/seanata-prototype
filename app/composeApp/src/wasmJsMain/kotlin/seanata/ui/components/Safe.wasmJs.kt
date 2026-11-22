@file:OptIn(ExperimentalWasmJsInterop::class)

package seanata.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

/**
 * In a browser the safe area is CSS's `env(safe-area-inset-*)` (index.html asks for `viewport-fit=cover`).
 * `?safe=47,34` in the address sets it by hand, to see a notch on a desktop.
 */
@Composable
actual fun safeArea(): Safe = remember { Safe(inset("top", 0).dp, inset("bottom", 1).dp) }

private fun inset(side: String, index: Int): Double = js(
    """(function () {
        var forced = new URLSearchParams(window.location.search).get('safe');
        if (forced) return parseFloat(forced.split(',')[index]) || 0;
        var probe = document.createElement('div');
        probe.style.cssText = 'position:fixed;visibility:hidden;padding-top:env(safe-area-inset-' + side + ',0px)';
        document.body.appendChild(probe);
        var value = parseFloat(getComputedStyle(probe).paddingTop) || 0;
        probe.remove();
        return value;
    })()""",
)
