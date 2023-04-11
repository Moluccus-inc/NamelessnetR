package namelessnet.app.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.*

object Components {
    // Service
    fun StartVpn(context: Context){
        // ToDO:
    }

    fun StopVpn(context: Context) {
        // ToDO:
    }

    // ClipboardManager
    fun setClipboard(context: Context, content: String) {
        try {
            val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, content)
            cmb.setPrimaryClip(clipData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getClipboard(context: Context): String {
        return try {
            val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cmb.primaryClip?.getItemAt(0)?.text.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun openUri(context: Context, uriString: String) {
        val uri = Uri.parse(uriString)
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    fun getUuid(): String {
        return try {
            UUID.randomUUID().toString().replace("-", "")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}