package com.app.techzone

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.app.techzone.ui.theme.TechZoneTheme
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.reusables.ProductBuyButton
import org.junit.Rule
import org.junit.Test

class ProductBuyButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productBuyButton_initialState_isInCart() {
        // Mocking the product check status to return true (product is in cart)
        val onProductCheckStatus: (CheckProductStatus) -> Boolean = { true }
        val onProductAction: suspend (ProductAction) -> Boolean = { true }

        composeTestRule.setContent {
            TechZoneTheme {
                WrapperCompositionLocalProvider {
                    ProductBuyButton(
                        productId = 1,
                        isActive = true,
                        onProductCheckStatus = onProductCheckStatus,
                        onProductAction = onProductAction
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithText("В корзине")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun productBuyButton_toggleProduct_addToCart() {
        // Mocking the product check status to return false (product is not in cart)
        val onProductCheckStatus: (CheckProductStatus) -> Boolean = { false }
        var isAddedToCart = false
        val onProductAction: suspend (ProductAction) -> Boolean = {
            if (it is ProductAction.AddToCart) {
                isAddedToCart = true
                true
            } else {
                false
            }
        }

        composeTestRule.setContent {
            TechZoneTheme {
                WrapperCompositionLocalProvider {
                    ProductBuyButton(
                        productId = 1,
                        isActive = true,
                        onProductCheckStatus = onProductCheckStatus,
                        onProductAction = onProductAction
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithText("В корзину")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("В корзине")
            .assertExists()
            .assertIsDisplayed()

        assert(isAddedToCart)
    }

}
