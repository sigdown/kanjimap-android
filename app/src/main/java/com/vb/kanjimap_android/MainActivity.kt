package com.vb.kanjimap_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vb.kanjimap_android.app.ui.AppScaffold
import com.vb.kanjimap_android.ui.theme.KanjimapandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KanjimapandroidTheme {
                AppScaffold()
            }
        }
    }
}
