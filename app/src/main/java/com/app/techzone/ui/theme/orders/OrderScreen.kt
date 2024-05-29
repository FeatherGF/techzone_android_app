package com.app.techzone.ui.theme.orders

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.Order
import com.app.techzone.data.remote.model.OrderStatus
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.Success
import com.app.techzone.ui.theme.SuccessHalfOpacity
import com.app.techzone.ui.theme.reusables.ProductImageOrPreview
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.server_response.UnauthorizedScreen
import com.app.techzone.ui.theme.profile.UserViewModel
import com.app.techzone.ui.theme.reusables.OrderComposition
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.formatDateShort
import kotlinx.coroutines.launch

@Composable
fun OrderScreenRoot(
    userViewModel: UserViewModel,
) {
    LaunchedEffect(Unit) {
        userViewModel.loadOrders()
    }

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
                OrderHistory(
                    orders,
                    onProductCheckStatus = userViewModel::onCheckProduct,
                    onProductAction = userViewModel::onProductAction,
                    getOrderDetail = userViewModel::getDetailedOrder,
                )
            }
        }
    }
}

@Composable
private fun EmptyOrderHistory() {
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
private fun OrdersTopBar() {
    val navController = LocalNavController.current
    BackHandler { navController.navigate(ScreenRoutes.PROFILE) }
    Row(
        Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = ForStroke)
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.navigate(ScreenRoutes.PROFILE) }) {
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
private fun OrderHistory(
    orders: List<Order>,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
    getOrderDetail: suspend (Int) -> Order?,
) {
    val scope = rememberCoroutineScope()
    var selectedOrderId: Int? by remember { mutableStateOf(null) }
    var selectedOrder: Order? by remember { mutableStateOf(null) }
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        OrdersTopBar()
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
                            "Заказ от ${formatDateShort(order.dateCreated)}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.scrim
                        )
                    }

                    val orderStatus = OrderStatus.valueOf(order.status.uppercase())
                    val containerColor: Color
                    val textColor: Color
                    val text: String
                    when (orderStatus) {
                        OrderStatus.ASSEMBLY -> {
                            text = "В сборке"
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                            textColor = MaterialTheme.colorScheme.primary
                        }

                        OrderStatus.READY -> {
                            text = "Готов к выдаче"
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                            textColor = MaterialTheme.colorScheme.primary
                        }

                        OrderStatus.GOT -> {
                            text = "Получен"
                            containerColor = SuccessHalfOpacity
                            textColor = Success
                        }

                        else -> { // not possible here
                            text = ""
                            containerColor = Color.Unspecified
                            textColor = Color.Unspecified
                        }
                    }
                    Surface(
                        color = containerColor,
                        contentColor = textColor,
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            text,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        order.orderItems.forEach { orderItem ->
                            ProductImageOrPreview(
                                Modifier.size(60.dp),
                                photos = orderItem.product.photos,
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
            HorizontalDivider(thickness = 1.dp, color = ForStroke)
        }
        selectedOrderId?.let { selectedId ->
            scope.launch {
                getOrderDetail(selectedId)?.let { order ->
                    selectedOrder = order
                }
            }
            selectedOrder?.let { order ->
                OrderComposition(
                    order,
                    onDismiss = {
                        selectedOrderId = null
                        selectedOrder = null
                    },
                    onProductCheckStatus = onProductCheckStatus,
                    onProductAction = onProductAction
                )
            }
        }
    }
}
