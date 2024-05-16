package com.app.techzone.ui.theme.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.LocalNavController
import com.app.techzone.LocalSnackbarHostState
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.ui.theme.DarkText
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.main.ProductCrossedPrice
import com.app.techzone.ui.theme.main.ProductImageOrPreview
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UserViewModel
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.formatPrice
import kotlinx.coroutines.launch

@Composable
fun AddReviewScreenRoot(userViewModel: UserViewModel, orderId: Int, productId: Int) {
    LaunchedEffect(userViewModel.orderItemForReview) {
        userViewModel.loadProductForReview(orderId, productId)
    }
    val orderItem by userViewModel.orderItemForReview.collectAsStateWithLifecycle()
    when (userViewModel.state.response){
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> {
            ErrorScreen {
                userViewModel.loadProductForReview(orderId, productId)
            }
        }
        ServerResponse.SUCCESS -> {
            orderItem?.let {
                AddReviewScreen(orderItem = it) { text: String?, rating: Int ->
                    userViewModel.addReview(productId, rating, text)
                }
            }
        }
        ServerResponse.UNAUTHORIZED -> {}
    }
}

@Composable
private fun AddReviewTopBar(){
    val navController = LocalNavController.current
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(start = 20.dp, top = 56.dp, bottom = 16.dp, end = 28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = Color.Companion.Transparent
        )
        Text(
            "Отзыв",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
        )
        IconButton(onClick = navController::popBackStack) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = Color.Black,
            )
        }
    }
}


@Composable
fun AddReviewScreen(orderItem: OrderItem, addReview: suspend (String?, Int) -> Boolean?) {
    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        AddReviewTopBar()

        // Product Info
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .fillMaxWidth()
                .border(width = 1.dp, color = ForStroke)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProductImageOrPreview(
                photos = orderItem.product.photos,
                filterQuality = FilterQuality.None,
                modifier = Modifier.size(60.dp)
            )
            val style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp
            )
            val color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "${orderItem.product.name} (${orderItem.quantity} шт.)",
                    style = style,
                    color = color
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    ProductCrossedPrice(product = orderItem.product, large = true)
                    Text(
                        formatPrice(orderItem.product.price), style = style, color = color
                    )
                }
            }
        }

        // Review description
        val (reviewText, onReviewTextChange) = remember { mutableStateOf("")}
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 28.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = reviewText,
                onValueChange = onReviewTextChange,
                placeholder = {
                    Text(
                        "Отзыв о товаре",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
            val starsStateMapping = (0..4).associateBy { remember {mutableStateOf(false) } }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                starsStateMapping.forEach { (state, index) ->
                    IconButton(
                        onClick = {
                            starsStateMapping.forEach { (state, pressedIndex) ->
                                state.value = pressedIndex <= index
                            }
                        },
                    ) {
                        if (state.value){
                            Icon(
                                modifier = Modifier.size(40.dp),
                                imageVector = Icons.Filled.StarRate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                imageVector = Icons.Outlined.StarRate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    scope.launch {
                        val rating = starsStateMapping.keys.filter { it.value }.size
                        val response = addReview(reviewText, rating)
                        if (response == null){
                            snackbarHostState.showSnackbar(
                                "Что-то пошло не так\nПроверьте подключение к интернету"
                            )
                            return@launch
                        }
                        if (response) {
                            navController.popBackStack()
                        } else {
                            snackbarHostState.showSnackbar(
                                "Вы уже добавили отзыв на этот товар"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = starsStateMapping.any { it.key.value },
            ) {
                Text(
                    "Отправить",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}