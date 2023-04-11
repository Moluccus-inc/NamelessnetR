package namelessnet.app.protocol

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val appVersion: String,
    val packageName: String,
    val isSystemApp: Boolean,
    val appIcon: Drawable
)
