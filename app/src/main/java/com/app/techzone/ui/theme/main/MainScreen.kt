package com.app.techzone.ui.theme.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.techzone.R
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.model.Benefit
import com.app.techzone.model.benefits
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.reusables.ProductCarousel


@Composable
fun MainScreenRoot(
    productViewModel: ProductViewModel,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val newProducts = productViewModel.productPagingFlow.collectAsLazyPagingItems()
    val popularProducts = productViewModel.popularProductsPagingFlow.collectAsLazyPagingItems()
    MainScreen(
        popularProducts = popularProducts,
        newProducts = newProducts,
        onProductCheckStatus = onProductCheckStatus,
        onProductAction = onProductAction
    )
}


@Composable
private fun MainScreen(
    popularProducts: LazyPagingItems<BaseProduct>,
    newProducts: LazyPagingItems<BaseProduct>,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
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
                products = newProducts,
                onProductCheckStatus = onProductCheckStatus,
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
private fun BannerCarousel() {
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

@Composable
private fun BenefitItem(benefit: Benefit) {
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
        horizontalArrangement = Arrangement.spacedBy(
            44.dp,
            alignment = Alignment.CenterHorizontally
        ),

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
