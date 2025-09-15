package com.example.todoapp.ui.main

import android.util.Log
import com.example.todoapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.example.todoapp.database.entities.ProjectEntity
import com.example.todoapp.helper.DataStoreHelper
import com.example.todoapp.ui.common.DialogCustom
import com.example.todoapp.ui.common.OperationCustom
import com.example.todoapp.ui.drawer.DrawerScreen
import com.example.todoapp.viewmodel.ListViewModel
import com.example.todoapp.viewmodel.ProjectViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(projectViewModel: ProjectViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var newProjectName by remember { mutableStateOf("") }
    var isShowAddProjectDialog by remember { mutableStateOf(false) }
    val listProject = projectViewModel.projects.collectAsState(initial = emptyList())
    val context = LocalContext.current
    var isShowEditProjectDialog by remember { mutableStateOf(false) }
    var projectEdit by remember { mutableStateOf<ProjectEntity?>(null) }
    val navController = rememberNavController()

    Log.d("HomeScreen", "ListProject: ${listProject.value}")
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerScreen(
                projectSection = {
                    listProject.value.forEach { project ->
                        OperationCustom(
                            icon = R.drawable.ic_list,
                            text = project.projectName,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .combinedClickable(
                                    onLongClick = {
                                        projectEdit = project
                                        isShowEditProjectDialog = true
                                    },
                                    onClick = {
                                        coroutineScope.launch {
                                            DataStoreHelper.saveCurrentProjectId(context, project.projectId!!)
                                            navController.navigate("projectView/${project.projectId}")
                                            drawerState.close()
                                        }
                                    }
                                )
                        )
                    }
                },
                onAddNewProject = {
                    coroutineScope.launch {
                        isShowAddProjectDialog = true
                    }
                },
                onProjectViewByTag = {
                    coroutineScope.launch {
                        navController.navigate("projectViewByTag")
                        drawerState.close()
                    }
                },
                onProjectViewByDueDate = {
                    coroutineScope.launch {
                        navController.navigate("projectViewByDueDate")
                        drawerState.close()
                    }
                },
                onPomodoroMode = {
                    coroutineScope.launch {
                        navController.navigate("pomodoroMode")
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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost(navController = navController, graph = NavGraph(navController))
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
                    projectViewModel.insertProject(
                        ProjectEntity(
                            projectName = newProjectName
                        )
                    )
                    newProjectName = ""
                    isShowAddProjectDialog = false
                }
            )
        }
        if (isShowEditProjectDialog) {
            DialogCustom(
                title = "Chỉnh sửa danh sách",
                label = "Tên danh sách",
                value = projectEdit?.projectName ?: "",
                onValueChange = { newValue ->
                    projectEdit?.let {
                        projectEdit = it.copy(projectName = newValue)
                    }
                },
                onDelete = {
                    Text(
                        text = "Xóa",
                        fontSize = 14.sp,
                        color = Color(0xFFE40000),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                                projectViewModel.deleteProject(projectEdit?.projectId ?: -1)
                                isShowEditProjectDialog = false
                            }
                    )
                },
                onDismiss = {
                    isShowEditProjectDialog = false
                },
                onConfirm = {
                    projectViewModel.updateProject(projectEdit!!)
                    isShowEditProjectDialog = false
                }
            )
        }
    }
}

@Composable
fun NavGraph(navController: NavController): NavGraph {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val listViewModel: ListViewModel = hiltViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    return navController.createGraph("pomodoroMode") {
        composable(
            route = "projectView/{projectId}",
            arguments = listOf(
                navArgument("projectId") { type = NavType.IntType}
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId") ?: -1
            ProjectViewScreen(projectViewModel, listViewModel, navController, projectId)
        }
        composable("projectViewByTag") {
            ProjectViewByTagScreen()
        }
        composable("projectViewByDueDate") {
            ProjectDueDateScreen()
        }
        composable("pomodoroMode") {
            CountDownScreen()
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(projectViewModel = hiltViewModel())
}