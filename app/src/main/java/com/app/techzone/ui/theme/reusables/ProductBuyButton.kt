package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.app.techzone.LocalSnackbarHostState
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import kotlinx.coroutines.launch

@Composable
fun ProductBuyButton(
    modifier: Modifier = Modifier,
    productId: Int,
    isActive: Boolean,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean
) {
    val scope = rememberCoroutineScope()
    var isInCartState by remember {
        mutableStateOf(onProductCheckStatus(CheckProductStatus.CheckProductInCart(productId)))
    }
    val text: String
    val colors: ButtonColors
    val action: ProductAction
    if (!isActive) {
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0, 111, 238, 12),
            contentColor = MaterialTheme.colorScheme.primary
        )
        text = "Нет в наличии"
        // button will be disabled
        action = ProductAction.AddToCart(productId, LocalSnackbarHostState.current)
    } else {
        if (isInCartState) {
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 111, 238, 12),
                contentColor = MaterialTheme.colorScheme.primary
            )
            text = "В корзине"
            action = ProductAction.RemoveFromCart(productId, LocalSnackbarHostState.current)
        } else {
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.tertiary
            )
            text = "В корзину"
            action = ProductAction.AddToCart(productId, LocalSnackbarHostState.current)
        }
    }

    Button(
        onClick = {
            scope.launch {
                isInCartState = onProductAction(action)
            }
        },
        contentPadding = PaddingValues(
            vertical = MaterialTheme.dimension.medium,
            horizontal = MaterialTheme.dimension.large
        ),
        colors = colors,
        modifier = modifier,
        enabled = isActive
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/**
Composable with passed isInCart state for example
product detail view and those two separate add to cart buttons
 */
@Composable
fun ProductBuyButton(
    modifier: Modifier = Modifier,
    productId: Int,
    isActive: Boolean,
    isInCart: Boolean,
    onInCartChange: (Boolean) -> Unit,
    onProductAction: suspend (ProductAction) -> Boolean
) {
    val scope = rememberCoroutineScope()
    val text: String
    val colors: ButtonColors
    val action: ProductAction
    if (!isActive) {
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0, 111, 238, 12),
            contentColor = MaterialTheme.colorScheme.primary
        )
        text = "Товара нет в наличии"
        // button will be disabled
        action = ProductAction.AddToCart(productId, LocalSnackbarHostState.current)
    } else {
        if (isInCart) {
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 111, 238, 12),
                contentColor = MaterialTheme.colorScheme.primary
            )
            text = "В корзине"
            action = ProductAction.RemoveFromCart(productId, LocalSnackbarHostState.current)
        } else {
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.tertiary
            )
            text = "В корзину"
            action = ProductAction.AddToCart(productId, LocalSnackbarHostState.current)
        }
    }
    Button(
        onClick = {
            scope.launch {
                onInCartChange(onProductAction(action))
            }
        },
        contentPadding = PaddingValues(
            vertical = MaterialTheme.dimension.medium,
            horizontal = MaterialTheme.dimension.large
        ),
        colors = colors,
        modifier = modifier,
        enabled = isActive
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}