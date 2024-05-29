package com.app.techzone.ui.theme.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.LocalSnackbarHostState
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.data.remote.model.totalDiscountPrice
import com.app.techzone.data.remote.model.totalPrice
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.profile.auth.AuthState
import com.app.techzone.ui.theme.reusables.ConfirmationModalSheet
import com.app.techzone.ui.theme.reusables.ProductCrossedPrice
import com.app.techzone.ui.theme.reusables.ProductFavoriteIcon
import com.app.techzone.ui.theme.reusables.ProductImageOrPreview
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.UnauthorizedScreen
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatCommonCase
import com.app.techzone.utils.formatPrice
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@Composable
fun CartScreen(
    cartItems: List<OrderItem>,
    state: AuthState,
    loadCart: () -> Unit,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    LaunchedEffect(cartItems.size) { loadCart() }
    when (state.response) {
        ServerResponse.LOADING -> {
            LoadingBox()
        }

        ServerResponse.ERROR -> {
            ErrorScreen(loadCart)
        }

        ServerResponse.UNAUTHORIZED -> {
            UnauthorizedScreen()
        }

        ServerResponse.SUCCESS -> {
            if (cartItems.isEmpty()) {
                EmptyCartScreen()
            } else {
                CartItemsList(
                    cartItems = cartItems,
                    onProductCheckStatus = onProductCheckStatus,
                    onProductAction = onProductAction,
                )
            }
        }
    }
}


@Composable
private fun EmptyCartScreen() {
    val navController = LocalNavController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, top = 120.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                "Ваша корзина пуста",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )
            Button(onClick = { navController.navigate(ScreenRoutes.CATALOG) }) {
                Text(
                    "Перейти в каталог",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Composable
private fun CartItemsList(
    cartItems: List<OrderItem>,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current
    val stateProductMapping: Map<MutableState<Boolean>, OrderItem> =
        cartItems.associate { orderItem ->
            remember { mutableStateOf(true) } to orderItem.also {
                it.mutableQuantity = remember { mutableIntStateOf(it.quantity) }
            }
        }
    val areAllChosen: Boolean by remember {
        derivedStateOf {
            stateProductMapping.keys.all { state -> state.value }
        }
    }
    val selectedItems: List<OrderItem> = stateProductMapping
        .filter { (state, _) -> state.value }
        .map { it.value }
        .toList()
    val bottomPadding = if (selectedItems.isNotEmpty()) (12 + 72).dp else 12.dp
    var deleteProductId: Int? by remember { mutableStateOf(null) }
    var clearCart by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 11.dp, top = 32.dp, bottom = bottomPadding, end = 16.dp),
        ) {
            Row(modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = "Корзина ",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
                Text(
                    cartItems.size.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = areAllChosen,
                        onCheckedChange = {
                            stateProductMapping.keys.map { state -> state.value = it }
                        }
                    )
                    Text(
                        "Выбрать все",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
                OutlinedButton(
                    onClick = { clearCart = true },
                    border = null,
                    contentPadding = PaddingValues(start = 12.dp, end = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(26.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            "Удалить все",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            stateProductMapping.forEach { (state, product) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Checkbox(
                        checked = state.value,
                        onCheckedChange = { state.value = it }
                    )
                    CartItemCard(
                        orderItem = product,
                        onProductCheckStatus = onProductCheckStatus,
                        onProductAction = onProductAction,
                        onShowModal = { deleteProductId = it }
                    )
                }
            }
        }

        if (selectedItems.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(72.dp),
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                border = BorderStroke(width = 1.dp, color = ForStroke),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Итого ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            )
                            Text(
                                formatCommonCase(selectedItems.size, "товар"),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.scrim
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val totalDiscountPrice = selectedItems.totalDiscountPrice()
                            val totalPrice = selectedItems.totalPrice()
                            Text(
                                text = formatPrice(totalDiscountPrice),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            if (totalDiscountPrice != totalPrice)
                                ProductCrossedPrice(price = totalPrice, large = true)
                        }
                    }
                    Button(
                        onClick = {
                            val orderItemIdsQuery = selectedItems
                                .map { it.id }
                                .joinToString("&") { "orderItem=$it" }
                            navController.navigate("${ScreenRoutes.PURCHASE}?$orderItemIdsQuery")
                        },
                        modifier = Modifier.wrapContentWidth(),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                    ) {
                        Text(
                            "К оформлению",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
        deleteProductId?.let { productId ->
            ConfirmationModalSheet(
                confirmationText = "Вы действительно хотите удалить товар из корзины?",
                onConfirm = {
                    scope.launch {
                        onProductAction(ProductAction.RemoveFromCart(productId, snackbarHostState))
                        deleteProductId = null
                    }
                },
                onDismiss = { deleteProductId = null }
            )
        }
        if (clearCart) {
            ConfirmationModalSheet(
                confirmationText = "Вы действительно хотите удалить все товары из корзины?",
                onConfirm = {
                    scope.launch {
                        onProductAction(ProductAction.ClearCart(snackbarHostState))
                        clearCart = false
                    }
                },
                onDismiss = { clearCart = false }
            )
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
private fun CartItemCard(
    orderItem: OrderItem,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
    onShowModal: (Int) -> Unit,
) {
    val navController = LocalNavController.current
    var quantity by orderItem.mutableQuantity
    var isFirstComposition by remember { mutableStateOf(true) }
    LaunchedEffect(quantity) {
        // we don't need to send patch request with initial data.
        // only send it when quantity actually changes
        if (isFirstComposition) {
            isFirstComposition = false
        } else {
            snapshotFlow { quantity }.debounce(500L).distinctUntilChanged().collect {
                onProductAction(ProductAction.ChangeQuantityInCart(orderItem.product.id, quantity))
            }
        }
    }
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        onClick = { navController.navigate("${ScreenRoutes.PRODUCT_DETAIL}/${orderItem.product.id}") },
        shape = RoundBorder24,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        border = BorderStroke(width = 1.dp, color = ForStroke.copy(alpha = 0.1f)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProductImageOrPreview(Modifier.size(110.dp), photos = orderItem.product.photos)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        orderItem.product.name,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                        style = MaterialTheme.typography.labelLarge
                    )
                    ProductCrossedPrice(
                        price = orderItem.product.price,
                        discountPercentage = orderItem.product.discountPercentage,
                        large = true
                    )
                    Text(
                        formatPrice(
                            calculateDiscount(
                                orderItem.product.price,
                                orderItem.product.discountPercentage
                            )
                        ),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ProductFavoriteIcon(
                        Modifier.size(28.dp),
                        productId = orderItem.product.id,
                        onProductCheckStatus = onProductCheckStatus,
                        onProductAction = onProductAction,
                    )
                    IconButton(
                        onClick = { onShowModal(orderItem.product.id) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { quantity-- },
                        enabled = quantity > 1,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = null,
                        )
                    }
                    Text(
                        quantity.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                    IconButton(
                        onClick = { quantity++ },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}