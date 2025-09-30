package com.example.guessthenumber.Ui

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.guessthenumber.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guessthenumber.Data.GuessedLebel
import com.example.guessthenumber.Data.Levels
import com.example.guessthenumber.Ui.theme.GuessTheNumberTheme
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.delay

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column {
        Scaffold(
            topBar = { GameTopBar() },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // info row contains : 'highest score' and 'Level' states
                // todo : information row
                InformationHeader(highestScore = gameUiState.highest, gameLevel = gameUiState.level)

                // game layout contains the games playscreen : edit field, button, play count
                // todo : gameLayout
                Column(
                    modifier = Modifier
                        .padding(vertical = 80.dp)
                ) {
                    GameLayout(gameUiState = gameUiState, gameViewModel = gameViewModel)
                }

            }
        }
    }

}

// Game app top app bar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.paddingMedium))
                    .size(40.dp),

            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.paddingSmall)))
                )
            }
        },
        actions = {
            IconButton(onClick = {
                // todo : handel onclick share icon
            }) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.shadow(12.dp, ambientColor = MaterialTheme.colorScheme.onBackground)
    )
}

//  shows highest and game level states
@Composable
fun InformationHeader(
    highestScore: Int,
    gameLevel: Levels
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.paddingMedium))
    ) {
        Column(
            modifier = Modifier
                .weight(2.5f)
                .padding(vertical = dimensionResource(R.dimen.paddingSmall)),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(R.string.highest, highestScore),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.paddingSmall))
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = dimensionResource(R.dimen.paddingSmall))
                .clickable {
                    // todo : changing level feature
                },
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${stringResource(R.string.level)} $gameLevel",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.paddingSmall))
            )
        }
    }
}

/*
 * game logic and ui , user interaction section
 */
@Composable
fun GameLayout(
    gameUiState: GameUiState,
    gameViewModel: GameViewModel
) {


    val level: Levels = gameUiState.level
    val playedCount: Int = gameUiState.count
    val score: Int = gameUiState.score

    var isTurnOver: Boolean by remember { mutableStateOf(false) }
    var guessMatchLebel: GuessedLebel = GuessedLebel.FAR
    var guessedLebel: String by remember { mutableStateOf("") }
    val text = remember { mutableStateOf("") }
    var playerGuess: Int by remember { mutableIntStateOf(-1) }
    var secretNumberForLastGuess by remember { mutableStateOf<Int?>(null) }
    // Animation variables to show comparison between numbers
    var showComparisonAnimation by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.paddingMedium))
    )
    {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .shadow(
                    8.dp,
                    ambientColor = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Text(
                text = guessedLebel,
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (guessedLebel == "") 0f else 0.7f)
                    .background(if (guessedLebel == "FAR") Color.Red else if (guessedLebel == "CLOSE") Color.Yellow else if (guessedLebel == "ACCURATE") Color.Green else Color.Gray)
                    .padding(dimensionResource(R.dimen.paddingSmall))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(dimensionResource(R.dimen.paddingLarge)),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() && it.isNotBlank() }){
                            text.value = it
                            playerGuess = if(it.isNotBlank()) it.toInt() else -1
                        }
                    },
                    label = {
                        when (level) {
                            Levels.EASY -> Text("Guess between 1 - 10")
                            Levels.MEDIUM -> Text("Guess between 1- 50")
                            Levels.HARD -> Text("Guess between 1- 100")
                        }
                    },
                    placeholder = { Text("So, What it is?") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)

                )
                TextField(
                    value = "$playedCount/10",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .width(70.dp)
                        .focusProperties { canFocus = false },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    )
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(dimensionResource(R.dimen.paddingLarge)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        // todo : onCheck button click
                        val intValue: Int? = text.value.toIntOrNull()
                        playerGuess = intValue?.toInt() ?: -1
                        if (gameUiState.count == 10) {
                            isTurnOver = true
                        } else if (intValue != null) {
                            secretNumberForLastGuess =
                                gameUiState.randomNumber // Capture the number for this guess
                            guessMatchLebel = gameViewModel.checkHandler(intValue)
                        }
                        guessedLebel = guessMatchLebel.toString()

                        // every time when the button is hit, animated box will be appearing, of course something should be in the input box
                        showComparisonAnimation = false
                        showComparisonAnimation = true
                    },
                    enabled = playerGuess != -1,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.check)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(dimensionResource(R.dimen.paddingLarge)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.score) + ": $score",
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(dimensionResource(R.dimen.paddingSmall))
                )
                // space between both text
                Spacer(modifier = Modifier.weight(1f))
                // machine guess text box
                Text(
                    text = if (secretNumberForLastGuess != null) " $playerGuess v/s $secretNumberForLastGuess" else "",
                    modifier = Modifier
                        .alpha(if (guessedLebel == "" || secretNumberForLastGuess == null) 0f else 1f)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(dimensionResource(R.dimen.paddingSmall))
                )
            }
        }

        AnimatedVisibility(
            visible = showComparisonAnimation,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Start from the right edge
                animationSpec = tween(durationMillis = 500) // Adjust duration as needed
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // Exit to the left edge
                animationSpec = tween(durationMillis = 500) // Adjust duration as needed
            )
        ) {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .shadow(
                        8.dp,
                        ambientColor = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .align(Alignment.Center)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (guessedLebel == "FAR") Color(0xFFFDAAAA) else if (guessedLebel == "CLOSE") Color(
                                0xFFF4991A
                            ) else if (guessedLebel == "ACCURATE") Color(0xFF59AC77) else Color.Gray
                        )
                        .padding(dimensionResource(R.dimen.paddingLarge)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = guessedLebel,
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "$secretNumberForLastGuess",
                            color = Color.DarkGray,
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }

    }


    if (showComparisonAnimation) {
        LaunchedEffect(Unit) {
            delay(1000L) // Wait for 1 second
            showComparisonAnimation = false
        }
    }

    if (isTurnOver) {
        AvgDialoageBox(
            onDismissRequest = {
                // User might dismiss by clicking outside, so this can be left empty
                // or set isTurnOver = false if appropriate
            },
            onConfirmation = {
                isTurnOver = false
                gameViewModel.resetGame()
                secretNumberForLastGuess = null // Reset for new game
                guessedLebel = "" // Reset for new game
            },
            dialogTitle = "Score",
            dialogText = "It turns out that your guess is ${gameUiState.highest * 2}% accurate"
        )
    }
}

@Composable
fun AvgDialoageBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    val context = LocalContext.current
    AlertDialog(
        // todo : Icon
        icon = {
            Icon(
                Icons.Filled.Share,
                contentDescription = null
            )
        },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText, textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.Bold)},
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                 Text("Play again")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    (context as? Activity)?.finish() // Exit the game
                }
            ) {
                Text("Exit Game")
            }
        }
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun ElementPreview(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    GuessTheNumberTheme {
        GameLayout(gameUiState = gameUiState, gameViewModel = gameViewModel)
    }
}
