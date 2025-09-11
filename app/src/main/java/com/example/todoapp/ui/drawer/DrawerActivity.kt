package com.example.todoapp.ui.drawer

import androidx.compose.foundation.Image
import com.example.todoapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFFFF7171))
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(80.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.thanhxinh),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "Xin chào",
                    color = Color(0xFFFFFFFF),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Phương Thanh",
                    color = Color(0xFFFFFFFF),
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_logout),
                tint = Color(0xFFFFFFFF),
                contentDescription = "log out"
            )
        }
    }
}

@Preview
@Composable
fun DrawerScreenPreview() {
    DrawerScreen()
}