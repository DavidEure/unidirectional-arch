package com.example.unidirectional.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.unidirectional.R
import com.example.unidirectional.infoWorkingIn

@Composable
fun Greeting(viewModel: GreetingViewModel, onNextClick: () -> Unit, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateLifecycleAware = remember(viewModel.state, lifecycleOwner) {
        viewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val state by stateLifecycleAware.collectAsState(GreetingViewState())

    Greeting(
        state = state,
        onGreetClick = { viewModel.greet() },
        onNextClick = onNextClick,
        onNickNameChange = { viewModel.onNickNameChange(it) },
        modifier = modifier
    )
}

@Composable
fun Greeting(
    state: GreetingViewState,
    onGreetClick: () -> Unit,
    onNextClick: () -> Unit,
    onNickNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NicknameInput(state.nickname, onNickNameChange)
            Button(
                modifier = Modifier.padding(top = 70.dp),
                onClick = { onGreetClick() },
                content = { Text("Greet") }
            )

            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = when {
                    state.successMessage.isNotBlank() -> state.successMessage
                    state.failureMessage.isNotBlank() -> state.failureMessage
                    else -> ""
                },
                color = if (state.failureMessage.isNotBlank()) colorResource(R.color.purple_200) else Color.Black
            ).also { infoWorkingIn("Recompose Greeting Text") }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { onNextClick() },
            content = { Icon(Icons.Filled.ArrowForward, "") }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview(modifier: Modifier = Modifier) {
    Greeting(
        modifier = modifier,
        onGreetClick = {},
        onNextClick = {},
        onNickNameChange = {},
        state = GreetingViewState()
    )
}