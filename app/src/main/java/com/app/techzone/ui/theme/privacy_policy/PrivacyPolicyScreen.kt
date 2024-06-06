package com.app.techzone.ui.theme.privacy_policy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.techzone.LocalNavController
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.dimension


@Composable
fun PrivacyPolicy() {
    val navController = LocalNavController.current
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
            IconButton(onClick = navController::popBackStack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                )
            }
            Text(
                "Политика \nконфиденциальности",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.Transparent
            )
        }
        val privacyPolicyText = PrivacyPolicyText()
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.large),
            contentPadding = PaddingValues(
                start = MaterialTheme.dimension.extendedMedium,
                end = MaterialTheme.dimension.extendedMedium,
                top = MaterialTheme.dimension.large,
                bottom = MaterialTheme.dimension.extraLarge
            )
        ) {
            items(privacyPolicyText.paragraphs, key = { it.hashCode() }) { paragraph ->
                PrivacyParagraph(paragraph)
            }
        }
    }
}


@Composable
private fun PrivacyParagraph(paragraph: IPrivacyPolicyParagraph) {
    Text(
        paragraph.title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
        modifier = Modifier.padding(bottom = MaterialTheme.dimension.medium)
    )
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small)) {
        paragraph.subParagraphs.forEach { subParagraph ->
            Text(
                subParagraph,
                style = MaterialTheme.typography.bodyLarge,
                letterSpacing = 0.1.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f)
            )
        }
    }
}