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
import com.app.techzone.data.remote.model.ReviewShort
import com.app.techzone.ui.theme.DarkText
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UserViewModel
import com.app.techzone.ui.theme.reusables.ProductCrossedPrice
import com.app.techzone.ui.theme.reusables.ProductImageOrPreview
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.calculateDiscount
import com.app.techzone.utils.formatPrice
import kotlinx.coroutines.launch

@Composable
fun ReviewScreenRoot(userViewModel: UserViewModel, orderId: Int, productId: Int) {
    val orderItem by userViewModel.orderItemForReview.collectAsStateWithLifecycle()
    val review by userViewModel.review.collectAsStateWithLifecycle()
    LaunchedEffect(userViewModel.orderItemForReview) {
        userViewModel.loadProductForReview(orderId, productId)
    }
    when (userViewModel.state.response) {
        ServerResponse.LOADING -> {
            LoadingBox()
        }

        ServerResponse.ERROR -> {
            ErrorScreen {
                userViewModel.loadProductForReview(orderId, productId)
            }
        }

        ServerResponse.SUCCESS -> {
            orderItem?.let {
                ReviewScreen(
                    orderItem = it,
                    review = review,
                    onReviewAction = userViewModel::onReviewAction
                )
            }
        }

        ServerResponse.UNAUTHORIZED -> {}
    }
}

@Composable
private fun ReviewTopBar() {
    val navController = LocalNavController.current
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(
                start = MaterialTheme.dimension.mediumLarge,
                top = MaterialTheme.dimension.huge,
                bottom = MaterialTheme.dimension.extendedMedium,
                end = MaterialTheme.dimension.large
            ),
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
private fun ReviewScreen(
    orderItem: OrderItem,
    review: ReviewShort?,
    onReviewAction: suspend (ReviewAction) -> Boolean?
) {
    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ReviewTopBar()

        // Product Info
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .fillMaxWidth()
                .border(width = 1.dp, color = ForStroke)
                .padding(
                    vertical = MaterialTheme.dimension.medium,
                    horizontal = MaterialTheme.dimension.extendedMedium
                ),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.medium)
        ) {
            ProductImageOrPreview(
                Modifier.size(MaterialTheme.dimension.huge),
                photos = orderItem.product.photos,
                filterQuality = FilterQuality.None,
            )
            val style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp
            )
            val color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small)) {
                Text(
                    "${orderItem.product.name} (${orderItem.quantity} шт.)",
                    style = style,
                    color = color
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductCrossedPrice(
                        price = orderItem.product.price,
                        discountPercentage = orderItem.product.discountPercentage,
                        large = true
                    )
                    Text(
                        formatPrice(
                            calculateDiscount(
                                orderItem.product.price,
                                orderItem.product.discountPercentage
                            )
                        ),
                        style = style,
                        color = color
                    )
                }
            }
        }

        // Review description
        val (reviewText, onReviewTextChange) = remember { mutableStateOf(review?.text ?: "") }
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    vertical = MaterialTheme.dimension.large,
                    horizontal = MaterialTheme.dimension.extendedMedium
                ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.mediumLarge)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary),
                minLines = 3,
            )
            val starsStateMapping = (0..4).associateBy {
                remember {
                    if (review != null)
                        mutableStateOf(review.rating >= it + 1)
                    else
                        mutableStateOf(false)
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimension.extendedMedium),
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
                        if (state.value) {
                            Icon(
                                modifier = Modifier.size(MaterialTheme.dimension.extraLarge),
                                imageVector = Icons.Filled.StarRate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                modifier = Modifier.size(MaterialTheme.dimension.extraLarge),
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
                        val action = if (review != null)
                            ReviewAction.EditReview(review.id, reviewText, rating)
                        else
                            ReviewAction.AddReview(orderItem.product.id, reviewText, rating)
                        val response = onReviewAction(action)
                        if (response == null) {
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