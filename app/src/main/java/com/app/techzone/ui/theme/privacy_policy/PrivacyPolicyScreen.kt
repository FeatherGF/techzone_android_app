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
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 40.dp),
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
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 40.dp)
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
        modifier = Modifier.padding(bottom = 12.dp)
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        paragraph.subParagraphs.forEach { subParagraph ->
            Text(
                subParagraph,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.15f.sp,
                    lineHeight = 24.sp
                ),
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f)
            )
        }
    }
}