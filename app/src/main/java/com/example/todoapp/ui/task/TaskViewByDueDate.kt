package com.example.todoapp.ui.task

import com.example.todoapp.R
import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.helper.CommonHelper
import com.example.todoapp.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@SuppressLint("ServiceCast", "ScheduleExactAlarm")
@Composable
fun TaskViewByDueDate(taskViewModel: TaskViewModel) {
    val comingTasks = taskViewModel.getUpComingTasks().collectAsState(emptyList())
    val overDueTasks = taskViewModel.getOverdueTasks().collectAsState(emptyList())
    val tasksWithoutDueDate = taskViewModel.getTasksWithoutDueDate().collectAsState(emptyList())
    val pagerState = rememberPagerState(pageCount = { 3 })
    val listTags = listOf("Chưa hết hạn", "Đã hết hạn", "Chưa thiết lập hạn")
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFF4343),
                            Color(0xFFFF6C6C),
                            Color(0xFFFF7E7E)
                        )
                    )
                )
                .padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 20.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_list2),
                        contentDescription = "icon list",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier
                            .size(45.dp)
                    )
                    Text(
                        text = "Xem nhiệm vụ đến hạn",
                        fontSize = 24.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }
                Row {
                    ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFFFFFFFF),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        listTags.forEachIndexed { index, tag ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(
                                        text = listTags[index],
                                        maxLines = 1,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) { pager ->
            when (pager) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(comingTasks.value.size) { index ->
                            TaskItemWithDueDate(
                                task = comingTasks.value[index],
                                onToggle = { checked ->
                                    taskViewModel.updateTaskStatus(
                                        comingTasks.value[index].taskId!!,
                                        checked
                                    )
                                }
                            )
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(overDueTasks.value.size) { index ->
                            TaskItemWithDueDate(
                                task = overDueTasks.value[index],
                                onToggle = { checked ->
                                    taskViewModel.updateTaskStatus(
                                        overDueTasks.value[index].taskId!!,
                                        checked
                                    )
                                }
                            )
                        }
                    }
                }
                2 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(tasksWithoutDueDate.value.size) { index ->
                            TaskItemWithDueDate(
                                task = tasksWithoutDueDate.value[index],
                                onToggle = { checked ->
                                    taskViewModel.updateTaskStatus(
                                        tasksWithoutDueDate.value[index].taskId!!,
                                        checked
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItemWithDueDate(task: TaskEntity, onToggle: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Checkbox(
                checked = task.status,
                onCheckedChange = { checked -> onToggle(checked) }
            )
            Text(
                text = task.taskName
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (task.dueDate.isNullOrBlank()) {
                    "Chưa đặt hạn"
                } else {
                    CommonHelper.dateDifferentDetail(CommonHelper.getCurrentDate(), task.dueDate)
                }
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        ) {
            drawLine(
                color = Color(0xFF969696),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(26f, 16f),
                    phase = 0f
                )
            )
        }
    }
}
