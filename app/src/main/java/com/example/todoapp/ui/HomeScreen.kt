package com.example.todoapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.common.DialogCustom
import com.example.todoapp.ui.drawer.DrawerScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var newProjectName by remember { mutableStateOf("") }
    var isShowAddProjectDialog by remember { mutableStateOf(false) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerScreen(
                projectSection = {

                },
                onAddNewProject = {
                    coroutineScope.launch {
                        isShowAddProjectDialog = true
                    }
                },
                onEditProject = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onProjectViewByTag = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onProjectViewByDueDate = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onPomodoroMode = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onTag = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onMoveProject = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onDeletedProjectView = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            )
        },
        gesturesEnabled = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

        }
        if (isShowAddProjectDialog) {
            DialogCustom(
                title = "Thêm mới danh sách",
                label = "Tên danh sách",
                value = newProjectName,
                onValueChange = {
                    newProjectName = it
                },
                onDelete = {},
                onDismiss = {
                    isShowAddProjectDialog = false
                },
                onConfirm = {
                    isShowAddProjectDialog = false
                }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}