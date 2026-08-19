package com.mavuno.church.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.mavuno.church.bridge.MavunoWebBridge
import com.mavuno.church.ui.theme.MavunoTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebContainerView(
    modifier: Modifier = Modifier,
    onPageTitleChanged: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val isDark = MavunoTheme.colors.isDark

    val embeddedHtml = remember(isDark) {
        val bgColor = if (isDark) "#000000" else "#FFFFFF"
        val cardBg = if (isDark) "#111113" else "#F8FAFC"
        val cardBorder = if (isDark) "#27272A" else "#E2E8F0"
        val textColor = if (isDark) "#F8FAFC" else "#0F172A"
        val textSecondary = if (isDark) "#94A3B8" else "#475569"
        val statusBg = if (isDark) "#09090B" else "#F1F5F9"
        val statusColor = if (isDark) "#38BDF8" else "#0284C7"
        val inputBg = if (isDark) "#18181B" else "#FFFFFF"
        val inputBorder = if (isDark) "#3F3F46" else "#CBD5E1"
        val btnAltBg = if (isDark) "#27272A" else "#E2E8F0"
        val btnAltColor = if (isDark) "#F8FAFC" else "#0F172A"

        """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <title>Mavuno Church Web & Kids Guard Bridge</title>
            <style>
                * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
                body { background: $bgColor; color: $textColor; padding: 16px; padding-bottom: 90px; transition: background-color 0.2s ease, color 0.2s ease; }
                .card { background: $cardBg; border-radius: 18px; padding: 16px; margin-bottom: 16px; border: 1px solid $cardBorder; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
                .hero-badge { background: #F97316; color: white; padding: 4px 10px; border-radius: 20px; font-size: 11px; font-weight: bold; display: inline-block; }
                h1 { font-size: 18px; margin-top: 8px; margin-bottom: 4px; color: $textColor; }
                p { font-size: 12px; color: $textSecondary; line-height: 1.5; }
                .btn { background: #F97316; color: white; border: none; border-radius: 12px; padding: 10px 14px; font-size: 12px; font-weight: bold; cursor: pointer; display: inline-flex; align-items: center; gap: 6px; margin: 4px 2px; }
                .btn-alt { background: $btnAltBg; color: $btnAltColor; }
                .btn-red { background: #EF4444; color: white; }
                .btn-green { background: #10B981; color: white; }
                .status-box { background: $statusBg; border-radius: 12px; padding: 12px; font-family: monospace; font-size: 11px; color: $statusColor; border: 1px solid $cardBorder; margin-top: 10px; word-break: break-all; }
                .input-box { width: 100%; background: $inputBg; border: 1px solid $inputBorder; border-radius: 10px; padding: 10px; color: $textColor; font-size: 12px; margin-top: 8px; margin-bottom: 8px; }
                .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; margin-top: 8px; }
            </style>
        </head>
        <body>
            <div class="card">
                <span class="hero-badge">HYBRID BRIDGE ACTIVE</span>
                <h1>Mavuno Church Web Container</h1>
                <p>Connected via two-way native Kotlin JavaScript Interface (<code>@JavascriptInterface</code>).</p>
                <div class="grid">
                    <button class="btn btn-green" onclick="toggleGuard(true)">🛡️ Enable Guard</button>
                    <button class="btn btn-alt" onclick="toggleGuard(false)">⏸️ Pause Guard</button>
                </div>
            </div>

            <div class="card">
                <h3 style="font-size:14px; margin-bottom:6px;">Age-Adaptive Profiles</h3>
                <p>Switch security sensitivity thresholds in real-time:</p>
                <div style="margin-top:8px;">
                    <button class="btn btn-alt" onclick="setAge('CHILD')">👶 Child (3-9)</button>
                    <button class="btn btn-alt" onclick="setAge('PRE_TEEN')">🧑 Pre-Teen (10-12)</button>
                    <button class="btn btn-alt" onclick="setAge('TEEN')">🧑‍🎓 Teen (13-17)</button>
                </div>
            </div>

            <div class="card">
                <h3 style="font-size:14px; margin-bottom:6px;">Test Multimodal Guard Scanner</h3>
                <p>Simulate on-screen text to evaluate glassmorphic shield activation:</p>
                <input id="scanInput" class="input-box" placeholder="e.g. Free Robux Hack or Sunday Sermon Story..." value="Free Robux Generator 10,000" />
                <button class="btn" onclick="testScan()">🔍 Trigger Safety Scan</button>
                <button class="btn btn-alt" onclick="askEllaFromWeb()">✨ Ask Ella</button>
                <div id="scanOutput" class="status-box">Status: Ready for Bridge calls.</div>
            </div>

            <div class="card">
                <h3 style="font-size:14px; margin-bottom:6px;">Campus Live Query</h3>
                <button class="btn btn-alt" onclick="getCampusData()">⛪ Fetch Nairobi Campus Info</button>
                <div id="campusOutput" class="status-box">Campus info will load here.</div>
            </div>

            <script>
                function updateLog(msg) {
                    document.getElementById('scanOutput').innerText = msg;
                }

                function toggleGuard(enabled) {
                    if (window.MavunoBridge) {
                        window.MavunoBridge.toggleGuardService(enabled);
                        updateLog("Toggled Guard: " + enabled);
                    } else {
                        updateLog("Error: MavunoBridge not detected");
                    }
                }

                function setAge(tier) {
                    if (window.MavunoBridge) {
                        window.MavunoBridge.setAgeProfile(tier);
                        updateLog("Set Age Tier: " + tier);
                    }
                }

                function testScan() {
                    const text = document.getElementById('scanInput').value;
                    if (window.MavunoBridge) {
                        const res = window.MavunoBridge.simulateSafetyScan(text);
                        updateLog("Scan Result: " + res);
                    }
                }

                function askEllaFromWeb() {
                    const text = document.getElementById('scanInput').value;
                    if (window.MavunoBridge) {
                        window.MavunoBridge.triggerEllaAssistant(text);
                    }
                }

                function getCampusData() {
                    if (window.MavunoBridge) {
                        const info = window.MavunoBridge.getCampusInfo();
                        document.getElementById('campusOutput').innerText = info;
                    }
                }

                window.onMavunoNativeEvent = function(event, data) {
                    updateLog("Native Event Received: " + event + " -> " + JSON.stringify(data));
                };
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.cacheMode = WebSettings.LOAD_DEFAULT

                    val bridge = MavunoWebBridge(ctx, this)
                    addJavascriptInterface(bridge, "MavunoBridge")

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            onPageTitleChanged?.invoke(view?.title ?: "Mavuno Hybrid Web")
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            super.onReceivedTitle(view, title)
                            title?.let { onPageTitleChanged?.invoke(it) }
                        }
                    }

                    loadDataWithBaseURL("https://mavunochurch.org", embeddedHtml, "text/html", "UTF-8", null)
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL("https://mavunochurch.org", embeddedHtml, "text/html", "UTF-8", null)
            },
            modifier = Modifier
                .fillMaxSize()
                .testTag("embedded_web_container")
        )
    }
}
