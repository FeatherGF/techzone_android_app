package com.app.techzone.ui.theme.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.techzone.R
import com.app.techzone.calculateDiscount
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.formatPrice
import com.app.techzone.formatReview
import com.app.techzone.model.Benefit
import com.app.techzone.model.ProductCard
import com.app.techzone.model.benefits
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.ui.theme.RoundBorder24


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerCarousel() {
    val bannerSlides = listOf(
        R.drawable.ic_banner,
        R.drawable.ic_banner2,
        R.drawable.ic_banner3,
    )
    val pagerState = rememberPagerState(pageCount = { bannerSlides.size })
    Box(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(205.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            key = { bannerSlides[it] },
            pageSize = PageSize.Fixed(316.dp),
            pageSpacing = 8.dp,
        ) { index ->
            Image(
                painter = painterResource(id = bannerSlides[index]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCarousel(
    navigateToDetail: (productId: Int) -> Unit,
    products: List<BaseProduct>,
    addToFavorite: (ProductCard) -> Unit,
    removeFromFavorite: (ProductCard) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { products.size })
    Box(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, bottom = 24.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            key = { products[it].id },
            pageSize = PageSize.Fixed(154.dp),
            pageSpacing = 8.dp,
        ) { index ->
            val product = products[index]
            Box(
                modifier = Modifier
                    .height(336.dp)
                    .width(154.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary, shape = RoundBorder24
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.1f),
                        shape = RoundBorder24
                    )
                    .clickable { navigateToDetail(product.id) }
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 14.dp, top = 12.dp, end = 14.dp, bottom = 17.dp
                        )
                        .width(128.dp),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.photos[0].url)
//                            .size(130)
                            .build(),
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 9.dp)
                            .height(114.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .height(30.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                ProductCrossedPrice(product = product)
                                Text(
                                    text = formatPrice(
                                        calculateDiscount(
                                            product.price,
                                            product.discountPercentage
                                        )
                                    ),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                )
                            }
                            ProductFavoriteIcon(
                                product = product,
//                                addToFavorite = addToFavorite,
//                                removeFromFavorite = removeFromFavorite
                            )
                        }
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .height(48.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductRating(product = product)
                            ProductReviewCount(product = product)
                        }
                    }
                    ProductBuyButton(product = product)
                }
            }
        }
    }
}


@Composable
fun ProductBuyButton(product: BaseProduct? = null) {
    var isInCartState by remember { mutableStateOf(false)}
    if (isInCartState) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundBorder100,
                )
                .clickable {
                    isInCartState = false
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "В корзине",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundBorder100
                )
                .clickable {
                    isInCartState = true
                    // TODO: add to cart here
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "В корзину",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}


@Composable
fun ProductCrossedPrice(product: IBaseProduct) {
    if (product.discountPercentage > 0){
        Text(
            text = formatPrice(product.price),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.scrim,
            textDecoration = TextDecoration.LineThrough
        )
    }
}


@Composable
fun ProductRating(
    product: IBaseProduct,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    isStarFilled: Boolean = false
) {
    product.rating?.let{rating ->
        if (rating > 0) {
            Row {
                Text(
                    text = product.rating.toString(),
                    style = textStyle,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (isStarFilled) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .height(14.dp)
                        .width(14.dp)
                )
            }
        }
    }
}


@Composable
fun ProductReviewCount(
    product: IBaseProduct,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    if (product.reviewsCount > 0){
        Text(
            text = formatReview(product.reviewsCount),
            style = textStyle,
            color = Color.Companion.Black.copy(alpha = 0.5f),
        )
    }
}


@Composable
fun ProductFavoriteIcon(
    product: IBaseProduct,
    sizeDp: Dp = 24.dp,
//    addToFavorite: (ProductCard) -> Unit,
//    removeFromFavorite: (ProductCard) -> Unit,
) {
    var isFavoriteState by rememberSaveable { mutableStateOf(product.isFavorite) }
    Icon(
        imageVector = if (isFavoriteState) Icons.Filled.Favorite else {
            Icons.Outlined.FavoriteBorder
        },
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(sizeDp)
            .clickable {
//               } if (isFavoriteState) {
////                    removeFromFavorite(product)
////                } else {
////                    addToFavorite(product)
////
                isFavoriteState = !isFavoriteState
            }
    )

}


@Composable
fun BenefitItem(benefit: Benefit) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(140.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary, shape = RoundBorder24
            )
            .padding(start = 44.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(44.dp, alignment = Alignment.CenterHorizontally),

    ) {
        Box(
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = benefit.imageId,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = benefit.text,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MainScreen(
    navigateToDetail: (productId: Int) -> Unit,
    newProducts: List<BaseProduct>,
    bestSellerProducts: List<BaseProduct>,
    addToFavorite: (ProductCard) -> Unit,
    removeFromFavorite: (ProductCard) -> Unit,
) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        item { BannerCarousel() }

        item {
            Text(
                text = "Новинки",
                modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            ProductCarousel(
                navigateToDetail = navigateToDetail,
                products = newProducts,
                addToFavorite = addToFavorite,
                removeFromFavorite = removeFromFavorite
            )
        }

        item {
            Text(
                text = "Хиты продаж",
                modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            ProductCarousel(
                navigateToDetail = navigateToDetail,
                products = bestSellerProducts,
                addToFavorite = addToFavorite,
                removeFromFavorite = removeFromFavorite
            )
        }

        items(benefits) { benefit ->
            BenefitItem(benefit)
        }
    }
}
