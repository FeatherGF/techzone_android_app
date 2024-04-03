package com.app.techzone.screens

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.VerifiedUser
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.techzone.R
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.ui.theme.RoundBorder24
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


data class ProductCard(
    @SerializedName("image_id") val imageId: Int,
    val title: String,
    val price: Int,
    @SerializedName("crossed_price") val crossedPrice: Int? = null,
    @SerializedName("review_count") val reviewCount: Int? = null,
    val rating: Float? = null,
    val isInCart: Boolean = false,
    val isFavorite: Boolean = false,
)

data class Benefit(val imageId: ImageVector, val text: String)

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
        "Ноутбук игровой MSI Katana 17 B11UCX-882XRU",
        89999,
        reviewCount = 22,
        rating = 5.0f
    ),
)

val bestSellerProducts = listOf(
    ProductCard(
        R.drawable.ic_sale,
        "Телевизор Haier 55 Smart TV AX Pro",
        price = 64_999,
        rating = 4.9f,
        reviewCount = 13,
    ),
    ProductCard(
        R.drawable.ic_sale_2,
        "Планшет Apple iPad 10.2 Wi-Fi 64GB Space Grey (MK2K3)",
        price = 37_999,
        crossedPrice = 39_999,
        rating = 4.9f,
        reviewCount = 156,
    ),
    ProductCard(
        R.drawable.ic_sale_3,
        "Смартфон Samsung Galaxy A54 128GB Awesome Graphite",
        price = 34_999,
        crossedPrice = 45_999,
        rating = 4.8f,
        reviewCount = 73,
    ),
    ProductCard(
        R.drawable.ic_sale_4,
        "Смарт-часы Xiaomi M2216W1 Ivory",
        price = 7_999,
        crossedPrice = 9_999,
        rating = 4.75f,
        reviewCount = 25,
    ),
)

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ProductCarousel(products: List<ProductCard> = bestSellerProducts) {
    val pagerState = rememberPagerState(pageCount = { products.size })
    Box(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, bottom = 24.dp)
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

            Box(
                modifier = Modifier
                    .height(336.dp)
                    .width(154.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundBorder24
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.1f),
                        shape = RoundBorder24
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 14.dp, top = 12.dp, end = 14.dp, bottom = 17.dp
                        )
                        .width(128.dp),
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
                                .padding(top = 12.dp)
                                .height(30.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                ProductCrossedPrice(product = product)
                                titleBoxHeight = 48.dp
                                Text(
                                    text = formatPrice(product.price),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                )
                            }
                            ProductFavoriteIcon(product = product)
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
fun ProductBuyButton(product: ProductCard) {
    var isInCartState by remember { mutableStateOf(product.isInCart)}
    if (isInCartState) {
        Box(
            modifier = Modifier
                .width(128.dp)
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
                .width(128.dp)
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
fun ProductCrossedPrice(product: ProductCard) {
    product.crossedPrice?.let {
        Text(
            text = formatPrice(it),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.scrim,
            textDecoration = TextDecoration.LineThrough
        )
    }
}


@Composable
fun ProductRating(
    product: ProductCard,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    product.rating?.let {
        Row {
            Text(
                text = it.toString(),
                style = textStyle,
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
}


@Composable
fun ProductReviewCount(
    product: ProductCard,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    product.reviewCount?.let {
        Text(
            text = formatReview(it),
            style = textStyle,
            color = Color.Companion.Black.copy(alpha = 0.5f),
        )
    }
}


@Composable
fun ProductFavoriteIcon(product: ProductCard, sizeDp: Dp = 24.dp) {
    var isFavoriteState by remember { mutableStateOf(product.isFavorite) }
    Icon(
        imageVector = if (isFavoriteState) Icons.Filled.Favorite else {
            Icons.Outlined.FavoriteBorder
        },
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(sizeDp)
            .clickable {
                isFavoriteState = !isFavoriteState
                // TODO: add to favorite here
            }
    )

}


val benefits = listOf(
    Benefit(Icons.Outlined.Percent, "Скидки до -50% на весь ассортимент"),
    Benefit(Icons.Outlined.ChangeCircle, "30 дней на обмен или возврат товара"),
    Benefit(Icons.Outlined.VerifiedUser, "Гарантия качества и страхование техники"),
)


@Composable
@Preview
fun BenefitItem(benefit: Benefit = benefits[0]) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(140.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundBorder24
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        item { BannerCarousel() }

        item {
            Text(
                text = "Новинки",
                modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item { ProductCarousel(products = newProducts) }

        item {
            Text(
                text = "Хиты продаж",
                modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item { ProductCarousel(products = bestSellerProducts) }

        items(benefits) { benefit ->
            BenefitItem(benefit)
        }
    }
}
