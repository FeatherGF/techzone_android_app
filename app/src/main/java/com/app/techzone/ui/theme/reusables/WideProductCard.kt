package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice

@Composable
fun WideProductCard(
    modifier: Modifier = Modifier,
    product: BaseProduct,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val navController = LocalNavController.current
    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        onClick = { navController.navigate("${ScreenRoutes.PRODUCT_DETAIL}/${product.id}") },
        shape = RoundBorder24,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        border = BorderStroke(width = 1.dp, color = ForStroke.copy(alpha = 0.1f)),
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProductImageOrPreview(modifier.size(110.dp), photos = product.photos)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        product.name,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ProductRating(
                            rating = product.rating,
                            textStyle = MaterialTheme.typography.labelLarge
                        )
                        ProductReviewCount(
                            reviewsCount = product.reviewsCount,
                            textStyle = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            Row(
                modifier = modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    ProductCrossedPrice(
                        price = product.price,
                        discountPercentage = product.discountPercentage,
                        large = true
                    )
                    Text(
                        formatPrice(
                            calculateDiscount(
                                product.price,
                                product.discountPercentage
                            )
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
                Row(
                    modifier = modifier.width(190.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ProductFavoriteIcon(
                        modifier.size(32.dp),
                        productId = product.id,
                        onProductCheckStatus = onProductCheckStatus,
                        onProductAction = onProductAction,
                    )
                    ProductBuyButton(
                        productId = product.id,
                        onProductCheckStatus = onProductCheckStatus,
                        onProductAction = onProductAction
                    )
                }
            }
        }
    }
}