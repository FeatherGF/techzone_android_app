package com.app.techzone.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.techzone.Header
import com.app.techzone.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


data class ProductCard(
    val imageId: Int,
    val title: String,
    val price: Int,
    val crossedPrice: Int? = null,
    val reviewCount: Int? = null,
    val rating: Float? = null,
    val isInCart: Boolean = false,
    val isFavorite: Boolean = false,
)


fun formatPrice(price: Int): String {
    val dec = DecimalFormat("###,###,###,###,### ₽", DecimalFormatSymbols(Locale.ENGLISH))
    return dec.format(price).replace(",", " ")
}

fun formatReview(reviewCount: Int): String {
    when (reviewCount.mod(100)) {
        11, 12, 13, 14 -> { return "$reviewCount отзывов"}
    }

    val review: String = when (reviewCount.mod(10)) {
        1 -> {"отзыв"}
        2, 3, 4 -> {"отзыва"}
        else -> {"отзывов"}
    }
    return "$reviewCount $review"
}


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
            .padding(start = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .height(205.dp)
            .background(
                shape = RoundedCornerShape(CornerSize(28.dp)),
                color = MaterialTheme.colorScheme.tertiary
            )
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
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


val newProducts = listOf(
    ProductCard(
        R.drawable.ic_iphone,
        "Смартфон Apple iPhone 15 Pro 256GB Blue Titanium",
        123999,
        154999,
        191,
        4.75f,
        true,
    ),
    ProductCard(
        R.drawable.ic_tv,
        "Телевизор Samsung Ultra HD (4K) LED 55",
        77399,
        85999,
        25,
        5.0f,
        isFavorite = true
    ),
    ProductCard(
        R.drawable.ic_macbook,
        "Ноутбук Apple MacBook Air 13 M1/8/256GB Silver (MGN93)",
        95999,
        reviewCount = 114,
        rating = 4.8f,
        isInCart = true
    ),
    ProductCard(
        R.drawable.ic_gaming_laptop,
        "Ноутбук игровой MSI Katana 17 B11UCX-882XRUё",
        89999,
        reviewCount = 22,
        rating = 5.0f
    ),
)

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ProductCarousel(products: List<ProductCard> = newProducts) {
    val pagerState = rememberPagerState(pageCount = { products.size })
    Box(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            key = { products[it].imageId },
            pageSize = PageSize.Fixed(154.dp),
            pageSpacing = 8.dp,
        ) { index ->
            val product = products[index]
            var titleBoxHeight = 64.dp
            var isInCartState by remember { mutableStateOf(product.isInCart) }
            var isFavoriteState by remember { mutableStateOf(product.isFavorite) }

            Box(
                modifier = Modifier
                    .height(336.dp)
                    .width(154.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(CornerSize(24.dp))
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 14.dp, top = 12.dp, end = 14.dp, bottom = 17.dp
                        )
                        .width(128.dp), verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier.padding(),
                        painter = painterResource(id = product.imageId),
                        contentDescription = product.title
                    )
                    Column(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 9.dp)
                            .height(114.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                product.crossedPrice?.let {
                                    Text(
                                        text = formatPrice(it),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.scrim,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }
                                titleBoxHeight = 48.dp
                                Text(
                                    text = formatPrice(product.price),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Icon(imageVector = if (isFavoriteState) Icons.Filled.Favorite else {
                                Icons.Outlined.FavoriteBorder
                            },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .height(24.dp)
                                    .height(28.dp)
                                    .clickable {
                                        isFavoriteState = !isFavoriteState
                                        // TODO: add to favorite here
                                    })
                        }
                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .height(titleBoxHeight)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            product.rating?.let {
                                Row {
                                    Text(
                                        text = it.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Icon(
                                        imageVector = Icons.Outlined.StarOutline,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .height(14.dp)
                                            .width(14.dp)
                                    )
                                }
                            }
                            product.reviewCount?.let {
                                Text(
                                    text = formatReview(it), // TODO: сделать настройку падежа для "Отзывов"
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.scrim,
                                )
                            }
                        }
                    }
                    if (isInCartState) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(CornerSize(100.dp)),
                                )
                                .clickable {
                                    isInCartState = false
                                }, contentAlignment = Alignment.Center
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
                                    shape = RoundedCornerShape(CornerSize(100.dp))
                                )
                                .clickable {
                                    isInCartState = true
                                    // TODO: add to cart here
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "В корзину",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    LazyColumn {
        item { Header() }
        item { BannerCarousel() }
        item {
            Text(
                text = "Новинки",
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, end = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        item { ProductCarousel(products = newProducts) }
    }
}
