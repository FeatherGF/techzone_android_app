package com.app.techzone

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.ui.theme.TechZoneTheme
import com.app.techzone.ui.theme.reusables.ProductCompactCard
import org.junit.Rule
import org.junit.Test


class TestProductCard {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val product = BaseProduct(
        id = 1,
        name = "Смартфон Apple iPhone 15 Pro 256GB Blue Titanium",
        price = 154_999,
        photos = null,
        discountPercentage = 0,
        reviewsCount = 0,
        rating = null
    )

    @Test
    fun testCardIsDisplayed() {
        composeTestRule.setContent {
            TechZoneTheme {
                WrapperCompositionLocalProvider {
                    ProductCompactCard(
                        product = product,
                        onProductCheckStatus = { false },
                        onProductAction = { false }
                    )
                }
            }
        }
        composeTestRule.onRoot().assertIsDisplayed()
    }
}