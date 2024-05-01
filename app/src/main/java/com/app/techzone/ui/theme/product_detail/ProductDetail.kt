package com.app.techzone.ui.theme.product_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.data.remote.model.IDetailedProduct
import com.app.techzone.data.remote.model.Review
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.ui.theme.RoundBorder28
import com.app.techzone.ui.theme.main.ProductBuyButton
import com.app.techzone.ui.theme.main.ProductCarousel
import com.app.techzone.ui.theme.main.ProductCrossedPrice
import com.app.techzone.ui.theme.main.ProductFavoriteIcon
import com.app.techzone.ui.theme.main.ProductRating
import com.app.techzone.ui.theme.main.ProductReviewCount
import com.app.techzone.ui.theme.main.ProductViewModel
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice
import com.app.techzone.ui.theme.main.ProductImageOrPreview
import com.app.techzone.ui.theme.product_detail.characteristics.ICharacteristic
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.getProductCharacteristics


@Composable
fun ProductDetailScreen(
    productId: Int,
    navigateToDetail: (productId: Int) -> Unit,
    onBackClicked: () -> Unit,
    addToFavorite: (Int) -> Int,
    removeFromFavorite: (Int) -> Int,
) {
    val detailProductViewModel = hiltViewModel<ProductDetailViewModel>()
    val recommendations = hiltViewModel<ProductViewModel>()

    LaunchedEffect(detailProductViewModel) {
        detailProductViewModel.loadProduct(productId)
    }

    val product by detailProductViewModel.product.collectAsStateWithLifecycle()
    val recommendedProducts by recommendations.allProducts.collectAsStateWithLifecycle()
    val state = detailProductViewModel.state

    val lazyListState = rememberLazyListState()
    val isMainBuyButtonHidden = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 2
        }
    }
    BackHandler(onBack = onBackClicked)
    Column {
        Column(Modifier.weight(1f)) {
            product?.let {
                LazyColumn(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    state = lazyListState
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .background(color = MaterialTheme.colorScheme.tertiary)
                                .padding(end = 28.dp, top = 20.dp, bottom = 20.dp, start = 28.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = onBackClicked) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                )
                            }
                            ProductFavoriteIcon(
                                product = it,
                                addToFavorite = addToFavorite,
                                removeFromFavorite = removeFromFavorite
                            )
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ProductDetail(it)
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ReviewAndCharacteristicsTabs(it)
                        }
                    }
                    item {
                        Text(
                            "Вам может понравиться",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 40.dp,
                                end = 16.dp,
                                bottom = 12.dp
                            ),
                        )
                    }
                    item {
                        ProductCarousel(
                            navigateToDetail = navigateToDetail,
                            products = recommendedProducts.items,
                            addToFavorite = addToFavorite,
                            removeFromFavorite = removeFromFavorite
                        )
                    }
                }
            }
        }
        if (isMainBuyButtonHidden.value) {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = ForStroke.copy(alpha = 0.1f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentColor = MaterialTheme.colorScheme.tertiary,
            ) {
                ProductBuyButton()
            }
        }
    }

    when(state.response) {
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> {
            ErrorScreen(onRefreshApiCall = { detailProductViewModel.loadProduct(productId) })
        }
        // if response is successful all the code above will be rendered
        ServerResponse.SUCCESS -> {}
        ServerResponse.UNAUTHORIZED -> TODO()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImagesPager(product: IDetailedProduct) {
    val pageCount = product.photos.size.takeIf { it > 0 } ?: 1
    val pagerState = rememberPagerState(pageCount = { pageCount })
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(205.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(316.dp),
            pageSpacing = 8.dp,
        ) { index ->
            ProductImageOrPreview(
                product.photos,
                photoIndex = index,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RoundBorder28),
                filterQuality = FilterQuality.Medium
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductVariantsAndPrice(product: IDetailedProduct) {
    val mainTextColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
    val dimTextColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f)
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.tertiary, shape = RoundBorder28)
            .border(width = 1.dp, color = ForStroke.copy(alpha = 0.1f), shape = RoundBorder28)
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column {
                Text("Цвет:", style = MaterialTheme.typography.labelLarge, color = dimTextColor)
                val chips = mapOf(
                    "Blue Titanium" to mainTextColor,
                    "Gray" to Color.Companion.Gray,
                    "Black Titanium" to Color.Companion.Black,
                    "White" to Color.Companion.White,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    chips.forEach { (text, color) ->
                        var selected by rememberSaveable { mutableStateOf(false) }
                        FilterChip(
                            selected = selected,
                            onClick = { selected = !selected },
                            border = if (selected) BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            ) else BorderStroke(width = 1.dp, color = dimTextColor),
                            label = {
                                Text(
                                    text,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Companion.Black
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Circle,
                                    contentDescription = null,
                                    tint = color,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Встроенная память:",
                    style = MaterialTheme.typography.labelLarge,
                    color = dimTextColor
                )
                val memorySizes = listOf(
                    "128 ГБ",
                    "256 ГБ",
                    "512 ГБ",
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    memorySizes.forEach { size ->
                        var selected by remember { mutableStateOf(false) }
                        FilterChip(
                            modifier = Modifier
                                .height(32.dp)
                                .width(77.dp),
                            selected = selected,
                            onClick = { selected = !selected },
                            border = if (selected) BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            ) else BorderStroke(width = 1.dp, color = dimTextColor),
                            label = {
                                Text(
                                    size,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Companion.Black
                                )
                            }
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ProductCrossedPrice(product, large = true)
                    Text(
                        text = formatPrice(
                            calculateDiscount(
                                product.price,
                                product.discountPercentage
                            )
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
                if (product.discountPercentage > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Скидка",
                            style = MaterialTheme.typography.labelLarge,
                            color = mainTextColor
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .height(32.dp)
                                .width(52.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "-${product.discountPercentage}%",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
        ProductBuyButton()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAndCharacteristicsTabs(product: IDetailedProduct) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val titles = listOf(
        "Отзывы ${if (product.reviewsCount > 0) product.reviewsCount else ""}",
        "Характеристики"
    )
    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = false),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        },
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = {
                    val color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.scrim
                    Text(
                        text = title,
                        color = color,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        }
    }


    val (showFullInfo, onShowFullChange) = remember { mutableStateOf(false) }
    val (showAllReviews, onShowAllReviewsChange) = remember { mutableStateOf(false) }

    when (selectedTabIndex) {
        0 -> {
            ProductDetailReviews(
                product.reviews,
                showAllReviews = showAllReviews,
                onShowAllReviewsChange = { onShowAllReviewsChange(true) }
            )
        }

        1 -> {
            ProductCharacteristics(
                product,
                showFullInfo = showFullInfo,
                onShowFullChange = { onShowFullChange(true) }
            )
        }
    }

}


@Composable
fun Review(review: Review) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    review.user.ifBlank { "Неопознанный покупатель" },
                    style = textStyle,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (index in 0..4) {
                        val starIcon =
                            if (index < review.rating) Icons.Filled.Star else Icons.Outlined.StarOutline
                        Icon(
                            modifier = Modifier.size(17.dp),
                            imageVector = starIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Row(Modifier.padding(top = 4.dp)) {
                Text(
                    "1 марта 2024", // TODO: поставить обратно product.dateCreated, когда баг на беке пофиксится
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.scrim
                )
            }
        }

        Row {
            Text(
                review.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.scrim
            )
        }
    }
}


@Composable
fun ProductDetailReviews(
    reviews: List<Review>,
    showAllReviews: Boolean,
    onShowAllReviewsChange: () -> Unit
) {
    if (reviews.isEmpty()) {
        Text("Отзывов нет!", style = MaterialTheme.typography.labelLarge)
        return
    }

    Column {
        if (showAllReviews || reviews.size < 4) {
            reviews.forEach { reviewData: Review ->
                Review(reviewData)
            }
        } else {
            for (index in 0..2) {
                Review(reviews[index])
            }
            Button(
                onClick = onShowAllReviewsChange,
                shape = RoundBorder100,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Показать все",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Composable
fun ProductCharacteristics(
    product: IDetailedProduct,
    showFullInfo: Boolean,
    onShowFullChange: () -> Unit
) {
    val characteristics = getProductCharacteristics(product)
    Column {
        if (showFullInfo || characteristics.allInfo.size < 3) {
            characteristics.allInfo.forEach { characteristic: ICharacteristic ->
                Characteristic(characteristic)
            }
        } else {
            for (index in 0..1) {
                Characteristic(characteristics.allInfo[index])
            }
            Button(
                onClick = onShowFullChange,
                shape = RoundBorder100,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            ) {
                Text(
                    "Показать все",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Composable
fun Characteristic(characteristic: ICharacteristic) {
    Text(
        modifier = Modifier.padding(top = 28.dp),
        text = characteristic.label,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
    )
    Column {
        characteristic.items.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.scrim
                )
                TextSplitter(
                    color = MaterialTheme.colorScheme.scrim,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .align(Alignment.Bottom),
                )
                Text(
                    value.takeIf { !it.isNullOrEmpty() } ?: "-",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
            }
        }
    }
}


@Composable
fun ProductDetail(product: IDetailedProduct) {
    val mainTextColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
    val dimTextColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f)
    ProductImagesPager(product)
    Text(
        product.name,
        style = MaterialTheme.typography.titleLarge,
        color = mainTextColor,
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Код товара:",
                style = MaterialTheme.typography.labelLarge,
                color = dimTextColor
            )
            Text(
                product.id.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = mainTextColor
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProductRating(
                product,
                textStyle = MaterialTheme.typography.labelLarge,
                isStarFilled = true
            )
            ProductReviewCount(product, textStyle = MaterialTheme.typography.labelLarge)
        }
    }
    ProductVariantsAndPrice(product)
}
