package com.example.todoapp.ui.common

import com.example.todoapp.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OperationCustom(icon: Int, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "icon",
            tint = Color(0xFF4C4C4C),
            modifier = Modifier
                .padding(start = 20.dp, end = 10.dp)
                .size(16.dp)
        )
        Text(
            text = text,
            fontSize = 15.sp
        )
    }
}

@Preview
@Composable
fun OperationPreview() {
    OperationCustom(
        R.drawable.ic_edit, "Chỉnh sửa danh sách"
    )
}