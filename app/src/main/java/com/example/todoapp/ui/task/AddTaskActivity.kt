package com.example.todoapp.ui.task

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.R
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.ui.common.SelectedTagDialog
import com.example.todoapp.ui.common.TagItem
import com.example.todoapp.ui.common.TextFieldCustom
import com.example.todoapp.viewmodel.TaskViewModel

@Composable
fun AddTaskScreen(
    taskViewModel: TaskViewModel,
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit = {},
    listTags: List<TagEntity>,
    onConfirm: (List<TagEntity>) -> Unit,
) {
    Log.d("listTag: ", "$listTags")
    var isShowAddTagDialog by remember { mutableStateOf(false) }

    val selectedTagState = remember { mutableStateListOf<TagEntity>() }
    val tags = taskViewModel.getTags()
        .collectAsState(initial = listOf()).value.filter { tag -> !selectedTagState.contains(tag) }

    LaunchedEffect(listTags) {
        selectedTagState.addAll(listTags)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB58585))
        ) {
            Text(
                text = "Tên nhiệm vụ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .padding(start = 20.dp, top = 16.dp, bottom = 10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {
                TextFieldCustom(
                    value = taskName,
                    onValueChange = onTaskNameChange,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onConfirm(selectedTagState) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF),
                        contentColor = Color(0xFF891212)
                    )
                ) {
                    Text(
                        text = "Done"
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 30.dp, end = 40.dp, top = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_tag),
                    contentDescription = "icon tag",
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
                Column {
                    Text(
                        text = "Nhãn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                    Row(
                    ) {
                        if (selectedTagState.isNotEmpty()) {
                            selectedTagState.forEach { tag ->
                                TagItem(tag.tagName, color = Color(tag.tagColor))
                            }
                        } else {
                            Text(
                                text = "Chưa có tag nào được chọn"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.ic_add_task),
                    tint = Color(0xFF707070),
                    contentDescription = "add tag",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            isShowAddTagDialog = true
                        }
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFC7C7C7),
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_note),
                    contentDescription = "icon note",
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
                Column {
                    Text(
                        text = "Ghi chú",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                    TextFieldCustom(
                        value = note,
                        onValueChange = onNoteChange,
                        modifier = Modifier
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFC7C7C7),
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
            )
            Row(
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "Ngày đến hạn",
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
                Column {
                    Text(
                        text = "Ngày đến hạn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                    Text(
                        text = "Chưa đặt ngày đến hạn"
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFC7C7C7),
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
            )
            Row(
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bell),
                    contentDescription = "icon tag",
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
                Column {
                    Text(
                        text = "Loại nhắc nhở",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                    Text(
                        text = "Báo thức"
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFC7C7C7),
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
            )
        }
        if (isShowAddTagDialog) {
            SelectedTagDialog(
                listTag = tags,
                selectedListTag = selectedTagState,
                onDismiss = { isShowAddTagDialog = false },
                onConfirm = { isShowAddTagDialog = false },
                selectTag = { tag ->
                    selectedTagState.add(tag)
                },
                onRemoveTag = { tag ->
                    selectedTagState.remove(tag)
                }
            )
        }
    }
}

@Preview
@Composable
fun AddTaskScreenPreview() {
    AddTaskScreen(hiltViewModel(), "", {}, "", {}, listOf(), {})
}