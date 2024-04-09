package com.app.techzone.ui.theme.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.Photo
import com.app.techzone.data.remote.repository.PhotoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepo: PhotoRepo
): ViewModel() {
    private val _state = MutableStateFlow(emptyList<Photo>())
    val state: StateFlow<List<Photo>>
        get() = _state

    init {
        viewModelScope.launch {
            // TODO: add exception handling
            val cat = photoRepo.getPhotos()
            _state.value = cat
        }
    }
}