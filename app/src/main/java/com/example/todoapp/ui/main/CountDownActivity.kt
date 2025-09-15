package com.example.todoapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.util.TableInfo

@Composable
fun CountDownScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF44336))
            .statusBarsPadding()
    ) {

    }
}

@Preview
@Composable
fun CountDownPreview() {
    CountDownScreen()
}