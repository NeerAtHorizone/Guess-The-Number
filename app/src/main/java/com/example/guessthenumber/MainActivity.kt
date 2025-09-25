package com.example.guessthenumber

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.guessthenumber.Ui.GameScreen
import com.example.guessthenumber.Ui.theme.GuessTheNumberTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            GuessTheNumberTheme {
                GameScreen()
            }
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name  = "DefaultPreviewLight"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name  = "DefaultPreviewDark"
)
@Composable
fun DefaultPreview(){
    GuessTheNumberTheme {
        GameScreen()
    }
}

// todo: glitch of null
// todo: add change level function
// todo: add share icon feature
// todo: update highest score avg
// todo: 01 add sound
// todo: 02 add ad sense
// todo: 03 deploy on itch.io
