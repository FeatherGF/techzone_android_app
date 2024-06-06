package com.app.techzone.ui.theme.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.reusables.WideProductCard
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.server_response.UnauthorizedScreen
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse


@Composable
fun FavoriteScreen(
    favorites: List<BaseProduct>,
    favoriteState: ServerResponse,
    loadFavorites: () -> Unit,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    LaunchedEffect(favorites.size) { loadFavorites() }
    when (favoriteState) {
        ServerResponse.LOADING -> {
            LoadingBox()
        }

        ServerResponse.ERROR -> {
            ErrorScreen(loadFavorites)
        }

        ServerResponse.UNAUTHORIZED -> {
            UnauthorizedScreen()
        }

        ServerResponse.SUCCESS -> {
            if (favorites.isEmpty()) {
                EmptyFavoriteScreen()
            } else {
                FavoriteList(
                    favorites = favorites,
                    onProductCheckStatus = onProductCheckStatus,
                    onProductAction = onProductAction
                )
            }
        }
    }
}


@Composable
private fun EmptyFavoriteScreen() {
    val navController = LocalNavController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = MaterialTheme.dimension.extendedMedium,
                end = MaterialTheme.dimension.extendedMedium,
                top = MaterialTheme.dimension.huge * 2
            ),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimension.huge),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "У вас пока нет избранных товаров",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                modifier = Modifier.padding(
                    top = MaterialTheme.dimension.medium,
                    bottom = MaterialTheme.dimension.extendedMedium,
                    end = MaterialTheme.dimension.extraLarge,
                    start = MaterialTheme.dimension.extraLarge
                ),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { navController.navigate(ScreenRoutes.CATALOG) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Перейти в каталог",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Composable
private fun FavoriteList(
    favorites: List<BaseProduct>,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = MaterialTheme.dimension.extendedMedium,
            top = MaterialTheme.dimension.larger,
            end = MaterialTheme.dimension.extendedMedium
        ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small)
    ) {
        item {
            Row {
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
        }
        items(
            favorites,
            key = { it.id },
            contentType = { it::class }
        ) { product ->
            WideProductCard(
                product = product,
                onProductCheckStatus = onProductCheckStatus,
                onProductAction = onProductAction
            )
        }
    }
}