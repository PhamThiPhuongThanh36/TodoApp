package com.example.todoapp.ui.task

import android.util.Log
import androidx.compose.foundation.BorderStroke
import com.example.todoapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.database.entities.TaskWithTags
import com.example.todoapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskViewByTagScreen(taskViewModel: TaskViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .statusBarsPadding()
    ) {
        var tasks = remember { mutableStateListOf<TaskWithTags>() }
        var expand by remember { mutableStateOf(false) }
        val tags = taskViewModel.getTags().collectAsState(emptyList())
        var selectedTag by remember { mutableStateOf<TagEntity?>(null) }
        if (selectedTag != null) {
            LaunchedEffect(selectedTag) {
                taskViewModel.getTasksByTagId(selectedTag!!.tagId!!).collect { list ->
                    tasks.clear()
                    tasks.addAll(list.filterNotNull())
                }
                Log.d("TaskViewByTagScreen", "Tasks: $tasks")
            }
        }
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
                .padding(top = 20.dp, bottom = 20.dp, start = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_list2),
                contentDescription = "icon tag",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier
                    .clickable {
                        expand = !expand
                    }
            )
            ExposedDropdownMenuBox(
                expanded = expand,
                onExpandedChange = { expand = !expand}
            ) {
                Text(
                    text = selectedTag?.tagName ?: "Bạn chưa chọn Tag nào",
                    color = Color(0xFFFFFFFF),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
                ExposedDropdownMenu(
                    expanded = expand,
                    onDismissRequest = { expand = false },
                    containerColor = Color(0xFFFFFFFF),
                    border = BorderStroke(1.dp, Color(0xFFC4C4C4)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width(200.dp)
                ) {
                    tags.value.forEach { tag ->
                        DropdownMenuItem(
                            text = { Text(text = tag.tagName) },
                            onClick = {
                                expand = false
                                selectedTag = tag
                            },
                            modifier = Modifier
                                .width(200.dp),
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_trash),
                                    contentDescription = "delete tag",
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .clickable {
                                            taskViewModel.deleteTag(tag)
                                        }
                                )
                            }
                        )
                    }
                }
            }
        }
        tasks.forEach { task ->
            TaskItem(
                taskWithTags = task,
                onDelete = { taskViewModel.deleteTask(it.taskId!!) },
                onEdit = { },
                onToggle = { checked -> taskViewModel.updateTaskStatus(task.task.taskId!!, checked) }
            )
        }
    }
}


@Preview
@Composable
fun TaskViewByTagScreenPreview() {
    TaskViewByTagScreen(hiltViewModel())
}