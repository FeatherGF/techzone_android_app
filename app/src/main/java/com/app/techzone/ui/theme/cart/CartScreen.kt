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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.main.ProductCrossedPrice
import com.app.techzone.ui.theme.main.ProductFavoriteIcon
import com.app.techzone.ui.theme.main.ProductImageOrPreview
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.ConfirmationModalSheet
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UnauthorizedScreen
import com.app.techzone.ui.theme.profile.auth.UserViewModel
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatCommonCase
import com.app.techzone.utils.formatPrice
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun CartScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    addToFavorite: (Int) -> Int,
    removeFromFavorite: (Int) -> Int,
) {
    LaunchedEffect(addToFavorite, removeFromFavorite) {
        userViewModel.loadCart()
    }
    val cartItems by userViewModel.cartItems.collectAsState()
    when (userViewModel.state.response) {
        ServerResponse.LOADING -> {
            LoadingBox()
        }

        ServerResponse.ERROR -> {
            ErrorScreen(userViewModel::loadCart)
        }

        ServerResponse.UNAUTHORIZED -> {
            UnauthorizedScreen {
                navController.navigate(ScreenRoutes.PROFILE_REGISTRATION)
            }
        }

        ServerResponse.SUCCESS -> {
            if (cartItems.isEmpty()) {
                EmptyCartScreen {
                    navController.navigate(ScreenRoutes.CATALOG)
                }
            } else {
                CartItemsList(
                    cartItems,
                    addToFavorite = addToFavorite,
                    removeFromFavorite = removeFromFavorite,
                    navController = navController,
//                    navigateToDetail = { navController.navigate("catalog/$it") }
                )
            }
        }
    }
}


@Composable
fun EmptyCartScreen(navigateToCatalog: () -> Unit) {
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
fun CartItemsList(
    cartItems: List<OrderItem>,
    addToFavorite: (Int) -> Int,
    removeFromFavorite: (Int) -> Int,
    navController: NavController,
) {
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
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
                        addToFavorite = addToFavorite,
                        removeFromFavorite = removeFromFavorite,
                        navigateToDetail = { navController.navigate("catalog/$it") },
                        onRemoveFromCart = { deleteProductId = it }
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
                            val discountPrice = selectedItems
                                .associate { (it.product.price to it.product.discountPercentage) to it.mutableQuantity.intValue }
                                .map { (pair, quantity) ->
                                    calculateDiscount(
                                        initialPrice = pair.first,
                                        discountPercentage = pair.second
                                    ) * quantity
                                }
                                .reduce { acc, i -> acc + i }

                            val maxPrice = selectedItems
                                .map { it.product.price * it.mutableQuantity.intValue }
                                .reduce { acc, i -> acc + i }

                            Text(
                                text = formatPrice(discountPrice),
                                style = MaterialTheme.typography.titleLarge,
                            )

                            if (discountPrice != maxPrice)
                                ProductCrossedPrice(price = maxPrice, large = true)
                        }
                    }
                    Button(
                        onClick = { navController.navigate(ScreenRoutes.PURCHASE) },
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
                onConfirm = { /*TODO userViewModel.removeFromCart(productId)*/ },
                onDismiss = { deleteProductId = null }
            )
        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun CartItemCard(
    orderItem: OrderItem,
    addToFavorite: (Int) -> Int,
    removeFromFavorite: (Int) -> Int,
    navigateToDetail: (Int) -> Unit,
    onRemoveFromCart: (Int) -> Unit,
) {
    var quantity by orderItem.mutableQuantity
    var isFirstComposition by remember { mutableStateOf(true) }
    LaunchedEffect(quantity) {
        // we don't need to send patch request with initial data.
        // only send it when quantity actually changes
        if (isFirstComposition){
            isFirstComposition = false
        } else {
            snapshotFlow { quantity }.debounce(2000L).distinctUntilChanged().collect {
                println("SEND PATCH TO ORDER WITH QUANTITY: $it")
            }
        }
    }
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        onClick = { navigateToDetail(orderItem.product.id) },
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
                ProductImageOrPreview(orderItem.product.photos, modifier = Modifier.size(110.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        orderItem.product.name,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                        style = MaterialTheme.typography.labelLarge
                    )
                    ProductCrossedPrice(product = orderItem.product, large = true)
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
                        product = orderItem.product,
                        addToFavorite = addToFavorite,
                        removeFromFavorite = removeFromFavorite,
                        sizeDp = 28.dp
                    )
                    IconButton(
                        onClick = { onRemoveFromCart(orderItem.product.id) },
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
                        onClick = {
                            quantity--
                            println("CHANGED REMOVE")
                        },
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
                        onClick = {
                            quantity++
                            println("CHANGED ADD")
                        },
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