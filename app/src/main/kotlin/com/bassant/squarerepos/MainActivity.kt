package com.bassant.squarerepos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bassant.squarerepos.core.designsystem.theme.SquareReposTheme
import com.bassant.squarerepos.navigation.SquareReposNavHost
import dagger.hilt.android.AndroidEntryPoint

/** Single activity: every screen is a Compose destination inside [SquareReposNavHost]. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SquareReposTheme {
                SquareReposNavHost()
            }
        }
    }
}
