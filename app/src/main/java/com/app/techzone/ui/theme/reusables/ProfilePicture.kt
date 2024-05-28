package com.app.techzone.ui.theme.reusables

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder100

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    userPhotoUrl: String? = null,
    imageUri: Uri? = null,
    iconTint: Color = ForStroke.copy(alpha = 0.3f)
) {
    if (userPhotoUrl == null && imageUri == null) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            tint = iconTint,
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = imageUri ?: userPhotoUrl,
            contentDescription = null,
            modifier = modifier.clip(RoundBorder100),
            filterQuality = FilterQuality.None,
            contentScale = ContentScale.Crop
        )
    }
}