package com.example.todoapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogCustom(title: String, label: String, value: String, onValueChange: (String) -> Unit, onDelete: @Composable () -> Unit = {},onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFBD6C6C), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(15.dp)
            ) {
                Text(
                    text = title,
                    color = Color(0xFFFFFFFF),
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp)
            )
            TextFieldCustom (
                leadingIcon = {},
                trailingIcon = {},
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = Color(0xFFFFFFFF),
//                    unfocusedContainerColor = Color(0xFFFFFFFF),
//                    focusedIndicatorColor = Color(0xFFB3B3B3),
//                    unfocusedIndicatorColor = Color(0xFFB3B3B3),
//                ),
//                modifier = Modifier
//                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
//                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp)
                    .fillMaxWidth()
            ) {
                onDelete()
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Hủy",
                    fontSize = 14.sp,
                    color = Color(0xFFE40000),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable { onDismiss() }
                )

                Text(
                    text = "Lưu",
                    fontSize = 14.sp,
                    color = Color(0xFFE40000),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onConfirm() }
                )
            }
        }
    }
}

@Preview
@Composable
fun DialogCustomPreview() {
    DialogCustom("Chỉnh sửa danh sách", "Tên danh sách", "", {}, {}, {}, {})
}