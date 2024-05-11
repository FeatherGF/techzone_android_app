package com.app.techzone.ui.theme.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.model.PricePreset
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
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.CurrencyVisualTransformation

enum class CatalogScreenEnum {
    DEFAULT,
    FILTERS,
}


const val MAX_PRICE = 250_000  // TODO: получать значения с бека
const val MIN_PRICE = 5_000
val UNSELECTED_PRICING = PricePreset("", 0, 0)


val priceMask = CurrencyVisualTransformation("RUB")
@Composable
fun PriceRangeField(placeholderText: String, text: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = RoundedCornerShape(4.dp),
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
    catalogViewModel: CatalogViewModel,
    onChangeView: (SearchWidgetState) -> Unit,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    when (catalogViewModel.activeScreenState) {
        CatalogScreenEnum.DEFAULT -> {
            onChangeView(SearchWidgetState.CATALOG_OPENED)
            DefaultCatalogView(
                category = category,
                catalogViewModel = catalogViewModel,
                onProductAction = onProductAction
            )
        }
        CatalogScreenEnum.FILTERS -> {
            onChangeView(SearchWidgetState.HIDDEN)
            FiltersView {
                catalogViewModel.updateActiveState(CatalogScreenEnum.DEFAULT)
            }
        }
    }
}


@Composable
fun DefaultCatalogView(
    category: String,
    catalogViewModel: CatalogViewModel,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    LaunchedEffect(Unit){
        catalogViewModel.loadByCategory(category)
    }
    val products by catalogViewModel.products.collectAsState()
    val state = catalogViewModel.state

    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        FiltersAndSorting {
            catalogViewModel.updateActiveState(CatalogScreenEnum.FILTERS)
        }
        LazyProductCards(
            products = products,
            onProductAction = onProductAction
        )
    }
    when(state.response) {
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> {
            ErrorScreen {
                catalogViewModel.loadByCategory(category)
            }
        }
        // if response is successful all the code above will be rendered
        ServerResponse.SUCCESS -> {}
        ServerResponse.UNAUTHORIZED -> {}
    }
}


@Composable
fun LazyProductCards(
    products: List<BaseProduct>,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val navController = LocalNavController.current
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
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                onClick = { navController.navigate("${ScreenRoutes.CATALOG}/${product.id}") },
                shape = RoundBorder24,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
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
                            ProductCrossedPrice(product = product, large = true)
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
                            ProductFavoriteIcon(
                                product = product,
                                onProductAction = onProductAction,
                                sizeDp = 32.dp
                            )
                            ProductBuyButton(product = product, onProductAction = onProductAction)
                        }
                    }
                }
            }
        }
    }
}
