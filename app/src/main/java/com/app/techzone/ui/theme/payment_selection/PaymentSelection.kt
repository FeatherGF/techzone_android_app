package com.app.techzone.ui.theme.payment_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.model.PaymentType
import com.app.techzone.ui.theme.dimension
import com.app.techzone.utils.defaultPaymentTypes

@Composable
fun PaymentTypes(
    modifier: Modifier = Modifier,
    selectedType: Pair<String, PaymentType>,
    onPaymentTypeChange: (Pair<String, PaymentType>) -> Unit
) {
    Column(
        modifier
            .selectableGroup()
            .background(MaterialTheme.colorScheme.tertiary)
            .border(
                width = 1.dp,
                color = ForStroke.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        defaultPaymentTypes.forEachIndexed { index, paymentTypePair ->
            val (name, _) = paymentTypePair
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimension.huge)
                    .selectable(
                        selected = selectedType == paymentTypePair,
                        onClick = { onPaymentTypeChange(paymentTypePair) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimension.larger),
                    checked = selectedType == paymentTypePair,
                    onCheckedChange = null
                )
                Text(
                    name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
            }
            // don't render divider after last, because border will do it
            if (index != defaultPaymentTypes.size - 1) {
                HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
fun PaymentSelection(paymentViewModel: PaymentSelectionViewmodel) {
    val navController = LocalNavController.current
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar { navController.popBackStack() }
        PaymentTypes(
            Modifier.padding(
                horizontal = MaterialTheme.dimension.extendedMedium,
                vertical = MaterialTheme.dimension.larger
            ),
            selectedType = paymentViewModel.selectedPayment,
            onPaymentTypeChange = paymentViewModel::onPaymentSelected
        )
    }
}

@Composable
private fun TopBar(onBackClicked: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = ForStroke)
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(
                start = MaterialTheme.dimension.extendedMedium,
                end = MaterialTheme.dimension.extendedMedium,
                bottom = MaterialTheme.dimension.extendedMedium,
                top = MaterialTheme.dimension.extraLarge
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
            )
        }
        Text(
            "Способ оплаты",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.Transparent
        )
    }
}
