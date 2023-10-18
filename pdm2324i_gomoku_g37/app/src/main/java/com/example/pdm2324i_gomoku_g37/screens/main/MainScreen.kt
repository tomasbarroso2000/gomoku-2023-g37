package com.example.pdm2324i_gomoku_g37.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.pdm2324i_gomoku_g37.R
import com.example.pdm2324i_gomoku_g37.screens.components.LargeCustomButtonView
import com.example.pdm2324i_gomoku_g37.screens.components.CustomContainerView
import com.example.pdm2324i_gomoku_g37.screens.components.CustomFooterView
import com.example.pdm2324i_gomoku_g37.screens.components.LargeCustomTitleView
import com.example.pdm2324i_gomoku_g37.screens.components.MAIN_SCREEN_BUTTON_FONT_SIZE
import com.example.pdm2324i_gomoku_g37.screens.components.MAIN_SCREEN_DEFAULT_PADDING
import com.example.pdm2324i_gomoku_g37.screens.components.MAIN_SCREEN_SPACING_PADDING


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLoginRequested: () -> Unit = {}, onRegisterRequested: () -> Unit = {}) =
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            CustomFooterView {
                Text(
                    text = stringResource(id = R.string.activity_main_footer),
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { padding ->
        CustomContainerView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LargeCustomTitleView(text = "Gomoku")
            Image(painter = painterResource(id = R.drawable.img_gomoku_icon), contentDescription = null)
            DescriptionContainer()
            ButtonsContainer(onLoginRequested, onRegisterRequested)
        }
    }

@Composable
private fun DescriptionContainer() =
    CustomContainerView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = MAIN_SCREEN_DEFAULT_PADDING)
    ) {
        Text(
            text = stringResource(id = R.string.activity_main_description),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }

@Composable
private fun ButtonsContainer(onLoginRequested: () -> Unit, onRegisterRequested: () -> Unit) =
    CustomContainerView(
        modifier = Modifier.padding(top = MAIN_SCREEN_SPACING_PADDING)
    ) {
        LargeCustomButtonView(onClick = onLoginRequested) {
            Text(
                text = stringResource(id = R.string.activity_main_login),
                fontSize = MAIN_SCREEN_BUTTON_FONT_SIZE
            )
        }

        Spacer(modifier = Modifier.height(MAIN_SCREEN_SPACING_PADDING))

        LargeCustomButtonView {
            Text(
                text = stringResource(id = R.string.activity_main_signup),
                fontSize = MAIN_SCREEN_BUTTON_FONT_SIZE
            )
        }
    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
