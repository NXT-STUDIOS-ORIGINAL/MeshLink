package com.meshlink.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meshlink.app.R
import com.meshlink.app.model.UserProfile
import com.meshlink.app.ui.theme.MeshLinkTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onProfileClick: () -> Unit = {}
) {
    val profile by viewModel.profile.observeAsState()
    val isEditing by viewModel.isEditing.observeAsState()

    // Network settings
    val bluetoothEnabled by viewModel.bluetoothEnabled.observeAsState()
    val wifiDirectEnabled by viewModel.wifiDirectEnabled.observeAsState()
    val meshRelayEnabled by viewModel.meshRelayEnabled.observeAsState()
    val maxHopCount by viewModel.maxHopCount.observeAsState()

    // UI settings
    val theme by viewModel.theme.observeAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.observeAsState()
    val soundEnabled by viewModel.soundEnabled.observeAsState()
    val vibrationEnabled by viewModel.vibrationEnabled.observeAsState()

    // Security settings
    val biometricEnabled by viewModel.biometricEnabled.observeAsState()
    val autoLockTimeout by viewModel.autoLockTimeout.observeAsState()

    // Debug
    val debugMode by viewModel.debugMode.observeAsState()

    MeshLinkTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            )

            androidx.compose.foundation.layout.Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Profile Section
                ProfileSection(profile = profile, onClick = { viewModel.setEditing(true); onProfileClick() })

                Divider()

                // Network Section
                SettingsSection(title = stringResource(R.string.network)) {
                    SettingRow(
                        title = stringResource(R.string.bluetooth),
                        subtitle = stringResource(R.string.enable_bluetooth_discovery),
                        icon = androidx.compose.material.icons.Icons.Default.Bluetooth,
                        trailing = {
                            Switch(
                                checked = bluetoothEnabled,
                                onCheckedChange = { viewModel.setBluetoothEnabled(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                    SettingRow(
                        title = stringResource(R.string.wifi_direct),
                        subtitle = stringResource(R.string.enable_wifi_direct),
                        icon = androidx.compose.material.icons.Icons.Default.Wifi,
                        trailing = {
                            Switch(
                                checked = wifiDirectEnabled,
                                onCheckedChange = { viewModel.setWifiDirectEnabled(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                    SettingRow(
                        title = stringResource(R.string.mesh_relay),
                        subtitle = stringResource(R.string.enable_mesh_relay),
                        icon = androidx.compose.material.icons.Icons.Default.Router,
                        trailing = {
                            Switch(
                                checked = meshRelayEnabled,
                                onCheckedChange = { viewModel.setMeshRelayEnabled(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                    SettingRow(
                        title = stringResource(R.string.max_hop_count),
                        subtitle = stringResource(R.string.max_hop_count_desc),
                        icon = androidx.compose.material.icons.Icons.Default.ArrowForward,
                        trailing = {
                            Text(text = maxHopCount.toString(), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    )
                }

                Divider()

                // Appearance Section
                SettingsSection(title = stringResource(R.string.appearance)) {
                    SettingRow(
                        title = stringResource(R.string.theme),
                        subtitle = stringResource(R.string.theme_desc),
                        icon = androidx.compose.material.icons.Icons.Default.Palette,
                        trailing = {
                            Text(text = theme.capitalize(), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    )
                }

                Divider()

                // Notifications Section
                SettingsSection(title = stringResource(R.string.notifications)) {
                    SettingRow(
                        title = stringResource(R.string.notifications),
                        subtitle = stringResource(R.string.notifications_desc),
                        icon = androidx.compose.material.icons.Icons.Default.Notifications,
                        trailing = {
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { viewModel.setNotificationsEnabled(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                    SettingRow(
                        title = stringResource(R.string.sound),
                        subtitle = stringResource(R.string.sound_desc),
                        icon = androidx.compose.material.icons.Icons.Default.VolumeUp,
                        trailing = {
                            Switch(
                                checked = soundEnabled,
                                onCheckedChange = { viewModel.setSoundEnabled(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                    SettingRow(
                        title = stringResource(R.string.vibration),
                        subtitle = stringResource(R.string.vibration_desc),
                        icon = androidx.compose.material.icons.Icons.Default.Vibration,
                        trailing = {
                            Switch(
                                checked = vibrationEnabled,
                                onCheckedChange = { viewModel.setVibrationEnabled(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                }

                Divider()

                // Security Section
                SettingsSection(title = stringResource(R.string.security)) {
                    SettingRow(
                        title = stringResource(R.string.biometric_lock),
                        subtitle = stringResource(R.string.biometric_lock_desc),
                        icon = androidx.compose.material.icons.Icons.Default.Fingerprint,
                        trailing = {
                            Switch(
                                checked = biometricEnabled,
                                onCheckedChange = { viewModel.setBiometricEnabled(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                    SettingRow(
                        title = stringResource(R.string.auto_lock),
                        subtitle = stringResource(R.string.auto_lock_desc, autoLockTimeout),
                        icon = androidx.compose.material.icons.Icons.Default.LockClock,
                        trailing = {
                            Text(text = "$autoLockTimeout min", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    )
                }

                Divider()

                // Debug Section (only in debug mode)
                if (debugMode) {
                    SettingsSection(title = "Debug") {
                        SettingRow(
                            title = "Debug Mode",
                            subtitle = "Enable debug logging",
                            icon = androidx.compose.material.icons.Icons.Default.BugReport,
                            trailing = {
                                Switch(
                                    checked = debugMode,
                                    onCheckedChange = { viewModel.setDebugMode(it) },
                                    colors = androidx.compose.material3.SwitchDefaults.colors(
                                        thumbColor = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            }
                        )
                    }
                    Divider()
                }

                // About Section
                SettingsSection(title = stringResource(R.string.about)) {
                    SettingRow(
                        title = stringResource(R.string.version),
                        subtitle = "1.0.0",
                        icon = androidx.compose.material.icons.Icons.Default.Info,
                        trailing = {}
                    )
                    SettingRow(
                        title = stringResource(R.string.privacy_policy),
                        subtitle = stringResource(R.string.privacy_policy_desc),
                        icon = androidx.compose.material.icons.Icons.Default.PrivacyTip,
                        trailing = {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.ArrowForwardIos,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSection(
    profile: UserProfile?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                profile?.let { p ->
                    Text(
                        text = p.displayName.firstOrNull()?.uppercase() ?: "?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(16.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = profile?.displayName ?: "Tap to set up profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.tap_to_edit_profile),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))

            androidx.compose.material3.Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_profile),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
        content()
    }
}

@Composable
fun SettingRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    trailing: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 16.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        trailing()
    }
}