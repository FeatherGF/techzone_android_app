package com.app.techzone.ui.theme.catalog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.app.techzone.data.remote.model.IPriceFilter
import com.app.techzone.data.remote.model.IProductFilter
import com.app.techzone.data.remote.model.PriceVariant
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.reusables.PriceRangeField
import com.app.techzone.utils.DEFAULT_MAX_PRICE
import com.app.techzone.utils.DEFAULT_MIN_PRICE
import com.app.techzone.utils.formatPrice
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt


@Composable
fun FiltersView(
    priceFilters: IPriceFilter?,
    productFilters: List<IProductFilter>,
    selectedPriceRanges: SnapshotStateList<PriceVariant>,
    mutableSelectedFilters: MutableStateFlow<MutableMap<String, MutableList<Any>>>,
    selectedFilters: MutableMap<String, MutableList<Any>>,
    onFiltersApplied: () -> Unit,
    clearFilters: () -> Unit,
    onBackClicked: () -> Unit
) {
    val minPrice: Int
    val maxPrice: Int
    if (priceFilters != null) {
        minPrice = priceFilters.min?.toDoubleOrNull()?.roundToInt() ?: DEFAULT_MIN_PRICE
        maxPrice = priceFilters.max?.toDoubleOrNull()?.roundToInt() ?: DEFAULT_MAX_PRICE
    } else {
        minPrice = DEFAULT_MIN_PRICE
        maxPrice = DEFAULT_MAX_PRICE
    }

    var lowerBoundPrice: Int
    var higherBoundPrice: Int
    if (selectedPriceRanges.isNotEmpty()) {
        lowerBoundPrice = remember {
            mutableIntStateOf(
                selectedPriceRanges.sortedWith(compareBy(nullsFirst()) { it.min }).first().min
                    ?: minPrice
            )
        }.intValue
        higherBoundPrice = remember {
            mutableIntStateOf(
                selectedPriceRanges.sortedWith(compareByDescending(nullsLast()) { it.max })
                    .first().max ?: maxPrice
            )
        }.intValue
    } else {
        lowerBoundPrice = remember { mutableIntStateOf(minPrice) }.intValue
        higherBoundPrice = remember { mutableIntStateOf(maxPrice) }.intValue
    }

    var sliderPosition by remember {
        mutableStateOf(lowerBoundPrice.toFloat()..higherBoundPrice.toFloat())
    }
    var textLowerBoundPrice by remember {
        mutableStateOf(lowerBoundPrice.takeIf { lowerBoundPrice != minPrice }?.toString() ?: "")
    }
    var textHigherBoundPrice by remember {
        mutableStateOf(higherBoundPrice.takeIf { higherBoundPrice != maxPrice }?.toString() ?: "")
    }
    val steps = (maxPrice - minPrice) / 5100

    BackHandler {
        onBackClicked()
        clearFilters()
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)) {
        Column {
            FiltersTopBar(clearFilters = clearFilters, onBackClicked = onBackClicked)
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = MaterialTheme.dimension.extendedMedium,
                        end = MaterialTheme.dimension.extendedMedium,
                        top = MaterialTheme.dimension.large,
                        bottom = MaterialTheme.dimension.extraLarge * 3.6f
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.large)
            ) {
                Column {
                    Text(
                        "Цена",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.dimension.extendedMedium),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small)
                    ) {
                        PriceRangeField(
                            Modifier.weight(0.45f),
                            placeholderText = "от ${formatPrice(minPrice)}",
                            text = textLowerBoundPrice,
                            onValueChange = {
                                textLowerBoundPrice = it
                                it.toIntOrNull()?.let { price ->
                                    if (price in 0..higherBoundPrice) {
                                        selectedPriceRanges.clear()
                                        sliderPosition = (
                                                textLowerBoundPrice.toFloatOrNull() ?: 0f
                                                )..(
                                                textHigherBoundPrice.toFloatOrNull()
                                                    ?: maxPrice.toFloat()
                                                )
                                        lowerBoundPrice = price
                                        selectedPriceRanges.add(
                                            PriceVariant(
                                                min = textLowerBoundPrice.toIntOrNull(),
                                                max = textHigherBoundPrice.toIntOrNull()
                                            )
                                        )
                                    }
                                }
                            }
                        )
                        PriceRangeField(
                            Modifier.weight(0.45f),
                            placeholderText = "до ${formatPrice(maxPrice)}",
                            text = textHigherBoundPrice,
                            onValueChange = {
                                textHigherBoundPrice = it
                                it.toIntOrNull()?.let { price ->
                                    if (price in lowerBoundPrice..maxPrice) {
                                        selectedPriceRanges.clear()
                                        sliderPosition = (
                                                textLowerBoundPrice.toFloatOrNull() ?: 0f
                                                )..(
                                                textHigherBoundPrice.toFloatOrNull()
                                                    ?: maxPrice.toFloat()
                                                )
                                        higherBoundPrice = price
                                        selectedPriceRanges.add(
                                            PriceVariant(
                                                min = textLowerBoundPrice.toIntOrNull(),
                                                max = textHigherBoundPrice.toIntOrNull()
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                    RangeSlider(
                        value = sliderPosition,
                        steps = steps,
                        valueRange = minPrice.toFloat()..maxPrice.toFloat(),
                        onValueChange = { range ->
                            if (minPrice != maxPrice)
                                sliderPosition = range

                            val rangeStart = range.start.roundToInt()
                            val rangeEnd = range.endInclusive.roundToInt()

                            selectedPriceRanges.clear()

                            // if value is default -> don't replace placeholder
                            textLowerBoundPrice = if (rangeStart in minPrice..maxPrice) {
                                selectedPriceRanges.add(
                                    PriceVariant(
                                        min = rangeStart, max = rangeEnd
                                    )
                                )
                                rangeStart.toString()
                            } else {
                                selectedPriceRanges.add(PriceVariant(min = null, max = rangeEnd))
                                ""
                            }
                            textHigherBoundPrice = if (rangeEnd in minPrice..maxPrice) {
                                selectedPriceRanges.add(
                                    PriceVariant(
                                        min = rangeStart, max = rangeEnd
                                    )
                                )
                                rangeEnd.toString()
                            } else {
                                selectedPriceRanges.add(PriceVariant(min = rangeStart, max = null))
                                ""
                            }
                        },
                    )
                    Column(
                        Modifier
                            .selectableGroup()
                            .background(MaterialTheme.colorScheme.tertiary)
                            .border(
                                width = 1.dp,
                                color = ForStroke.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        priceFilters?.let { prices ->
                            prices.variants.forEachIndexed { index, f ->
                                val filter = f as LinkedTreeMap<*, *>
                                val label = filter["label"].toString()
                                val min = filter["min"].toString().toDoubleOrNull()?.roundToInt()
                                val max = filter["max"].toString().toDoubleOrNull()?.roundToInt()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(MaterialTheme.dimension.huge)
                                        .selectable(
                                            selected = selectedPriceRanges.any { it.label == label },
                                            onClick = {
                                                selectedPriceRanges.removeIf { it.label.isEmpty() }
                                                val selectedPrice =
                                                    selectedPriceRanges.find { it.label == label }
                                                if (selectedPrice == null) selectedPriceRanges.add(
                                                    PriceVariant(label, min, max)
                                                )
                                                else selectedPriceRanges.remove(selectedPrice)
                                                val selectedMinimum = selectedPriceRanges
                                                    .sortedWith(compareBy(nullsFirst()) { it.min })
                                                    .firstOrNull()?.min
                                                val selectedMaximum = selectedPriceRanges
                                                    .sortedWith(compareByDescending(nullsLast()) { it.max })
                                                    .firstOrNull()?.max
                                                textLowerBoundPrice =
                                                    selectedMinimum?.toString() ?: ""
                                                textHigherBoundPrice =
                                                    selectedMaximum?.toString() ?: ""
                                                sliderPosition = (selectedMinimum
                                                    ?: minPrice).toFloat()..(selectedMaximum
                                                    ?: maxPrice).toFloat()
                                            },
                                            role = Role.Checkbox
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Checkbox(
                                        modifier = Modifier.padding(horizontal = MaterialTheme.dimension.larger),
                                        checked = selectedPriceRanges.any { it.label == label },
                                        onCheckedChange = null
                                    )
                                    Text(
                                        label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                    )
                                }

                                if (index != prices.variants.size - 1)
                                    HorizontalDivider(color = ForStroke)
                            }
                        }
                    }
                }

                // Other filters
                if (productFilters.isNotEmpty()) {
                    productFilters.forEach { filter ->
                        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.extendedMedium)) {
                            Text(
                                filter.label,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                            Column(
                                Modifier
                                    .selectableGroup()
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .border(
                                        width = 1.dp,
                                        color = ForStroke.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            ) {
                                filter.variants.forEachIndexed { index, value ->
                                    val filterValue = parseFilterValue(value)
                                    var selected by remember {
                                        mutableStateOf(
                                            selectedFilters
                                                .getOrDefault(filter.id, mutableListOf())
                                                .contains(filterValue)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(MaterialTheme.dimension.huge)
                                            .selectable(
                                                selected = selected, onClick = {
                                                    mutableSelectedFilters.update { mapping ->
                                                        val sf = mapping.getOrDefault(
                                                            filter.id, mutableListOf()
                                                        )
                                                        if (sf.contains(filterValue)) sf.remove(
                                                            filterValue
                                                        )
                                                        else sf.add(filterValue)
                                                        selected = !selected
                                                        mapping[filter.id] = sf
                                                        mapping
                                                    }
                                                }, role = Role.Checkbox
                                            ), verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            modifier = Modifier.padding(horizontal = MaterialTheme.dimension.larger),
                                            checked = selected,
                                            onCheckedChange = null
                                        )
                                        Text(
                                            filterValue.toString(),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                        )
                                    }

                                    if (index != filter.variants.size - 1)
                                        HorizontalDivider(color = ForStroke)
                                }
                            }
                        }
                    }
                }
            }
        }
        Surface(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.tertiary,
            shadowElevation = 12.dp,
            border = BorderStroke(width = 1.dp, color = ForStroke),
        ) {
            Column(
                Modifier.padding(MaterialTheme.dimension.extendedMedium),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        clearFilters()
                        onFiltersApplied()
                        onBackClicked()
                    }, border = null, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Сбросить фильтры")
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        onFiltersApplied()
                        onBackClicked()
                    }, border = null, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Применить")
                }
            }
        }
    }
}

@Composable
fun FiltersTopBar(
    clearFilters: () -> Unit,
    onBackClicked: () -> Unit,
) =
    Surface(
        modifier = Modifier
            .height(MaterialTheme.dimension.huge * 2)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiary,
        border = BorderStroke(width = 1.dp, color = ForStroke.copy(alpha = 0.1f)),
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.dimension.extendedMedium,
                    top = MaterialTheme.dimension.huge,
                    end = MaterialTheme.dimension.extendedMedium,
                    bottom = MaterialTheme.dimension.extendedMedium
                )
                .fillMaxWidth()
                .height(MaterialTheme.dimension.extraLarge),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                "Фильтры",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            )
            IconButton(onClick = {
                clearFilters()
                onBackClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
            }
        }
    }

fun parseFilterValue(value: Any): Any {
    val strValue = value.toString()
    strValue.toDoubleOrNull()?.let {
        if (it % 1.0 == 0.0) return it.roundToInt()
        return it
    }
    return strValue
}
