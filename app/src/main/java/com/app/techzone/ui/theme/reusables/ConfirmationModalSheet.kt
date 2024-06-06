package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.app.techzone.ui.theme.dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationModalSheet(
    confirmationText: String,
    confirmOptionText: String = "Удалить",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.dimension.large,
                    end = MaterialTheme.dimension.large,
                    bottom = MaterialTheme.dimension.large
                ),
        ) {
            if (confirmationText.isNotEmpty()) {
                Text(
                    confirmationText,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MaterialTheme.dimension.medium,
                        bottom = MaterialTheme.dimension.small
                    )
                    .height(MaterialTheme.dimension.extraLarge),
                onClick = onConfirm
            ) {
                Text(
                    confirmOptionText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimension.extraLarge),
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    "Отменить",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationModalSheet(
    confirmationText: String,
    confirmOptionText: String = "Удалить",
    thirdActionOptionText: String,
    onThirdAction: () -> Unit,
    thirdActionButtonColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.error
    ),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.dimension.large,
                    end = MaterialTheme.dimension.large,
                    bottom = MaterialTheme.dimension.large
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.extraSmall)
        ) {
            if (confirmationText.isNotEmpty()) {
                Text(
                    confirmationText,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onConfirm
            ) {
                Text(
                    confirmOptionText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onThirdAction,
                colors = thirdActionButtonColors
            ) {
                Text(
                    thirdActionOptionText,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    "Отменить",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}