package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice

@Composable
fun ProductCarousel(
    products: List<IBaseProduct>,
    onProductCheckStatus: (CheckProductStatus) -> Boolean = { true },
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val navController = LocalNavController.current
    LazyRow(
        contentPadding = PaddingValues(end = 16.dp, start = 16.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(
            products,
            key = { it.id },
            contentType = { it::class }
        ) { product ->
            OutlinedCard(
                onClick = { navController.navigate("${ScreenRoutes.PRODUCT_DETAIL}/${product.id}") },
                shape = RoundBorder24,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.1f)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                ),
                modifier = Modifier
                    .height(323.dp)
                    .width(154.dp)
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(start = 14.dp, top = 12.dp, end = 14.dp)
                ) {
                    ProductImageOrPreview(Modifier.size(127.dp), photos = product.photos)
                    Column(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .height(112.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                ProductCrossedPrice(
                                    price = product.price,
                                    discountPercentage = product.discountPercentage
                                )
                                Text(
                                    text = formatPrice(
                                        calculateDiscount(
                                            product.price,
                                            product.discountPercentage
                                        )
                                    ),
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                            ProductFavoriteIcon(
                                Modifier.size(24.dp),
                                productId = product.id,
                                onProductCheckStatus = onProductCheckStatus,
                                onProductAction = onProductAction
                            )
                        }
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.height(48.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductRating(rating = product.rating)
                            ProductReviewCount(reviewsCount = product.reviewsCount)
                        }
                    }
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