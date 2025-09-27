package com.example.todoapp.ui.task

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.example.todoapp.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.util.TableInfo
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.database.entities.TaskTagEntity
import com.example.todoapp.database.entities.TaskWithTags
import com.example.todoapp.helper.CommonHelper
import com.example.todoapp.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(taskViewModel: TaskViewModel, navController: NavController, listId: Int) {
    var isShowModalSheetAddTask by remember { mutableStateOf(false) }
    var editTaskId by remember { mutableStateOf(-1) }
    var tasks = remember { mutableStateListOf<TaskWithTags>() }
    var taskName by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedTags = remember { mutableStateListOf<TagEntity>() }
    val coroutineScope = rememberCoroutineScope()
    var dueDateValue by remember { mutableStateOf("") }
    LaunchedEffect(listId) {
        taskViewModel.getTaskWithTags(listId).collect { items ->
            tasks.clear()
            tasks.addAll(items)
        }
    }

    LaunchedEffect(editTaskId) {
        if (editTaskId != -1) {
            Log.d("TaskScreen", "Loading task with taskId: $editTaskId")
            taskViewModel.getTaskWithTagsById(editTaskId).collect { taskWithTags ->
                Log.d("TaskScreen", "TaskWithTags: $taskWithTags")
                taskWithTags?.let {
                    taskName = it.task.taskName
                    note = it.task.note ?: ""
                    selectedTags.clear()
                    selectedTags.addAll(it.tags)
                    Log.d("Check selectedTag:", "$selectedTags")
                    dueDateValue = it.task.dueDate ?: ""
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            Icon(
                painter = painterResource(R.drawable.ic_add_task),
                tint = Color(0xFFFF6666),
                contentDescription = "icon add task",
                modifier = Modifier
                    .padding(bottom = 40.dp, end = 20.dp)
                    .clickable {
                        editTaskId = -1
                        taskName = ""
                        note = ""
                        selectedTags.clear()
                        isShowModalSheetAddTask = true
                    }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
        ) {
            tasks.forEach { taskWithTag ->
                if (taskWithTag != null) {
                    TaskItem(
                        taskWithTags = taskWithTag,
                        onDelete = {
                            taskViewModel.insertDeletedTask(
                                deletedTaskId = taskWithTag.task.taskId!!
                            )
                            Log.d("TaskScreen", "Delete task with taskId status: ${taskWithTag.task.taskId} ${taskWithTag.task.status}")
                        },
                        onEdit = {
                            editTaskId = taskWithTag.task.taskId ?: -1
                            isShowModalSheetAddTask = true
                        },
                        onToggle = { checked ->
                            taskViewModel.updateTaskStatus(
                                taskWithTag.task.taskId!!,
                                checked
                            )
                        }
                    )
                }
            }

        }

        if (isShowModalSheetAddTask) {
            ModalBottomSheet(
                onDismissRequest = {
                    taskName = ""
                    note = ""
                    dueDateValue = ""
                    selectedTags.clear()
                    isShowModalSheetAddTask = false
                    editTaskId = -1
                },
                modifier = Modifier
                    .padding(bottom = 40.dp)
            ) {
                Log.d("SelectedTag:", "${selectedTags.toList()}")
                AddTaskScreen(
                    taskViewModel,
                    taskName = taskName,
                    onTaskNameChange = { taskName = it },
                    note = note,
                    onNoteChange = { note = it },
                    listTags = selectedTags.toList(),
                    dueDate = dueDateValue,
                    onDueDateChange = { dueDate -> dueDateValue = dueDate },
                    onConfirm = { listTag ->
                        coroutineScope.launch {
                            if (editTaskId == -1) {
                                val taskId = taskViewModel.insertTask(
                                    TaskEntity(
                                        listId = listId,
                                        taskName = taskName,
                                        note = note,
                                        createdAt = CommonHelper.getCurrentDate(),
                                        dueDate = dueDateValue
                                    )
                                ).toInt()
                                listTag.forEach { tag ->
                                    taskViewModel.insertTaskTag(
                                        TaskTagEntity(
                                            taskId = taskId,
                                            tagId = tag.tagId!!
                                        )
                                    )
                                }
                            } else {
                                taskViewModel.updateTask(
                                    TaskEntity(
                                        taskId = editTaskId,
                                        listId = listId,
                                        taskName = taskName,
                                        note = note,
                                        createdAt = CommonHelper.getCurrentDate(),
                                        dueDate = dueDateValue
                                    )
                                )
                                taskViewModel.deleteTaskTagByTaskId(editTaskId)
                                listTag.forEach { tag ->
                                    taskViewModel.insertTaskTag(
                                        TaskTagEntity(
                                            taskId = editTaskId,
                                            tagId = tag.tagId!!
                                        )
                                    )
                                }
                            }
                            taskName = ""
                            note = ""
                            selectedTags.clear()
                            dueDateValue = ""
                            isShowModalSheetAddTask = false
                            editTaskId = -1
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun TaskItem(
    taskWithTags: TaskWithTags,
    onDelete: (TaskEntity) -> Unit,
    onEdit: (TaskEntity) -> Unit,
    onToggle: (Boolean) -> Unit,
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val maxSwipe = 300f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(0f to 0, -maxSwipe to 1),
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Red)
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val enabled = swipeableState.offset.value < -50f
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(28.dp)
                    .clickable(enabled = enabled) { onEdit(taskWithTags.task) }
            )
            Icon(
                painter = painterResource(R.drawable.ic_remove),
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(enabled = enabled) { onDelete(taskWithTags.task) }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .fillMaxSize()
                .background(Color.White)
                .padding(start = 20.dp)
        ) {
            Checkbox(
                checked = taskWithTags.task.status,
                onCheckedChange = { checked -> onToggle(checked) }
            )
            Text(
                text = taskWithTags.task.taskName,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            taskWithTags.tags.forEach { tag ->
                Text(
                    text = tag.tagName,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color(tag.tagColor), shape = RoundedCornerShape(4.dp))
                        .padding(4.dp)
                )
            }
        }
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .align(Alignment.BottomCenter)
            .padding(start = 20.dp, end = 20.dp)
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
fun TaskPreview() {
    TaskScreen(hiltViewModel(), rememberNavController(), -1)
}