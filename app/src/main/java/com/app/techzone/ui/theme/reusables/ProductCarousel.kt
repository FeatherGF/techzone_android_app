package com.app.techzone.ui.theme.reusables

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice

@Composable
fun ProductCarousel(
    products: LazyPagingItems<BaseProduct>,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    LazyRow(
        contentPadding = PaddingValues(end = 16.dp, start = 16.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
//        items(
//            products,
//            key = { it.id },
//            contentType = { it::class }
//        ) { product ->
//            ProductCompactCard(
//                product = product,
//                onProductCheckStatus = onProductCheckStatus,
//                onProductAction = onProductAction
//            )
//        }
        if (products.loadState.refresh is LoadState.Loading) {
            items(3) {
                ProductCardPlaceholder()
            }
        }
        items(
            count = products.itemCount,
            key = products.itemKey { it.id },
            contentType = products.itemContentType { "BaseProduct" }
        ) { index ->
            val product = products[index]
            product?.let {
                ProductCompactCard(
                    product = it,
                    onProductCheckStatus = onProductCheckStatus,
                    onProductAction = onProductAction
                )
            }
        }
        item {
            if (products.loadState.append is LoadState.Loading){
                ProductCardPlaceholder()
            }
        }
    }
}

@Composable
private fun ProductCardPlaceholder() {
    OutlinedCard(
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
            .width(154.dp),
    ) {
        Column(
            modifier = Modifier.padding(start = 14.dp, top = 12.dp, end = 14.dp)
        ) {
            Box(
                Modifier
                    .size(127.dp)
                    .shimmerEffect())
            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(112.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .shimmerEffect())
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .shimmerEffect())
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(top = 8.dp)
                    .shimmerEffect())
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "InfiniteShimmerEffect")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1500)
        ),
        label = "InfiniteShimmerEffect"
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray,
                Color(0xFFB8B1B1),
                Color.LightGray,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

@Composable
private fun ProductCompactCard(
    product: IBaseProduct,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean
) {
    val navController = LocalNavController.current
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