package com.app.techzone.ui.theme.catalog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.app.techzone.model.pricePresets
import com.app.techzone.ui.theme.ForStroke
import kotlin.math.roundToInt


@Composable
fun FiltersView(onBackClicked: () -> Unit) {
    BackHandler(onBack = onBackClicked)
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
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
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(start = 16.dp, top = 28.dp, end = 16.dp)) {
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
            var textLowerBoundPrice by remember { mutableStateOf("") }
            var textHigherBoundPrice by remember { mutableStateOf("") }

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
                            if (price in 0..MAX_PRICE) {
                                lowerBoundPrice = price
                                sliderPosition =
                                    lowerBoundPrice.toFloat()..higherBoundPrice.toFloat()
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
                            if (price in lowerBoundPrice..MAX_PRICE) {
                                higherBoundPrice = price
                                sliderPosition =
                                    lowerBoundPrice.toFloat()..higherBoundPrice.toFloat()
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

            val (selectedPricing, onPricingSelected) = remember { mutableStateOf(UNSELECTED_PRICING) }

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
                pricePresets.forEachIndexed { index, pricePreset ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = selectedPricing.text == pricePreset.text,
                                onClick = {
                                    // if the same is
                                    if (selectedPricing.text != pricePreset.text) {
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

            // TODO: append when backend will be hosted
        }

    }
}