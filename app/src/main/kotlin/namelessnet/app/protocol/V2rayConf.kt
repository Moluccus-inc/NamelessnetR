package namelessnet.app.protocol

import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class V2rayConf(
    @Expose
    val dns: Dns,
    @Expose
    val inbounds: List<Inbound>,
    @Expose
    val log: Log,
    @Expose
    val outbounds: List<Outbound>,
    @Expose
    val policy: Policy,
    @Expose
    val routing: Routing,
    @Expose
    val stats: Stats
) {
    @Keep
    data class Dns(
        @Expose
        val hosts: Hosts,
        @Expose
        val servers: List<Any>
    ) {
        @Keep
        class Hosts
    }

    @Keep
    data class Inbound(
        @Expose
        val port: Int,
        @Expose
        val protocol: String,
        @Expose
        val settings: Settings,
        @Expose
        val sniffing: Sniffing,
        @Expose
        val tag: String
    ) {
        @Keep
        data class Settings(
            @Expose
            val auth: String,
            @Expose
            val udp: Boolean,
            @Expose
            val userLevel: Int
        )

        @Keep
        data class Sniffing(
            @Expose
            val destOverride: List<String>,
            @Expose
            val enabled: Boolean
        )
    }

    @Keep
    data class Log(
        @Expose
        val loglevel: String
    )

    @Keep
    data class Outbound(
        @Expose
        val mux: Mux,
        @Expose
        val protocol: String,
        @Expose
        val settings: Settings,
        @Expose
        val streamSettings: StreamSettings,
        @Expose
        val tag: String
    ) {
        @Keep
        data class Mux(
            @Expose
            val enabled: Boolean
        )

        @Keep
        data class Settings(
            @Expose
            val response: Response,
            @Expose
            val servers: List<Server>,
            @Expose
            val vnext: List<Vnext>
        ) {
            @Keep
            data class Response(
                @Expose
                val type: String
            )

            @Keep
            data class Server(
                @Expose
                val address: String,
                @Expose
                val level: Int,
                @Expose
                val method: String,
                @Expose
                val ota: Boolean,
                @Expose
                val password: String,
                @Expose
                val port: Int
            )

            @Keep
            data class Vnext(
                @Expose
                val address: String,
                @Expose
                val port: Int,
                @Expose
                val users: List<User>
            ) {
                @Keep
                data class User(
                    @Expose
                    val alterId: Int,
                    @Expose
                    val id: String,
                    @Expose
                    val level: Int,
                    @Expose
                    val security: String
                )
            }
        }

        @Keep
        data class StreamSettings(
            @Expose
            val network: String
        )
    }

    @Keep
    data class Policy(
        @Expose
        val levels: Map<Int, MapValue>,
        @Expose
        val system: System
    ) {
        @Keep
        data class MapValue(
            @Expose
            val connIdle: Int,
            @Expose
            val downlinkOnly: Int,
            @Expose
            val handshake: Int,
            @Expose
            val uplinkOnly: Int
        )

        @Keep
        data class System(
            @Expose
            val statsOutboundDownlink: Boolean,
            @Expose
            val statsOutboundUplink: Boolean
        )
    }

    @Keep
    data class Routing(
        @Expose
        val domainStrategy: String,
        @Expose
        val rules: List<Any>
    )

    @Keep
    class Stats
}