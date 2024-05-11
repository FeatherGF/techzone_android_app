package com.app.techzone.ui.theme.orders

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.Order
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.main.ProductCrossedPrice
import com.app.techzone.ui.theme.main.ProductImageOrPreview
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UnauthorizedScreen
import com.app.techzone.ui.theme.profile.UserViewModel
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice

@Composable
fun OrderScreenRoot(
    userViewModel: UserViewModel,
) {
    LaunchedEffect(userViewModel.orders) {
        userViewModel.loadOrders()
    }

//    val orders = emptyList<Order>()
    val orders by userViewModel.orders.collectAsStateWithLifecycle()
    when (userViewModel.state.response) {
        ServerResponse.LOADING -> {
            LoadingBox()
        }

        ServerResponse.ERROR -> {
            ErrorScreen(userViewModel::loadOrders)
        }

        ServerResponse.UNAUTHORIZED -> {
            UnauthorizedScreen()
        }

        ServerResponse.SUCCESS -> {
            if (orders.isEmpty()) {
                EmptyOrderHistory()
            } else {
                OrderHistory(orders)
            }
        }
    }
}

@Composable
fun EmptyOrderHistory() {
    val navController = LocalNavController.current
    Column {
        OrdersTopBar()
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
                Button(onClick = { navController.navigate(ScreenRoutes.CATALOG) }) {
                    Text(
                        "Перейти в каталог",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

    }
}

@Composable
fun OrdersTopBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = ForStroke)
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navController = LocalNavController.current

        IconButton(onClick = navController::popBackStack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
            )
        }
        Text(
            "Мои заказы",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
        )
        // filler
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.Transparent
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderHistory(orders: List<Order>) {
    var selectedOrderId: Int? by remember { mutableStateOf(null) }
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        OrdersTopBar()

        // content
        orders.forEach { order ->
            Row(
                Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "№ ${order.id}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Заказ от ...",  // TODO: добавить в ответ от бека дату заказа
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.scrim
                        )
                    }

                    // order status
                    Surface(
                        // если статус "Получен" то менять на зеленый
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        // only for testing
                        val text = if (order.status == "cart") "В сборке" else "Получен"
                        Text(
                            text,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    FlowRow {
                        order.orderItem.forEach { orderItem ->
                            ProductImageOrPreview(
                                photos = orderItem.product.photos,
                                modifier = Modifier.size(60.dp),
                                filterQuality = FilterQuality.None
                            )
                        }
                    }
                }

                IconButton(onClick = { selectedOrderId = order.id }) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }
            }
        }
        selectedOrderId?.let { selectedId ->
            orders.find { it.id == selectedId }?.let { selectedOrder ->
                OrderComposition(
                    selectedOrder,
                    onDismiss = { selectedOrderId = null },
                    showActions = true
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderComposition(
    order: Order,
    onDismiss: () -> Unit,
    showActions: Boolean = false,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Состав заказа", style = MaterialTheme.typography.titleLarge)
            order.orderItem.forEach { orderItem ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProductImageOrPreview(
                        photos = orderItem.product.photos,
                        modifier = Modifier.size(60.dp),
                        filterQuality = FilterQuality.None
                    )
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "${orderItem.product.name} (${orderItem.quantity} шт.)",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductCrossedPrice(
                                product = orderItem.product,
                                large = true
                            )
                            Text(
                                formatPrice(
                                    calculateDiscount(
                                        orderItem.product.price,
                                        orderItem.product.discountPercentage
                                    )
                                ),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                            )
                        }
                        if (showActions){
                            Row {
                                Button(onClick = { }) {
                                    Text("Оставить отзыв")
                                }
                                // TODO: order status enum
//                                if (selectedOrder.status == "get") {  // получен
//                                    Button(onClick = {  }) {
//                                        Text("Оставить отзыв")
//                                    }
//                                } else {
//                                    ProductBuyButton()
//                                    ProductFavoriteIcon(
//                                        product = orderItem.product,
//                                        addToFavorite = ,
//                                        removeFromFavorite =
//                                    )
//                                }
                            }
                        }
                    }
                }
            }
        }
    }
}