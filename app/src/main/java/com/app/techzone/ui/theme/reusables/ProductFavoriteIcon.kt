package com.app.techzone.ui.theme.reusables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app.techzone.LocalNavController
import com.app.techzone.LocalSnackbarHostState
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import kotlinx.coroutines.launch

@Composable
fun ProductFavoriteIcon(
    modifier: Modifier = Modifier,
    productId: Int,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val scope = rememberCoroutineScope()
    var isFavorite by remember {
        mutableStateOf(onProductCheckStatus(CheckProductStatus.CheckProductIsFavorite(productId)))
    }
    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current
    IconButton(
        onClick = {
            scope.launch {
                isFavorite = if (isFavorite) {
                    !onProductAction(
                        ProductAction.RemoveFromFavorites(productId, snackbarHostState) {
                            navController.navigate(ScreenRoutes.FAVORITE)
                        }
                    )
                } else {
                    onProductAction(
                        ProductAction.AddToFavorites(productId, snackbarHostState) {
                            navController.navigate(ScreenRoutes.FAVORITE)
                        }
                    )
                }
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else {
                Icons.Outlined.FavoriteBorder
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier
        )
    }
}