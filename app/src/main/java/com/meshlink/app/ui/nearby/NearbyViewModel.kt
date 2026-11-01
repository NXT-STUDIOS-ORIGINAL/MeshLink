package com.meshlink.app.ui.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meshlink.app.network.MeshNetworkManager
import com.meshlink.app.network.MeshPeer
import com.meshlink.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class NearbyViewModel(
    private val networkManager: MeshNetworkManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _discoveredPeers = MutableStateFlow<Set<MeshPeer>>(emptySet())
    val discoveredPeers = _discoveredPeers.asStateFlow()

    private val _knownPeers = MutableStateFlow<Set<MeshPeer>>(emptySet())
    val knownPeers = _knownPeers.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val _selectedPeer = MutableStateFlow<MeshPeer?>(null)
    val selectedPeer = _selectedPeer.asStateFlow()

    init {
        observePeers()
    }

    private fun observePeers() {
        viewModelScope.launch {
            networkManager.reachablePeers.collect { peers ->
                _knownPeers.value = peers
            }
        }
    }

    fun onPeerClick(peer: MeshPeer) {
        _selectedPeer.value = peer
    }

    fun clearSelection() {
        _selectedPeer.value = null
    }

    fun startScan() {
        viewModelScope.launch {
            _isScanning.value = true
            networkManager.scanForPeers()
            _isScanning.value = false
        }
    }

    fun startChat(peer: MeshPeer) {
        // Navigate to chat with this peer
        // This will be handled by navigation
    }
}