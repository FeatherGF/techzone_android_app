package com.app.techzone

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.app.techzone.ui.theme.TechZoneTheme
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.reusables.ProductFavoriteIcon
import org.junit.Rule
import org.junit.Test

@Composable
fun WrapperCompositionLocalProvider(content: @Composable () -> Unit ) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    CompositionLocalProvider(
        values = arrayOf(
            LocalSnackbarHostState provides snackbarHostState,
            LocalNavController provides navController
        ), content = content
    )
}

class TestFavoriteIcon {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testIsFavoriteIconDisplayed() {
        composeRule.setContent {
            TechZoneTheme {
                WrapperCompositionLocalProvider {
                    ProductFavoriteIcon(
                        productId = 1,
                        onProductCheckStatus = { false },
                        onProductAction = { false }
                    )
                }
            }
        }
        val iconButton = composeRule.onNodeWithContentDescription("favorite icon")
        iconButton.assertExists()
        iconButton.assertIsDisplayed()
        iconButton.assertHasClickAction()
    }

    @Test
    fun testIsFavoriteChangedOnClick() {
        var isAddedToFavorites = false
        val onProductAction: suspend (ProductAction) -> Boolean = {
            if (it is ProductAction.AddToFavorites) {
                isAddedToFavorites = true
                true
            } else {
                false
            }
        }
        composeRule.setContent {
            TechZoneTheme {
                WrapperCompositionLocalProvider {
                    ProductFavoriteIcon(
                        productId = 1,
                        onProductCheckStatus = { false },
                        onProductAction = onProductAction
                    )
                }
            }
        }
        val iconButton = composeRule.onNodeWithContentDescription("favorite icon")
        iconButton.performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithContentDescription("favorite icon").assertIsDisplayed()
        assert(isAddedToFavorites)
    }
}