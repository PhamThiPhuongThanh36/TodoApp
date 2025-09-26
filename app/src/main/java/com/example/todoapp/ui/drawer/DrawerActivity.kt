package com.example.todoapp.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import com.example.todoapp.R
import com.example.todoapp.ui.common.OperationCustom

@Composable
fun DrawerScreen(
    projectSection: @Composable () -> Unit?,
    onAddNewProject: () -> Unit,
    onProjectViewByTag: () -> Unit,
    onProjectViewByDueDate: () -> Unit,
    onPomodoroMode: () -> Unit,
    onTag: () -> Unit,
    onDeletedProjectView: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight()
            .background(Color(0xFFFFFFFF))
            .statusBarsPadding()
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
        projectSection()
        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFB1B1B1)
        )
        OperationCustom(
            R.drawable.ic_add_project,
            text = "Danh sách mới",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onAddNewProject() }
        )
        OperationCustom(
            R.drawable.ic_tag,
            text = "Xem nhiệm vụ theo loại nhãn",
            modifier = Modifier
                .clickable { onProjectViewByTag() }
        )
        OperationCustom(
            R.drawable.ic_lock,
            text = "Xem nhiệm vụ đến hạn",
            modifier = Modifier
                .clickable { onProjectViewByDueDate() }
        )
        OperationCustom(
            R.drawable.ic_countdown,
            text = "Chế độ tập trung",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { onPomodoroMode() }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFB1B1B1)
        )
        OperationCustom(
            R.drawable.ic_full_tag,
            text = "Thêm nhãn mới",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onTag() }
        )
        OperationCustom(
            R.drawable.ic_trash,
            text = "Các mục đã xóa",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { onDeletedProjectView()}
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFB1B1B1)
        )
    }
}

@Preview
@Composable
fun DrawerScreenPreview() {
    DrawerScreen({}, {},  {}, {}, {}, {}, {})
}