package com.app.techzone.ui.theme.payment_selection

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.app.techzone.data.remote.repository.EncryptedSharedPreferencesImpl
import com.app.techzone.data.remote.repository.PreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    // TODO: вынести это в репозиторий, где после ответа от бека ставится карта в preferences
    private val prefs: EncryptedSharedPreferencesImpl
): ViewModel() {
    private val gson = Gson()
    var state by mutableStateOf(PaymentState())

    private var _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards = _cards.asStateFlow()

    init {
        _cards.update { _getCards() }
    }

    private fun _getCards(): List<Card> {
        val storedCards = prefs.getKey(PreferencesKey.savedCards) ?: return emptyList()
        val type = object : TypeToken<List<Card>>(){}.type
        return gson.fromJson(storedCards, type)
    }

    fun onEvent(event: PaymentUiEvent) {
        when (event){
            is PaymentUiEvent.CardNumberChanged -> {
                state = state.copy(cardNumber = event.value)
            }
            PaymentUiEvent.CheckCard -> {
                val dateFormatter = SimpleDateFormat("MMyy", Locale("ru"))
                if (state.expirationDate.substring(0, 2).toInt() !in 0..12){
                    state = state.copy(cardExpiredText = "Некорректный месяц срока действия")
                    return
                }
                val date = dateFormatter.parse(state.expirationDate)
                if (date.time < System.currentTimeMillis()) {
                    state = state.copy(cardExpiredText = "Срок годности карты истёк")
                    return
                }
                // TODO: api call to backend
                val newCard = Card(
                    cardNumber = state.cardNumber,
                    expirationDate = state.expirationDate,
                    code = state.code
                )
                val storedCards = prefs.getKey(PreferencesKey.savedCards)
                val cards = if (storedCards == null) listOf(newCard) else {
                    val type = object : TypeToken<List<Card>>(){}.type
                    val storedDecodedCards: List<Card> = gson.fromJson(storedCards, type)
                    listOf(newCard) + storedDecodedCards
                }
                val encodedCards = gson.toJson(cards)
                _cards.update { cards }
                prefs.sharedPreferences.edit().putString(
                    PreferencesKey.savedCards, encodedCards
                ).apply()
                state = state.copy(
                    screen = PaymentScreens.CHOOSE_PAYMENT,
                    cardNumber = "",
                    expirationDate = "",
                    code = ""
                )
            }
            is PaymentUiEvent.DeleteCard -> {
                var storedCards = _getCards()
                if (storedCards.isEmpty()) return
                storedCards = storedCards.filter {it.cardNumber != event.cardNumber}
                val encodedCards = gson.toJson(storedCards)
                prefs.sharedPreferences.edit().putString(
                    PreferencesKey.savedCards, encodedCards
                ).apply()
                _cards.update { storedCards }
            }
            is PaymentUiEvent.CodeChanged -> {
                state = state.copy(code = event.value)
            }
            PaymentUiEvent.EnterCard -> {
                state = state.copy(screen = PaymentScreens.ENTER_CARD)
            }
            is PaymentUiEvent.ExpirationDateChanged -> {
                state = state.copy(expirationDate = event.value, cardExpiredText = null)
            }
            is PaymentUiEvent.ChoosePayment -> {
                state = state.copy(screen = PaymentScreens.CHOOSE_PAYMENT)
            }
        }
    }

}
