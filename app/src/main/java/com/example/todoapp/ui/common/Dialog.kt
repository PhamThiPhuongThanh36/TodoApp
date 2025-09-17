package com.example.todoapp.ui.common

import android.nfc.Tag
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColor
import com.example.todoapp.R
import com.example.todoapp.database.entities.TagEntity
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.PaletteContentScale
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun DialogCustom(
    title: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDelete: @Composable () -> Unit = {},
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
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
                    .background(
                        Color(0xFFBD6C6C),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
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
            TextFieldCustom(
                leadingIcon = {},
                trailingIcon = {},
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally)
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

@Composable
fun TagDialogCustom(
    newTag: String,
    onNewTagChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (Color) -> Unit,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        val controller = rememberColorPickerController()
        var selectedColor by remember { mutableStateOf(Color.Red) }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFBD6C6C),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp)
            ) {
                Text(
                    text = "Thêm nhãn mới",
                    color = Color(0xFFFFFFFF),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.ic_cancel),
                    tint = Color(0xFFFFFFFF),
                    contentDescription = "cancel",
                    modifier = Modifier
                        .clickable { onDismiss() }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "Tên nhãn mới",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                ) {
                    TextFieldCustom(
                        value = newTag,
                        onValueChange = onNewTagChange,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD78A8A)
                        ),
                        onClick = { onConfirm(selectedColor) }
                    ) {
                        Text(
                            text = "Lưu"
                        )
                    }
                }
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 10.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        selectedColor = colorEnvelope.color
                    }
                )
            }
        }
    }
}

@Composable
fun SelectedTagDialog(
    listTag: List<TagEntity>,
    selectedListTag: List<TagEntity>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    selectTag: (TagEntity) -> Unit,
    onRemoveTag: (TagEntity) -> Unit,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFFFFFFF), RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFBD6C6C),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(start = 20.dp, top = 15.dp, bottom = 15.dp)
            ) {
                Text(
                    text = "Chọn nhãn",
                    color = Color(0xFFFFFFFF),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "Danh sách nhãn được chọn",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    maxItemsInEachRow = 3,
                    modifier = Modifier
                        .padding(top = 10.dp)
                ) {
                    selectedListTag.forEach { tag ->
                        TagItem(
                            value = tag.tagName,
                            color = Color(tag.tagColor),
                            modifier = Modifier
                                .clickable {
                                    onRemoveTag(tag)
                                }
                        )
                    }
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF),
                    ),
                    border = BorderStroke(1.dp, color = Color(0xFFB7B7B7)),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(0.9f)
                        .heightIn(20.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        maxItemsInEachRow = 3,
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        listTag.forEach { tag ->
                            TagItem(
                                value = tag.tagName,
                                color = Color(tag.tagColor),
                                modifier = Modifier
                                    .clickable {
                                        selectTag(tag)
                                    }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Hủy",
                        color = Color(0xFFE40000),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                onDismiss()
                            }
                    )
                    Text(
                        text = "Lưu",
                        color = Color(0xFFE40000),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {
                                onConfirm()
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun TagItem(value: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = value,
        color = Color(0xFFFFFFFF),
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(5.dp)
            .background(color, shape = RoundedCornerShape(4.dp))
            .padding(5.dp)
    )
}

@Preview
@Composable
fun DialogCustomPreview() {
//    DialogCustom("Chỉnh sửa danh sách", "Tên danh sách", "", {}, {}, {}, {})
//    TagDialogCustom("", {}, {}, {})
//    SelectedTagDialog(listOf(), listOf(), {}, {})
    TagItem(
        "hehe",
        Color(0xFFD78A8A)
    )
}