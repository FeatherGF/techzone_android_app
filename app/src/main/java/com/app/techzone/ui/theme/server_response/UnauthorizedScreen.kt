package com.app.techzone.ui.theme.server_response

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.app.techzone.LocalNavController
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.LoginText

@Composable
fun UnauthorizedScreen() {
    val navController = LocalNavController.current
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(start = MaterialTheme.dimension.extendedMedium, top = MaterialTheme.dimension.huge, end = MaterialTheme.dimension.extendedMedium)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(MaterialTheme.dimension.huge),
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        LoginText()
        Text(
            modifier = Modifier.padding(top = MaterialTheme.dimension.small),
            textAlign = TextAlign.Center,
            text = "Чтобы сохранять товары\n и совершать покупки",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.scrim
        )
        Button(
            modifier = Modifier
                .padding(top = MaterialTheme.dimension.extendedMedium)
                .fillMaxWidth(),
            onClick = { navController.navigate(ScreenRoutes.PROFILE_REGISTRATION) }
        ) {
            Text(
                text = "Войти или зарегистрироваться",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}