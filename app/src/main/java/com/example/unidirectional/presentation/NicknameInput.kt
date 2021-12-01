package com.example.unidirectional.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NicknameInput(text: String, onTextChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        modifier = modifier.padding(15.dp),
        value = text,
        onValueChange = onTextChange,
        label = { Text("write your nickname") },
        singleLine = true,
        maxLines = 1
    )
}


@Preview(showBackground = true)
@Composable
fun NicknameInputPreview() {
    NicknameInput(text = "", onTextChange = {})
}