package com.app.techzone.ui.theme.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.techzone.LocalNavController
import com.app.techzone.LocalSnackbarHostState
import com.app.techzone.R
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.data.remote.model.Photo
import com.app.techzone.utils.formatPrice
import com.app.techzone.model.Benefit
import com.app.techzone.model.benefits
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.formatCommonCase
import kotlinx.coroutines.launch


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
    products: List<IBaseProduct>,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val pagerState = rememberPagerState(pageCount = { products.size })
    val navController = LocalNavController.current
    HorizontalPager(
        state = pagerState,
        key = { it.hashCode() },
        pageSize = PageSize.Fixed(154.dp),
        contentPadding = PaddingValues(end = 16.dp, start = 16.dp, top = 16.dp, bottom = 16.dp),
        pageSpacing = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) { index ->
        val product = products[index]
        OutlinedCard(
            onClick = { navController.navigate("${ScreenRoutes.CATALOG}/${product.id}") },
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
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(start = 14.dp, top = 12.dp, end = 14.dp)
            ) {
                ProductImageOrPreview(product.photos, modifier = Modifier.size(127.dp))
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
                            ProductCrossedPrice(product = product)
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
                            product = product,
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
                        ProductRating(product = product)
                        ProductReviewCount(product = product)
                    }
                }
                ProductBuyButton(
                    product = product,
                    onProductAction = onProductAction
                )
            }
        }
    }
}


@Composable
fun ProductImageOrPreview(
    photos: List<Photo>,
    photoIndex: Int = 0,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    filterQuality: FilterQuality = FilterQuality.Low,
    description: String? = null
) {
    if (photos.isNotEmpty()){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photos[photoIndex].url)
                .build(),
            contentDescription = description,
            filterQuality = filterQuality,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_preview),
            contentDescription = description,
            modifier = modifier
        )
    }
}

@Composable
fun ProductBuyButton(
    modifier: Modifier = Modifier,
    product: IBaseProduct,
    onProductAction: suspend (ProductAction) -> Boolean
) {
    val scope = rememberCoroutineScope()
    var isInCartState by remember {
        mutableStateOf(product.isInCart)
    }
    val text: String
    val colors: ButtonColors
    val action: ProductAction
    if (isInCartState) {
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0, 111, 238, 12),
            contentColor = MaterialTheme.colorScheme.primary
        )
        text = "В корзине"
        action = ProductAction.RemoveFromCart(product.id)
    } else {
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.tertiary
        )
        text = "В корзину"
        action = ProductAction.AddToCart(product.id)
    }
    Button(
        onClick = {
            scope.launch {
                isInCartState = onProductAction(action)
            }
        },
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 29.dp),
        colors = colors,
        modifier = modifier
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}


/**
 Composable with passed isInCart state for example
 product detail view and those two separate add to cart buttons
 */
@Composable
fun ProductBuyButton(
    modifier: Modifier = Modifier,
    productId: Int,
    isInCart: Boolean,
    onInCartChange: (Boolean) -> Unit,
    onProductAction: suspend (ProductAction) -> Boolean
) {
    val scope = rememberCoroutineScope()
    val text: String
    val colors: ButtonColors
    val action: ProductAction
    if (isInCart) {
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0, 111, 238, 12),
            contentColor = MaterialTheme.colorScheme.primary
        )
        text = "В корзине"
        action = ProductAction.RemoveFromCart(productId)
    } else {
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.tertiary
        )
        text = "В корзину"
        action = ProductAction.AddToCart(productId)
    }
    Button(
        onClick = {
            scope.launch {
                onInCartChange(onProductAction(action))
            }
        },
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 29.dp),
        colors = colors,
        modifier = modifier
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}


@Composable
fun ProductCrossedPrice(product: IBaseProduct, large: Boolean = false) {
    if (product.discountPercentage > 0){
        val style = if (large) MaterialTheme.typography.labelLarge else
            MaterialTheme.typography.labelSmall
        Text(
            text = formatPrice(product.price),
            style = style,
            color = MaterialTheme.colorScheme.scrim,
            textDecoration = TextDecoration.LineThrough,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}

@Composable
fun ProductCrossedPrice(price: Int, large: Boolean = false) {
    val style = if (large) MaterialTheme.typography.labelLarge else
        MaterialTheme.typography.labelSmall
    Text(
        text = formatPrice(price),
        style = style,
        color = MaterialTheme.colorScheme.scrim,
        textDecoration = TextDecoration.LineThrough,
        modifier = Modifier.padding(bottom = 2.dp)
    )
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
            text = formatCommonCase(product.reviewsCount, "отзыв"),
            style = textStyle,
            color = Color.Companion.Black.copy(alpha = 0.5f),
        )
    }
}


@Composable
fun ProductFavoriteIcon(
    product: IBaseProduct,
    sizeDp: Dp = 24.dp,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val scope = rememberCoroutineScope()
    var isFavorite by remember {
        mutableStateOf(product.isFavorite)
    }
    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current
    IconButton(
        onClick = {
            scope.launch {
                isFavorite = if (isFavorite) {
                    !onProductAction(
                        ProductAction.RemoveFromFavorites(product.id, snackbarHostState) {
                            navController.navigate(ScreenRoutes.FAVORITE)
                        }
                    )
                } else {
                    onProductAction(
                        ProductAction.AddToFavorites(product.id, snackbarHostState) {
                            navController.navigate(ScreenRoutes.FAVORITE)
                        }
                    )
                }
            }
        },
        modifier = Modifier.size(sizeDp)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else {
                Icons.Outlined.FavoriteBorder
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(sizeDp)
        )
    }
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
    productViewModel: ProductViewModel,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val products by productViewModel.allProducts.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        productViewModel.loadMainProducts()
    }
    val bestSellerProducts = products.sortedBy { it.discountPercentage }
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
                products = products,
                onProductAction = onProductAction
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
                products = bestSellerProducts,
                onProductAction = onProductAction
            )
        }

        items(benefits) { benefit ->
            BenefitItem(benefit)
        }
    }
    when (productViewModel.state.response){
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> {
            ErrorScreen(onRefreshApiCall = productViewModel::loadMainProducts)
        }
        ServerResponse.UNAUTHORIZED -> {}
        ServerResponse.SUCCESS -> {}
    }
}
