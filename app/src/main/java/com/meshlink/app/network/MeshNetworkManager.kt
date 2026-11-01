package com.meshlink.app.network

import com.meshlink.app.model.Message
import com.meshlink.app.model.User
import com.meshlink.app.repository.ChatRepository
import com.meshlink.app.repository.MessageRepository
import com.meshlink.app.repository.ProfileRepository
import com.meshlink.app.repository.UserRepository
import kotlinx.coroutines.flow.Flow

interface MeshNetworkManager {

    /** Current mesh network state */
    val networkState: Flow<MeshNetworkState>

    /** Local user profile */
    val localUser: User?

    /** Start all transports and mesh networking */
    suspend fun start(): MeshResult<Unit>

    /** Stop all transports and mesh networking */
    suspend fun stop(): MeshResult<Unit>

    /** Send a message to a specific user (handles routing) */
    suspend fun sendMessage(userId: String, message: Message): MeshResult<Unit>

    /** Broadcast a message to a chat (handles group messaging) */
    suspend fun broadcastToChat(chatId: String, message: Message): MeshResult<Unit>

    /** Get list of currently reachable peers */
    val reachablePeers: Flow<Set<MeshPeer>>

    /** Get list of all known peers */
    val knownPeers: Flow<Set<MeshPeer>>

    /** Register a message listener for incoming messages */
    fun registerMessageListener(listener: MessageListener)

    /** Unregister a message listener */
    fun unregisterMessageListener(listener: MessageListener)

    /** Force discovery scan */
    suspend fun scanForPeers(): MeshResult<Set<MeshPeer>>

    /** Get network statistics */
    val networkStats: Flow<NetworkStats>
}

data class MeshPeer(
    val userId: String,
    val displayName: String,
    val transports: Set<TransportId>,
    val lastSeen: Long,
    val signalQuality: Int, // 0-100
    val isDirectlyConnected: Boolean,
    val hopCount: Int = 1,
    val metadata: Map<String, String> = emptyMap()
)

enum class MeshNetworkState {
    STOPPED,
    STARTING,
    RUNNING,
    DEGRADED, // Some transports failed
    ERROR
}

data class NetworkStats(
    val messagesSent: Long = 0,
    val messagesReceived: Long = 0,
    val messagesRelayed: Long = 0,
    val bytesSent: Long = 0,
    val bytesReceived: Long = 0,
    val activePeers: Int = 0,
    val connectedTransports: Set<TransportId> = emptySet(),
    val uptimeMs: Long = 0
)

interface MessageListener {
    fun onMessageReceived(message: Message, fromPeer: MeshPeer, transport: TransportId)
    fun onMessageDelivered(messageId: String, toPeer: String)
    fun onMessageFailed(messageId: String, toPeer: String, error: String)
    fun onPeerDiscovered(peer: MeshPeer)
    fun onPeerConnected(peer: MeshPeer)
    fun onPeerDisconnected(peerId: String)
}

sealed class MeshResult<out T> {
    data class Success<out T>(val value: T) : MeshResult<T>()
    data class Failure(val error: MeshError) : MeshResult<Nothing>()
}

sealed class MeshError(val message: String, val cause: Throwable? = null) {
    data class NotStarted(override val message: String) : MeshError(message)
    data class NoRouteToPeer(override val message: String) : MeshError(message)
    data class PeerOffline(override val message: String) : MeshError(message)
    data class EncryptionFailed(override val message: String) : MeshError(message)
    data class SerializationFailed(override val message: String) : MeshError(message)
    data class TransportError(override val message: String, override val cause: Throwable? = null) : MeshError(message, cause)
    data class Unknown(override val message: String, override val cause: Throwable? = null) : MeshError(message, cause)
}

class MeshNetworkManagerImpl(
    private val context: android.content.Context,
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : MeshNetworkManager {

    override val networkState: Flow<MeshNetworkState> = kotlinx.coroutines.flow.flowOf(MeshNetworkState.STOPPED)
    override val localUser: User? = null
    override val reachablePeers: Flow<Set<MeshPeer>> = kotlinx.coroutines.flow.flowOf(emptySet())
    override val knownPeers: Flow<Set<MeshPeer>> = kotlinx.coroutines.flow.flowOf(emptySet())
    override val networkStats: Flow<NetworkStats> = kotlinx.coroutines.flow.flowOf(NetworkStats())

    override suspend fun start(): MeshResult<Unit> = MeshResult.Failure(MeshError.NotStarted("Not implemented in Stage 1"))

    override suspend fun stop(): MeshResult<Unit> = MeshResult.Success(Unit)

    override suspend fun sendMessage(userId: String, message: Message): MeshResult<Unit> =
        MeshResult.Failure(MeshError.NotStarted("Not implemented in Stage 1"))

    override suspend fun broadcastToChat(chatId: String, message: Message): MeshResult<Unit> =
        MeshResult.Failure(MeshError.NotStarted("Not implemented in Stage 1"))

    override fun registerMessageListener(listener: MessageListener) {}

    override fun unregisterMessageListener(listener: MessageListener) {}

    override suspend fun scanForPeers(): MeshResult<Set<MeshPeer>> =
        MeshResult.Failure(MeshError.NotStarted("Not implemented in Stage 1"))
}