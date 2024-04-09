package com.app.techzone.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.techzone.data.api.model.Photo
import com.app.techzone.ui.photo_viewmodel.PhotoViewModel


@Composable
fun ProfileScreen() {
    // WILL BE REMOVED WHEN BACKEND RELEASES ON HOSTING
    val photoViewModel = hiltViewModel<PhotoViewModel>()
    val state by photoViewModel.state.collectAsStateWithLifecycle()

    LazyColumn {
        if (state.isEmpty()) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }
        items(state) {photo: Photo ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo.url)
                    .size(600)
                    .build(),
                contentDescription = null
                )
            Text(photo.title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
