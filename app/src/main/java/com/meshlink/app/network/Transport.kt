package com.meshlink.app.network

import com.meshlink.app.model.Message
import com.meshlink.app.model.User
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow

/**
 * Transport interface for different networking technologies (BLE, Wi-Fi Direct, Internet).
 * Each transport implements this interface to provide a unified API for the mesh network layer.
 */
interface Transport {

    /** Unique identifier for this transport type */
    val transportId: TransportId

    /** Human-readable name */
    val displayName: String

    /** Whether this transport is currently available on the device */
    val isAvailable: Boolean

    /** Current connection state */
    val connectionState: Flow<TransportConnectionState>

    /** Start the transport (advertise, scan, listen) */
    suspend fun start(): TransportResult<Unit>

    /** Stop the transport */
    suspend fun stop(): TransportResult<Unit>

    /** Send a message to a specific peer */
    suspend fun send(peerId: String, message: Message): TransportResult<Unit>

    /** Broadcast a message to all connected peers */
    suspend fun broadcast(message: Message): TransportResult<Unit>

    /** Incoming message stream */
    val incomingMessages: ReceiveChannel<TransportMessage>

    /** Connected peers stream */
    val connectedPeers: Flow<Set<TransportPeer>>

    /** Discovered peers stream (for discovery phase) */
    val discoveredPeers: Flow<Set<TransportPeer>>

    /** Get transport-specific capabilities */
    val capabilities: TransportCapabilities
}

enum class TransportId {
    BLUETOOTH_LE,
    WIFI_DIRECT,
    INTERNET
}

data class TransportPeer(
    val id: String,           // Transport-specific peer identifier
    val displayName: String,  // Human-readable name
    val signalStrength: Int? = null, // RSSI or similar
    val metadata: Map<String, String> = emptyMap() // Transport-specific data
)

data class TransportMessage(
    val fromPeerId: String,
    val message: Message,
    val transportId: TransportId,
    val receivedAt: Long = System.currentTimeMillis()
)

enum class TransportConnectionState {
    DISCONNECTED,
    STARTING,
    SCANNING,
    ADVERTISING,
    CONNECTING,
    CONNECTED,
    ERROR
}

data class TransportCapabilities(
    val maxMessageSize: Int = 64 * 1024, // 64KB default
    val supportsBroadcast: Boolean = true,
    val supportsEncryption: Boolean = false, // Transport-level encryption
    val estimatedLatencyMs: Int = 100,
    val estimatedThroughputKbps: Int = 1000,
    val isReliable: Boolean = false, // Whether transport guarantees delivery
    val maxPeers: Int = 10
)

sealed class TransportResult<out T> {
    data class Success<out T>(val value: T) : TransportResult<T>()
    data class Failure(val error: TransportError) : TransportResult<Nothing>()
}

sealed class TransportError(val message: String, val cause: Throwable? = null) {
    data class NotAvailable(override val message: String) : TransportError(message)
    data class PermissionDenied(override val message: String) : TransportError(message)
    data class PeerNotFound(override val message: String) : TransportError(message)
    data class ConnectionFailed(override val message: String, override val cause: Throwable? = null) : TransportError(message, cause)
    data class SendFailed(override val message: String, override val cause: Throwable? = null) : TransportError(message, cause)
    data class Timeout(override val message: String) : TransportError(message)
    data class Unknown(override val message: String, override val cause: Throwable? = null) : TransportError(message, cause)
}