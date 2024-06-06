package com.app.techzone.ui.theme.server_response

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.app.techzone.ui.theme.dimension
import com.app.techzone.utils.PullToRefresh

@Composable
fun ErrorScreen(onRefreshApiCall: () -> Unit = {}) {
    var isRefreshing by remember { mutableStateOf(false) }
    PullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefreshApiCall()
            isRefreshing = false
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    start = MaterialTheme.dimension.extendedMedium,
                    end = MaterialTheme.dimension.extendedMedium,
                    top = MaterialTheme.dimension.huge
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Error Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(MaterialTheme.dimension.huge)
            )
            Text(
                "Что-то пошло не так",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                modifier = Modifier.padding(
                    top = MaterialTheme.dimension.medium,
                    bottom = MaterialTheme.dimension.small
                )
            )
            Text(
                "Проверьте подключение \nк интернету и обновите страницу",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center,
                color = Color(49, 58, 92, 60)
            )
            Button(
                onClick = { isRefreshing = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.dimension.medium)
            ) {
                Text(
                    "Обновить",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}