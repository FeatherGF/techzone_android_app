package com.app.techzone.ui.theme.reviews

sealed class ReviewAction {
    data class AddReview(
        val productId: Int, val text: String? = null, val rating: Int
    ): ReviewAction()

    data class EditReview(
        val reviewId: Int, val text: String? = null, val rating: Int
    ): ReviewAction()
}
