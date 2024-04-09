package com.app.techzone.ui.theme.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.techzone.model.PricePreset
import com.app.techzone.ui.theme.app_bars.SearchWidgetState
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.main.ProductBuyButton
import com.app.techzone.ui.theme.main.ProductCrossedPrice
import com.app.techzone.ui.theme.main.ProductFavoriteIcon
import com.app.techzone.ui.theme.main.ProductRating
import com.app.techzone.ui.theme.main.ProductReviewCount
import com.app.techzone.ui.theme.main.formatPrice
import com.app.techzone.ui.theme.main.newProducts

enum class CatalogScreenEnum {
    DEFAULT,
    FILTERS,
    SORTING
}


const val MAX_PRICE = 250_000  // TODO: получать значения с бека
const val MIN_PRICE = 5_000
val UNSELECTED_PRICING = PricePreset("", 0, 0)

@Composable
fun PriceRangeField(placeholderText: String, text: String, onValueChange: (String) -> Unit) {
    val fieldShape = RoundedCornerShape(4.dp)
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = fieldShape,
        singleLine = true,
        placeholder = {
            Text(
                placeholderText,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            )
        },
        modifier = Modifier
            .width(190.dp) // maybe make it dynamic according to screen size
            .height(56.dp),
    )
}

@Composable
fun CatalogCategoryScreen(
    category: String = "Телевизоры",
    activeScreenState: CatalogScreenEnum,
    onChangeView: (SearchWidgetState) -> Unit,
    onChangeStateView: (CatalogScreenEnum) -> Unit,
) {
    when (activeScreenState) {
        CatalogScreenEnum.DEFAULT -> {
            onChangeView(SearchWidgetState.CATALOG_OPENED)
            DefaultCatalogView(onChangeStateView = onChangeStateView)
        }
        CatalogScreenEnum.FILTERS -> {
            onChangeView(SearchWidgetState.HIDDEN)
            FiltersView(onChangeStateView = onChangeStateView)
        }
        CatalogScreenEnum.SORTING -> {
            onChangeView(SearchWidgetState.HIDDEN)
        }
    }
}


@Composable
fun DefaultCatalogView(onChangeStateView: (CatalogScreenEnum) -> Unit) {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        FiltersAndSorting(onChangeStateView = onChangeStateView)
        LazyColumn(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundBorder24
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = newProducts.size,
                key = { index -> newProducts[index].imageId }
            ) {index ->
                val product = newProducts[index]
                Surface(
                    modifier = Modifier
                        .height(202.dp)
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundBorder24,
                    color = MaterialTheme.colorScheme.tertiary,
                    border = BorderStroke(width = 1.dp, color = ForStroke.copy(alpha = 0.1f)),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp), // inside the container
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = product.imageId),
                                contentDescription = null,
                                modifier = Modifier.size(110.dp)
                            )
                            Column (
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    product.title,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ProductRating(product = product, textStyle = MaterialTheme.typography.labelLarge)
                                    ProductReviewCount(product = product, textStyle = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                        val priceLineHeight = if (product.crossedPrice != null) 50.dp else 40.dp
                        Row (
                            modifier = Modifier
                                .height(priceLineHeight)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column{
                                ProductCrossedPrice(product = product)
                                Text(
                                    formatPrice(product.price),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                )
                            }
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ProductFavoriteIcon(product = product, sizeDp = 32.dp)
                                ProductBuyButton(product = product)
                            }
                        }
                    }
                }
            }
        }
    }
}









