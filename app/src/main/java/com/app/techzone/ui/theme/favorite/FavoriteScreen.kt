package com.app.techzone.ui.theme.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.ui.theme.catalog.LazyProductCards
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.profile.UnauthorizedScreen
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse


@Composable
fun FavoriteScreen(
    favorites: List<BaseProduct>,
    favoriteState: ServerResponse,
    loadFavorites: () -> Unit,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    LaunchedEffect(favorites.size){ loadFavorites() }
    when (favoriteState){
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> { ErrorScreen(loadFavorites) }
        ServerResponse.UNAUTHORIZED -> { UnauthorizedScreen() }
        ServerResponse.SUCCESS -> {
            if (favorites.isEmpty()) {
                EmptyFavoriteScreen()
            } else {
                FavoriteList(favorites, onProductAction)
            }
        }
    }
}


@Composable
fun EmptyFavoriteScreen() {
    val navController = LocalNavController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, top = 120.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "У вас пока нет избранных товаров",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp, end = 43.dp, start = 43.dp),
                textAlign = TextAlign.Center
            )
            Button(onClick = { navController.navigate(ScreenRoutes.CATALOG) }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Перейти в каталог",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Composable
fun FavoriteList(
    favorites: List<BaseProduct>,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    Column(Modifier.background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.padding(start = 20.dp, top = 32.dp, bottom = 12.dp),
        ) {
            Text(
                text = "Избранное ",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            )
            Text(
                favorites.size.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim
            )
        }
        LazyProductCards(
            products = favorites,
            onProductAction = onProductAction,
        )
    }
}