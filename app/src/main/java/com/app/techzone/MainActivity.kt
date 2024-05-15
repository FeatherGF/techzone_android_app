package com.app.techzone

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.techzone.ui.theme.TechZoneTheme
import com.app.techzone.ui.theme.navigation.Main
import dagger.hilt.android.AndroidEntryPoint

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("no LocalSnackbarHostState provided")
}

val LocalNavController = compositionLocalOf<NavController> {
    error("no LocalNavController provided")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // market places don't support landscape orientations
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            TechZoneTheme {
                // support transparent status bar and appropriately
                // colored navigation bar with action buttons
                enableEdgeToEdge(
                    navigationBarStyle = SystemBarStyle.light(
                        MaterialTheme.colorScheme.tertiary.toArgb(),
                        0
                    )
                )

                // avoid passing around snackbar host state and get it from localSnackbarHostState
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                CompositionLocalProvider(
                    values = arrayOf(
                        LocalSnackbarHostState provides snackbarHostState,
                        LocalNavController provides navController
                    )
                ) {
                    Main(navController)
                }
            }
        }
    }
}
