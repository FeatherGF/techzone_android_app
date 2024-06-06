package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.Order
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.data.remote.model.OrderStatus
import com.app.techzone.ui.theme.dimension
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
                    .padding(
                        start = MaterialTheme.dimension.extendedMedium,
                        end = MaterialTheme.dimension.extendedMedium,
                        bottom = MaterialTheme.dimension.extendedMedium
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.mediumLarge)
            ) {
                Text("Состав заказа", style = MaterialTheme.typography.titleLarge)
                orderItems.forEach { orderItem ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.medium),
                        modifier = if (!orderItem.product.isDeleted) Modifier.clickable {
                            navController.navigate(
                                "${ScreenRoutes.PRODUCT_DETAIL}/${orderItem.product.id}"
                            )
                        } else Modifier
                    ) {
                        ProductImageOrPreview(
                            Modifier.size(MaterialTheme.dimension.huge),
                            photos = orderItem.product.photos,
                            filterQuality = FilterQuality.Low
                        )
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small)
                        ) {
                            Text(
                                "${orderItem.product.name} (${orderItem.quantity} шт.)",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small),
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
                                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.medium)
                                ) {
                                    when (it) {
                                        OrderStatus.GOT -> {
                                            val text: String
                                            val colors: ButtonColors
                                            if (orderItem.product.reviewId != null) {
                                                text = "Изменить отзыв"
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                                    contentColor = MaterialTheme.colorScheme.primary
                                                )
                                            } else {
                                                text = "Оставить отзыв"
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary,
                                                    contentColor = MaterialTheme.colorScheme.tertiary
                                                )
                                            }
                                            Button(
                                                onClick = {
                                                    navController.navigate(
                                                        ScreenRoutes.ADD_REVIEW + "?orderId=${orderId}&productId=${orderItem.product.id}"
                                                    )
                                                },
                                                contentPadding = PaddingValues(
                                                    horizontal = MaterialTheme.dimension.extendedMedium,
                                                    vertical = MaterialTheme.dimension.small
                                                ),
                                                colors = colors
                                            ) {
                                                Text(text, style = MaterialTheme.typography.labelLarge)
                                            }
                                        }

                                        else -> {
                                            if (orderItem.product.isDeleted){
                                                Button(onClick = {}, enabled = false) {
                                                    Text(
                                                        "Продажи прекращены",
                                                        style = MaterialTheme.typography.labelMedium
                                                    )
                                                }
                                            } else {
                                                ProductBuyButton(
                                                    productId = orderItem.product.id,
                                                    isActive = orderItem.product.isActive,
                                                    onProductCheckStatus = onProductCheckStatus,
                                                    onProductAction = onProductAction
                                                )
                                                ProductFavoriteIcon(
                                                    Modifier.size(MaterialTheme.dimension.larger),
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
}