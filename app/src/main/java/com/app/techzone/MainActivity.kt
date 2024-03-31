package com.app.techzone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.techzone.screens.BottomItem
import com.app.techzone.screens.CartScreen
import com.app.techzone.screens.FavoriteScreen
import com.app.techzone.screens.MainScreen
import com.app.techzone.screens.ProfileScreen
import com.app.techzone.screens.SearchScreen
import com.app.techzone.ui.theme.TechZoneTheme


//@Preview
//@Composable
//fun TechZoneLogo() {
//    Box(modifier = Modifier.padding(top = 60.dp, bottom = 20.dp)) {
//        Image(painter = painterResource(R.drawable.ic_logo), contentDescription = null)
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Header() {
    var active by remember { mutableStateOf(false) }
    val dynamicHeight = if (active) 360.dp else 190.dp  // TODO: remake to overflow

    var text by remember { mutableStateOf("") }
    val items = remember { mutableListOf("") }
    val onSearch = fun() {
        if (text.isNotEmpty()) {
            items.add(0, text)
            text = ""
            active = false
        }
    }
    items.remove("")
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(bottomEnd = 28.dp, bottomStart = 28.dp)
            )
            .fillMaxWidth()
            .height(dynamicHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(top = 60.dp, bottom = 20.dp)) {
            Image(painter = painterResource(R.drawable.ic_logo), contentDescription = null)
        }
        SearchBar(
            query = text,
            onQueryChange = { text = it },
            onSearch = { onSearch() },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(text = "Поиск в TechZone") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (active) {
                    Text(
                        text = "Отменить",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                    )
                }
            },
//            modifier = Modifier.
        ) {
            items.forEach {
                Row(modifier = Modifier
                    .padding(all = 14.dp)
                    .clickable {
                        text = it
                        onSearch()
                    }) {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.History,
                        contentDescription = "Иконка истории поискового запроса"
                    )
                    Text(text = it)
                }
            }
        }
    }
}


class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechZoneTheme {
                val navController = rememberNavController()
                val screens = listOf(
                    BottomItem.MainScreen,
                    BottomItem.SearchScreen,
                    BottomItem.CartScreen,
                    BottomItem.FavoriteScreen,
                    BottomItem.ProfileScreen,
                )
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                Scaffold(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    bottomBar = {
                        BottomAppBar(containerColor = MaterialTheme.colorScheme.tertiary) {
                            NavigationBar(containerColor = MaterialTheme.colorScheme.tertiary) {
                                screens.forEach { screen ->
                                    val isSelected = currentRoute == screen.route
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { navController.navigate(screen.route) },
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    screen.badgeCount?.let {
                                                        Badge { Text(text = it.toString()) }
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (isSelected) screen.iconFilled else screen.iconOutlined,
                                                    contentDescription = "Иконка навигации для раздела ${screen.title}",
                                                )
                                            }
                                        },
                                        label = { Text(text = screen.title) },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) {
                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") { MainScreen() }
                        composable("search_screen") { SearchScreen() }
                        composable("cart_screen") { CartScreen() }
                        composable("favorite_screen") { FavoriteScreen() }
                        composable("profile_screen") { ProfileScreen() }
                    }
                }
            }
        }
    }
}
