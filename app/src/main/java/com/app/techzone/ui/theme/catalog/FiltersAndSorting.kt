package com.app.techzone.ui.theme.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.app.techzone.model.sortingOptions
import com.app.techzone.ui.theme.ForStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersAndSorting(onChangeStateView: (CatalogScreenEnum) -> Unit) {
    var showSortingSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val (selectedSorting, onSortingSelected) = remember { mutableStateOf(sortingOptions[0]) }
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
            ) {
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
                                    onClick = {
                                        onSortingSelected(sortingOption)
                                        showSortingSheet = false
                                    },
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
    val buttonsContentPadding = PaddingValues(start = 12.dp, end = 16.dp)
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { showSortingSheet = true },
            border = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            contentPadding = buttonsContentPadding
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp),
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = null,
            )
            Text(selectedSorting.text, style = MaterialTheme.typography.labelLarge)
        }
        OutlinedButton(
            onClick = { onChangeStateView(CatalogScreenEnum.FILTERS) },
            border = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            contentPadding = buttonsContentPadding
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp),
                imageVector = Icons.Outlined.FilterAlt,
                contentDescription = null,
            )
            Text("Фильтры", style = MaterialTheme.typography.labelLarge)
        }
    }
}