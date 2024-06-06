package com.app.techzone.ui.theme.main

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.Banner
import com.app.techzone.data.remote.model.PagingBaseProduct
import com.app.techzone.model.Benefit
import com.app.techzone.model.benefits
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.reusables.ProductCarousel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MainScreenRoot(
    productViewModel: ProductViewModel,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val newProducts = productViewModel.productPagingFlow.collectAsLazyPagingItems()
    val popularProducts = productViewModel.popularProductsPagingFlow.collectAsLazyPagingItems()
    val banners by productViewModel.banners.collectAsStateWithLifecycle()
    MainScreen(
        popularProducts = popularProducts,
        newProducts = newProducts,
        banners = banners,
        onProductCheckStatus = onProductCheckStatus,
        onProductAction = onProductAction
    )
}


@Composable
private fun MainScreen(
    popularProducts: LazyPagingItems<PagingBaseProduct>,
    newProducts: LazyPagingItems<PagingBaseProduct>,
    banners: List<Banner>,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        item { BannerCarousel(banners) }

        item {
            Text(
                text = "Новинки",
                modifier = Modifier.padding(
                    start = MaterialTheme.dimension.mediumLarge,
                    top = MaterialTheme.dimension.large
                ),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            ProductCarousel(
                products = newProducts,
                onProductCheckStatus = onProductCheckStatus,
                onProductAction = onProductAction
            )
        }

        item {
            Text(
                text = "Хиты продаж",
                modifier = Modifier.padding(
                    start = MaterialTheme.dimension.mediumLarge,
                    top = MaterialTheme.dimension.large
                ),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            ProductCarousel(
                products = popularProducts,
                onProductCheckStatus = onProductCheckStatus,
                onProductAction = onProductAction
            )
        }

        items(benefits) { benefit ->
            BenefitItem(benefit)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BannerCarousel(banners: List<Banner>) {
    val navController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()
    val pageState = rememberPagerState(pageCount = { banners.size })
    val isDragged by pageState.interactionSource.collectIsDraggedAsState()
    if (!isDragged) {
        LaunchedEffect(pageState.pageCount) {
            repeat(Int.MAX_VALUE) {
                delay(3000)
                pageState.animateScrollToPage(
                    page = when {
                        pageState.currentPage == banners.lastIndex -> 0
                        else -> pageState.currentPage.inc()
                    },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }
    }
    val padding = MaterialTheme.dimension.extendedMedium
    Box(
        modifier = Modifier
            .padding(start = padding, top = padding, end = padding)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pageState,
            key = { it },
            pageSize = PageSize.Fill,
            pageSpacing = MaterialTheme.dimension.small,
        ) { index ->
            if (banners.isNotEmpty()) {
                val banner = banners[index]
                SubcomposeAsyncImage(
                    model = banner.link,
                    contentDescription = "product banner $index",
                    filterQuality = FilterQuality.Medium,
                    contentScale = ContentScale.FillWidth,
                    loading = { CircularProgressIndicator() },
                    modifier = Modifier
                        .clip(RoundBorder24)
                        .clickable {
                            navController.navigate(
                                "${ScreenRoutes.PRODUCT_DETAIL}/${banner.productId}"
                            )
                        }
                        // start slider again when hitting end or
                        // go to end when swiping at the beginning
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                change.consume()
                                when {
                                    dragAmount < 0 -> {
                                        coroutineScope.launch { /* right */
                                            if (pageState.currentPage == banners.lastIndex) {
                                                pageState.animateScrollToPage(0)
                                            } else {
                                                pageState.animateScrollToPage(pageState.currentPage + 1)
                                            }
                                        }
                                    }

                                    dragAmount > 0 -> { /* left */
                                        coroutineScope.launch {
                                            if (pageState.currentPage == 0) {
                                                pageState.animateScrollToPage(banners.lastIndex)
                                            } else {
                                                pageState.animateScrollToPage(pageState.currentPage - 1)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun BenefitItem(benefit: Benefit) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.dimension.extendedMedium,
                vertical = MaterialTheme.dimension.small,
            )
            .fillMaxWidth()
            .height(MaterialTheme.dimension.huge * 2)
            .background(
                color = MaterialTheme.colorScheme.tertiary, shape = RoundBorder24
            )
            .padding(start = MaterialTheme.dimension.extraLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimension.extraLarge,
            alignment = Alignment.CenterHorizontally
        ),
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimension.huge)
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
