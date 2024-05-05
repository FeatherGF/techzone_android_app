package com.app.techzone.ui.theme.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.techzone.data.remote.model.Order
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UnauthorizedScreen
import com.app.techzone.ui.theme.profile.auth.UserViewModel
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse

@Composable
fun OrderScreenRoot(
    userViewModel: UserViewModel,
    navController: NavController,
) {
    LaunchedEffect(userViewModel.orders) {
        userViewModel.loadOrders()
    }

//    val orders = emptyList<Order>()
    val orders by userViewModel.orders.collectAsStateWithLifecycle()
    // TODO: мои заказы top bar
    when (userViewModel.state.response){
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> { ErrorScreen(userViewModel::loadOrders) }
        ServerResponse.UNAUTHORIZED -> {
            UnauthorizedScreen {
                navController.navigate(ScreenRoutes.PROFILE_REGISTRATION)
            }
        }
        ServerResponse.SUCCESS -> {
            if (orders.isEmpty()) {
                EmptyOrderHistory {
                    navController.navigate(ScreenRoutes.CATALOG)
                }
            } else {
                OrderHistory(
                    orders,
//                    addToFavorite = addToFavorite,
//                    removeFromFavorite = removeFromFavorite,
                ) {
                    navController.navigate("catalog/$it")
                }
            }
        }
    }
}

@Composable
fun EmptyOrderHistory(navigateToCatalog: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, top = 120.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "У вас пока нет заказов",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )
            Button(onClick = navigateToCatalog) {
                Text(
                    "Перейти в каталог",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Composable
fun OrderHistory(orders: List<Order>, navigateToDetail: (Int) -> Unit) {
    Text(text = orders.joinToString(""))
}