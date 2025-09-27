package com.example.todoapp.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.viewmodel.TaskViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.model.TaskWithListAndProject
import com.example.todoapp.ui.common.ChoiceDialog
import kotlinx.coroutines.launch

@Composable
fun DeletedTasksScreen(taskViewModel: TaskViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .statusBarsPadding()
    ) {
        var isShowChoiceDialog by remember { mutableStateOf(false) }
        var editTaskId by remember { mutableStateOf(-1) }
        val coroutineScope = rememberCoroutineScope()
        val editTask = if (editTaskId != -1) taskViewModel.getTaskById(editTaskId).collectAsState(initial = null).value else { null }
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
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            Text(
                text = "Danh sách các Task đã xóa",
                fontSize = 24.sp,
                color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        }
        val tasks = taskViewModel.getTasksWithListAndProject().collectAsState(initial = emptyList())
        Log.d("DeletedTasksScreen", "tasks: ${tasks.value}")
        LazyColumn {
            items(tasks.value.size) { index ->
                DeletedTaskItem(
                    task = tasks.value[index],
                    modifier = Modifier
                        .clickable {
                            editTaskId = tasks.value[index].taskId ?: -1
                            isShowChoiceDialog = true
                        }
                )
            }
        }
        if (isShowChoiceDialog) {
            ChoiceDialog (
                onDismiss = { isShowChoiceDialog = false },
                onDelete = {
                    isShowChoiceDialog = false
                    taskViewModel.deleteTask(editTaskId)
                },
                onRestore = {
                    isShowChoiceDialog = false
                    coroutineScope.launch {
                        taskViewModel.restoreTask(editTaskId)
                    }
                }
            )
        }
    }
}

@Composable
fun DeletedTaskItem(task: TaskWithListAndProject, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
        ) {
            Text(
                text = task.taskName,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = task.listName,
                fontSize = 14.sp
            )
            Icon(
                painter = painterResource(R.drawable.ic_next),
                contentDescription = "icon next",
                tint = Color(0xFF4B4B4B),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = task.projectName,
                fontSize = 14.sp
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

@Preview
@Composable
fun DeletedTasksActivityPreview() {
    DeletedTasksScreen(hiltViewModel())
//    DeletedTaskItem(
//        task = TaskWithListAndProject(
//            taskId = 1,
//            taskName = "Task 1",
//            listId = 1,
//            listName = "List 1",
//            projectId = 1,
//            projectName = "Project",
//        )
//    )
}