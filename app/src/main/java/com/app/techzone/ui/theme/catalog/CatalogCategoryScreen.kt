package com.app.techzone.ui.theme.catalog

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.model.PricePreset
import com.app.techzone.model.ProductCard
import com.app.techzone.ui.theme.app_bars.SearchWidgetState
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder24
import com.app.techzone.ui.theme.main.ProductBuyButton
import com.app.techzone.ui.theme.main.ProductCrossedPrice
import com.app.techzone.ui.theme.main.ProductFavoriteIcon
import com.app.techzone.ui.theme.main.ProductRating
import com.app.techzone.ui.theme.main.ProductReviewCount
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice
import com.app.techzone.ui.theme.main.ProductImageOrPreview
import com.app.techzone.utils.CurrencyVisualTransformation

enum class CatalogScreenEnum {
    DEFAULT,
    FILTERS,
    SORTING
}


const val MAX_PRICE = 250_000  // TODO: получать значения с бека
const val MIN_PRICE = 5_000
val UNSELECTED_PRICING = PricePreset("", 0, 0)


val priceMask = CurrencyVisualTransformation("RUB")
@Composable
fun PriceRangeField(placeholderText: String, text: String, onValueChange: (String) -> Unit) {

    val fieldShape = RoundedCornerShape(4.dp)
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = fieldShape,
        singleLine = true,
        visualTransformation = priceMask,
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
    category: String,  // ApiConstant.Endpoints strings
    navigateToDetail: (productId: Int) -> Unit,
    activeScreenState: CatalogScreenEnum,
    onChangeView: (SearchWidgetState) -> Unit,
    onChangeStateView: (CatalogScreenEnum) -> Unit,
    addToFavorite: (ProductCard) -> Unit,
    removeFromFavorite: (ProductCard) -> Unit,
) {
    val catalogViewModel = hiltViewModel<CatalogViewModel>()
    LaunchedEffect(catalogViewModel){
        catalogViewModel.loadByCategory(category)
    }
    val products by catalogViewModel.products.collectAsStateWithLifecycle()
    when (activeScreenState) {
        CatalogScreenEnum.DEFAULT -> {
            onChangeView(SearchWidgetState.CATALOG_OPENED)
            DefaultCatalogView(
                products = products.items,
                navigateToDetail = navigateToDetail,
                onChangeStateView = onChangeStateView,
                addToFavorite = addToFavorite,
                removeFromFavorite = removeFromFavorite,
            )
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
fun DefaultCatalogView(
    products: List<BaseProduct>,
    navigateToDetail: (productId: Int) -> Unit,
    onChangeStateView: (CatalogScreenEnum) -> Unit,
    addToFavorite: (ProductCard) -> Unit,
    removeFromFavorite: (ProductCard) -> Unit,
) {
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
                count = products.size,
                key = { index -> products[index].id }
            ) {index ->
                val product = products[index]
                Surface(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .clickable { navigateToDetail(product.id) },
                    shape = RoundBorder24,
                    color = MaterialTheme.colorScheme.tertiary,
                    border = BorderStroke(width = 1.dp, color = ForStroke.copy(alpha = 0.1f)),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductImageOrPreview(product.photos, modifier = Modifier.size(110.dp))
                            Column (
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    product.name,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ProductRating(product = product, textStyle = MaterialTheme.typography.labelLarge)
                                    ProductReviewCount(product = product, textStyle = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                        Row (
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column{
                                ProductCrossedPrice(product = product)
                                Text(
                                    formatPrice(
                                        calculateDiscount(
                                            product.price,
                                            product.discountPercentage
                                        )
                                    ),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                )
                            }
                            Row (
                                modifier = Modifier.width(190.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                ProductFavoriteIcon(product = product, sizeDp = 32.dp)
                                ProductBuyButton()
                            }
                        }
                    }
                }
            }
        }
    }
}









