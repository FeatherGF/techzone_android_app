package com.app.techzone.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.app.techzone.SearchWidgetState
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder24
import kotlin.math.roundToInt

enum class CatalogScreenEnum {
    DEFAULT,
    FILTERS,
    SORTING
}


data class PricePreset (
//    val id: Int?,
    val text: String,
    val minPrice: Int,
    val maxPrice: Int,
//    var isChecked: Boolean = false
)

data class Sorting (
    val text: String,
    val queryName: String, // price_desc, price_asc, popular etc
)

class CatalogViewModel : ViewModel() {
    var activeScreenState by mutableStateOf(CatalogScreenEnum.DEFAULT)

    fun updateActiveState(newValue: CatalogScreenEnum){
        activeScreenState = newValue
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersAndSorting(onChangeStateView: (CatalogScreenEnum) -> Unit) {
    var showSortingSheet by remember { mutableStateOf(false)}
    val sheetState = rememberModalBottomSheetState()

    // queryName нужно согласовать с беком
    val sortingOptions = listOf(
        Sorting("По популярности", queryName = "popular"),
        Sorting("Сначала недорогие", queryName = "price_asc"),
        Sorting("Сначала дорогие", queryName = "price_desc"),
        Sorting("По размеру скидки", queryName = "sale_amount"),
        Sorting("Высокий рейтинг", queryName = "high_rating"),
    )
    val (selectedSorting, onSortingSelected) = remember { mutableStateOf(sortingOptions[0])}
    if (showSortingSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSortingSheet = false },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 36.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Text(
                    "Сортировка",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
                Column(
                    Modifier
                        .selectableGroup()
                        .border(
                            width = 1.dp,
                            color = ForStroke.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    sortingOptions.forEachIndexed { index, sortingOption ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = selectedSorting.text == sortingOption.text,
                                    onClick = { onSortingSelected(sortingOption) },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                modifier = Modifier.padding(start = 34.dp, end = 34.dp),
                                checked = selectedSorting.text == sortingOption.text,
                                onCheckedChange = null
                            )
                            Text(
                                sortingOption.text,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                        }
                        // don't render divider after last, because border will do it
                        if (index != sortingOptions.size - 1) {
                            HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
                        }
                    }
                }

            }
        }
    }
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(40.dp)
        ,
        horizontalArrangement = Arrangement.spacedBy(
            110.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable {
//                    onChangeStateView(CatalogScreenEnum.SORTING)
                    showSortingSheet = true
                },
            contentAlignment = Alignment.Center

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(18.dp),
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    "По популярности",  // TODO: сделать стейт сортировки для отображения
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable { onChangeStateView(CatalogScreenEnum.FILTERS) },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .width(115.dp)
                    .padding(top = 11.dp, bottom = 11.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Outlined.FilterAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Фильтры",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

const val MAX_PRICE = 250_000  // TODO: получать значения с бека
const val MIN_PRICE = 5_000
val UNSELECTED_PRICING = PricePreset("", 0, 0)

@Composable
fun FiltersView(onChangeStateView: (CatalogScreenEnum) -> Unit) {
    fun onBackClicked() {
        onChangeStateView(CatalogScreenEnum.DEFAULT)
    }
    BackHandler(onBack = ::onBackClicked)
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ){
        Surface(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiary,
            border = BorderStroke(width = 1.dp, color = ForStroke.copy(alpha = 0.1f)),
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 56.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary // invisible only for simplicity of development
                )
                Text(
                "Фильтры",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
                IconButton(onClick = ::onBackClicked) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(start = 16.dp, top = 28.dp , end = 16.dp)) {
            Text(
                "Цена",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            )
            var lowerBoundPrice by rememberSaveable { mutableIntStateOf(MIN_PRICE) }
            var higherBoundPrice by rememberSaveable { mutableIntStateOf(MAX_PRICE) }
            var sliderPosition by remember {
                mutableStateOf(lowerBoundPrice.toFloat()..higherBoundPrice.toFloat())
            }
            var textLowerBoundPrice by remember { mutableStateOf("")}
            var textHigherBoundPrice by remember { mutableStateOf("")}

            // approximately 50 steps with the 5 000 rubles between each step
            val steps = (MAX_PRICE - MIN_PRICE) / 5100

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PriceRangeField(
                    placeholderText = "от 5 000 ₽",
                    text = textLowerBoundPrice,
                    onValueChange = {
                        textLowerBoundPrice = it
                        it.toIntOrNull()?.let { price ->
                            if (price in 0..MAX_PRICE){
                                lowerBoundPrice = price
                                sliderPosition = lowerBoundPrice.toFloat() ..higherBoundPrice.toFloat()
                            }
                        }
                    }
                )
                PriceRangeField(
                    placeholderText = "до 250 000 ₽",
                    text = textHigherBoundPrice,
                    onValueChange = {
                        textHigherBoundPrice = it
                        it.toIntOrNull()?.let { price ->
                            if (price in lowerBoundPrice..MAX_PRICE){
                                higherBoundPrice = price
                                sliderPosition = lowerBoundPrice.toFloat() ..higherBoundPrice.toFloat()
                            }
                        }
                    }
                )
            }

            RangeSlider(
                value = sliderPosition,
                steps = steps,
                valueRange = MIN_PRICE.toFloat()..MAX_PRICE.toFloat(),
                onValueChange = { range ->
                    sliderPosition = range

                    val rangeStart = range.start.roundToInt()
                    val rangeEnd = range.endInclusive.roundToInt()

                    // if value is default -> don't replace placeholder
                    textLowerBoundPrice = if (rangeStart != MIN_PRICE) rangeStart.toString() else ""
                    textHigherBoundPrice = if (rangeEnd != MAX_PRICE) rangeEnd.toString() else ""
                },
            )

            val pricePresets = listOf(
                PricePreset("Менее 15 000 ₽", MIN_PRICE, 14_999),
                PricePreset("15 000 - 24 999 ₽", 15_000, 24_999),
                PricePreset("25 000 - 39 999 ₽", 25_000, 39_999),
                PricePreset("40 000 - 59 999 ₽", 40_000, 59_999),
                PricePreset("60 000 - 99 999 ₽", 60_000, 99_999),
                PricePreset("Более 99 999 ₽", 100_000, MAX_PRICE),
            )

            val (selectedPricing, onPricingSelected) = remember { mutableStateOf(UNSELECTED_PRICING) }

            Column(
                Modifier
                    .selectableGroup()
                    .border(
                        width = 1.dp,
                        color = ForStroke.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                pricePresets.forEachIndexed { index, pricePreset ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = selectedPricing.text == pricePreset.text,
                                onClick = {
                                    // if the same is
                                    if (selectedPricing.text != pricePreset.text){
                                        onPricingSelected(pricePreset)
                                    } else {
                                        onPricingSelected(UNSELECTED_PRICING)
                                    }
                                },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            modifier = Modifier.padding(start = 34.dp, end = 34.dp),
                            checked = selectedPricing.text == pricePreset.text,
                            onCheckedChange = null
                        )
                        Text(
                            pricePreset.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                        )
                    }
                    if (index != pricePresets.size - 1) {
                        HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
                    }
                }
            }

//            LazyColumn (
//                modifier = Modifier
//                    .padding(top = 16.dp)
//                    .fillMaxWidth()
//                    .background(color = MaterialTheme.colorScheme.tertiary)
//                    .border(
//                        width = 1.dp,
//                        color = ForStroke.copy(alpha = 0.1f),
//                        shape = RoundedCornerShape(4.dp)
//                    )
//            ) {
//                items(
//                    count = pricePresets.size,
//                    key = {index -> pricePresets[index].id}
//                ) { index ->
//                    val currentPreset = pricePresets[index]
//                    var isCheckedState by remember { mutableStateOf(currentPreset.isChecked) }
//                    // Можно выбрать много пресетов цен. Учитываться будет промежуток он самого
//                    // минимального до максимального выбранных пресетов.
//                    Row (
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(56.dp)
//                            .clickable {
//                                isCheckedState = !isCheckedState
//                            },
//                        verticalAlignment = Alignment.CenterVertically,
//
//                    ) {
//                        Checkbox(
//                            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
//                            checked = isCheckedState,
//                            onCheckedChange = null
//                        )
//                        Text(
//                            currentPreset.text,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
//                        )
//                    }
//                    if (index != pricePresets.size - 1) {
//                        HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
//                    }
//                }
//            }

            // TODO: append when backend will be hosted
        }


    }
}

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









