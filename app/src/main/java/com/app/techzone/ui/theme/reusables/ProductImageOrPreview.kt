package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.techzone.R
import com.app.techzone.data.remote.model.Photo

@Composable
fun ProductImageOrPreview(
    modifier: Modifier = Modifier,
    photos: List<Photo>? = null,
    photoIndex: Int = 0,
    filterQuality: FilterQuality = FilterQuality.Low,
    description: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    if (photos != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photos[photoIndex].url)
                .build(),
            contentDescription = description,
            filterQuality = filterQuality,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_preview),
            contentDescription = description,
            modifier = modifier
        )
    }
}