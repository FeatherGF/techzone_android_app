package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.Order
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.data.remote.model.OrderStatus
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice

@Composable
fun OrderComposition(
    order: Order,
    onDismiss: () -> Unit,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) = OrderCompositionHolder().OrderCompositionInternal(
    orderItems = order.orderItems,
    onDismiss = onDismiss,
    orderStatus = OrderStatus.valueOf(order.status.uppercase()),
    onProductCheckStatus = onProductCheckStatus,
    onProductAction = onProductAction,
    orderId = order.id
)

@Composable
fun OrderComposition(
    orderItems: List<OrderItem>,
    onDismiss: () -> Unit,
) = OrderCompositionHolder().OrderCompositionInternal(
    orderItems = orderItems,
    onDismiss = onDismiss,
    orderStatus = null,
    orderId = null,
    onProductCheckStatus = { false },
    onProductAction = { false }
)

private class OrderCompositionHolder {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OrderCompositionInternal(
        orderItems: List<OrderItem>,
        onDismiss: () -> Unit,
        orderStatus: OrderStatus? = null,
        orderId: Int? = null,
        onProductCheckStatus: (CheckProductStatus) -> Boolean,
        onProductAction: suspend (ProductAction) -> Boolean,
    ) {
        val navController = LocalNavController.current
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("Состав заказа", style = MaterialTheme.typography.titleLarge)
                orderItems.forEach { orderItem ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProductImageOrPreview(
                            Modifier.size(60.dp),
                            photos = orderItem.product.photos,
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
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                )
                            }
                            orderStatus?.let {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    when (it) {
                                        OrderStatus.GOT -> {
                                            Button(
                                                onClick = {
                                                    navController.navigate(
                                                        ScreenRoutes.ADD_REVIEW + "?orderId=${orderId}&productId=${orderItem.product.id}"
                                                    )
                                                }
                                            ) {
                                                Text(
                                                    if (orderItem.product.reviewId != null)
                                                        "Изменить отзыв"
                                                    else
                                                        "Оставить отзыв"
                                                )
                                            }
                                        }

                                        else -> {
                                            ProductBuyButton(
                                                productId = orderItem.product.id,
                                                onProductCheckStatus = onProductCheckStatus,
                                                onProductAction = onProductAction
                                            )
                                            ProductFavoriteIcon(
                                                Modifier.size(32.dp),
                                                productId = orderItem.product.id,
                                                onProductCheckStatus = onProductCheckStatus,
                                                onProductAction = onProductAction
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}