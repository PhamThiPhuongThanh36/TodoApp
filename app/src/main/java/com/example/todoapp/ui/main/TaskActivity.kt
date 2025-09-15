package com.example.todoapp.ui.main

import com.example.todoapp.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskScreen() {
    Scaffold(
        floatingActionButton = {
            Icon(
                painter = painterResource(R.drawable.ic_add_task),
                tint = Color(0xFFFF6666),
                contentDescription = "icon add task",
                modifier = Modifier
                    .padding(bottom = 40.dp, end = 20.dp)
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

        }
    }
}

@Preview
@Composable
fun TaskPreview() {
    TaskScreen()
}