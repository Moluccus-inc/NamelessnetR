package namelessnet.app.services

import android.content.Intent
import android.net.LocalServerSocket
import android.net.LocalSocket
import android.net.LocalSocketAddress
import android.net.VpnService
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LeafVpnService : VpnService() {

    private lateinit var bgThread: ExecutorService
    private lateinit var protectThread: ExecutorService
    private lateinit var tunFd: ParcelFileDescriptor

    companion object {
        const val RT_ID = 0 // You can change this to any non-negative value
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val protectPath = createProtectPath()
        protectThread = Executors.newSingleThreadExecutor()
        protectThread.submit {
            val localSocket = LocalSocket()
            localSocket.bind(
                LocalSocketAddress(
                    protectPath,
                    LocalSocketAddress.Namespace.FILESYSTEM
                )
            )
            val socket = LocalServerSocket(localSocket.fileDescriptor)
            val buffer = ByteBuffer.allocate(4)
            while (true) {
                val stream = socket.accept()
                buffer.clear()
                val n = stream.inputStream.read(buffer.array())
                if (n == 4) {
                    val fd = buffer.int
                    if (!this.protect(fd)) {
                        println("protect failed")
                    }
                    buffer.clear()
                    buffer.putInt(0)
                } else {
                    buffer.clear()
                    buffer.putInt(1)
                }
                stream.outputStream.write(buffer.array())
            }
        }

        bgThread = Executors.newSingleThreadExecutor()
        bgThread.submit {
            try {
                val builder = Builder().apply {
                    setSession("leaf")
                    setMtu(1500)
                    addAddress("10.255.0.1", 30)
                    addDnsServer("1.1.1.1")
                    addRoute("0.0.0.0", 0)
                }
                tunFd = builder.establish()!!
                val configFile = createConfigFile(tunFd.fd.toLong().toString())
                println(configFile.readText())
                LeafJNI.leaf_run(RT_ID.toShort(), configFile.absolutePath)
            } catch (e: Exception) {
                e.printStackTrace()
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            tunFd.close()
            LeafJNI.leaf_shutdown(RT_ID.toShort())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createProtectPath(): String {
        return File.createTempFile("leaf_vpn_socket_protect", ".sock", cacheDir).absolutePath
    }

    private fun createConfigFile(tunFdString: String): File {
        val configFile = File(filesDir, "config.conf")
        val configContent = """
        [General]
        loglevel = trace
        dns-server = 223.5.5.5
        tun-fd = REPLACE-ME-WITH-THE-FD
        [Proxy]
        Direct = direct
        """.trimIndent()
        val configContentWithTunFd =
            configContent.replace("REPLACE-ME-WITH-THE-FD", tunFdString)
        FileOutputStream(configFile).use {
            it.write(configContentWithTunFd.toByteArray())
        }
        return configFile
    }
}