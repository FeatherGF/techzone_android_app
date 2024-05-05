package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null
)

data class UserUpdateRequest(
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null
)

typealias ReasonValidationFailed = String

fun validateUserInfo(
    firstName: String,
    lastName: String,
    phoneNumber: String,
): Pair<Boolean, ReasonValidationFailed> {
    if (firstName.any { !it.isLetter() })
        return false to "В поле имени оставьте только буквы без цифр и специальных символов"

    if (lastName.any { !it.isLetter() })
        return false to "В поле фамилии оставьте только буквы без цифр и специальных символов"

    if (phoneNumber.any { it.isLetter() })
        return false to "Введите корректный номер телефона"

   return true to ""
}