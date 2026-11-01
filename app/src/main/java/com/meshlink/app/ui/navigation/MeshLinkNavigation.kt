package com.meshlink.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meshlink.app.ui.chat.ChatScreen
import com.meshlink.app.ui.groups.GroupsScreen
import com.meshlink.app.ui.main.MainScreen
import com.meshlink.app.ui.nearby.NearbyScreen
import com.meshlink.app.ui.rooms.RoomsScreen
import com.meshlink.app.ui.settings.SettingsScreen

@Composable
fun MeshLinkNavHost() {
    val navController = rememberNavController()
    androidx.navigation.compose.NavHost(navController, "main") {
        composable("main") {
            MainScreen(
                onNavigateToChat = { chatId ->
                    navController.navigate("chat/$chatId")
                },
                onNavigateToGroup = { groupId ->
                    navController.navigate("group/$groupId")
                },
                onNavigateToRoom = { roomId ->
                    navController.navigate("room/$roomId")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable(
            route = "chat/{chatId}",
            arguments = listOf(androidx.navigation.navArgument("chatId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.getString()!!
            ChatScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = "group/{groupId}",
            arguments = listOf(androidx.navigation.navArgument("groupId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.getString()!!
            GroupsScreen(onGroupClick = { chat ->
                navController.navigate("chat/${chat.id}")
            })
        }

        composable(
            route = "room/{roomId}",
            arguments = listOf(androidx.navigation.navArgument("roomId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.getString()!!
            RoomsScreen(onRoomClick = { chat ->
                navController.navigate("chat/${chat.id}")
            })
        }

        composable("settings") {
            SettingsScreen(onProfileClick = { navController.navigate("profile") })
        }

        composable("profile") {
            // TODO: Profile setup/edit screen
            SettingsScreen(onProfileClick = {})
        }
    }
}