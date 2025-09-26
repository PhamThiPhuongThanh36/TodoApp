package com.example.todoapp.ui.main

import android.util.Log
import androidx.activity.ComponentActivity
import com.example.todoapp.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.todoapp.database.entities.ProjectEntity
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.helper.DataStoreHelper
import com.example.todoapp.ui.alarm.CountdownTimerScreen
import com.example.todoapp.ui.alarm.ProjectViewByDueDate
import com.example.todoapp.ui.common.DialogCustom
import com.example.todoapp.ui.common.OperationCustom
import com.example.todoapp.ui.common.TagDialogCustom
import com.example.todoapp.ui.drawer.DrawerScreen
import com.example.todoapp.ui.task.TaskViewByTagScreen
import com.example.todoapp.viewmodel.ListViewModel
import com.example.todoapp.viewmodel.ProjectViewModel
import com.example.todoapp.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    projectViewModel: ProjectViewModel,
    taskViewModel: TaskViewModel,
    activity: ComponentActivity
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var newProjectName by remember { mutableStateOf("") }
    var newTagName by remember { mutableStateOf("") }
    var isShowAddProjectDialog by remember { mutableStateOf(false) }
    var isShowAddTagDialog by remember { mutableStateOf(false) }
    var isShowEditProjectDialog by remember { mutableStateOf(false) }
    var projectEdit by remember { mutableStateOf<ProjectEntity?>(null) }

    val listProject by projectViewModel.projects.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val navController = rememberNavController()

    var currentProjectId by remember { mutableStateOf(-1) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val savedId = DataStoreHelper.getCurrentProjectId(context)

        currentProjectId = when {
            savedId != -1 && listProject.any { it.projectId == savedId } -> savedId
            listProject.isNotEmpty() -> {
                val firstId = listProject.first().projectId ?: -1
                if (firstId != -1) {
                    DataStoreHelper.saveCurrentProjectId(context, firstId)
                }
                firstId
            }
            else -> -1
        }
        isLoading = false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerScreen(
                projectSection = {
                    listProject.forEach { project ->
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
                                            project.projectId?.let {
                                                DataStoreHelper.saveCurrentProjectId(context, it)
                                                currentProjectId = it
                                            }
                                            navController.navigate("projectView") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                            }
                                            drawerState.close()
                                        }
                                    }
                                )
                        )
                    }
                },
                onAddNewProject = { isShowAddProjectDialog = true },
                onProjectViewByTag = {
                    coroutineScope.launch {
                        navController.navigate("taskViewByTag")
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
                        navController.navigate("countdown")
                        drawerState.close()
                    }
                },
                onTag = { isShowAddTagDialog = true },
                onDeletedProjectView = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        },
        gesturesEnabled = true
    ) {
        Box(Modifier.fillMaxSize()) {
            if (isLoading) {
                Text("Loading...", modifier = Modifier.align(Alignment.Center))
            } else {
                NavHost(
                    navController = navController,
                    graph = NavGraph(navController, currentProjectId, activity)
                )
            }
        }

        if (isShowAddProjectDialog) {
            DialogCustom(
                title = "Thêm mới danh sách",
                label = "Tên danh sách",
                value = newProjectName,
                onValueChange = { newProjectName = it },
                onDelete = {},
                onDismiss = { isShowAddProjectDialog = false },
                onConfirm = {
                    projectViewModel.insertProject(ProjectEntity(projectName = newProjectName))
                    newProjectName = ""
                    isShowAddProjectDialog = false
                }
            )
        }

        if (isShowEditProjectDialog && projectEdit != null) {
            DialogCustom(
                title = "Chỉnh sửa danh sách",
                label = "Tên danh sách",
                value = projectEdit?.projectName ?: "",
                onValueChange = { newValue ->
                    projectEdit = projectEdit?.copy(projectName = newValue)
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
                onDismiss = { isShowEditProjectDialog = false },
                onConfirm = {
                    projectEdit?.let { projectViewModel.updateProject(it) }
                    isShowEditProjectDialog = false
                }
            )
        }

        if (isShowAddTagDialog) {
            TagDialogCustom(
                newTag = newTagName,
                onNewTagChange = { newTagName = it },
                onDismiss = { isShowAddTagDialog = false },
                onConfirm = { color ->
                    taskViewModel.insertTag(
                        TagEntity(tagName = newTagName, tagColor = color.toArgb().toLong())
                    )
                    newTagName = ""
                    isShowAddTagDialog = false
                }
            )
        }
    }
}

@Composable
fun NavGraph(
    navController: NavController,
    currentProjectId: Int,
    activity: ComponentActivity
): NavGraph {
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val listViewModel: ListViewModel = hiltViewModel()
    val taskViewModel: TaskViewModel = hiltViewModel()
    val context = LocalContext.current

    val startDest = if (currentProjectId != -1) "projectView" else "emptyProject"

    return navController.createGraph(startDestination = startDest) {
        composable("projectView") {
            ProjectViewScreen(projectViewModel, listViewModel, taskViewModel, navController)
        }
        composable("emptyProject") { EmptyProject() }
        composable("taskViewByTag") { TaskViewByTagScreen(taskViewModel) }
        composable("projectViewByDueDate") { ProjectViewByDueDate(taskViewModel) }
        composable(
            route = "countdown?isRinging={isRinging}",
            arguments = listOf(
                navArgument("isRinging") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "com.example.todoapp://countdown?isRinging={isRinging}"
                }
            )
        ) { backStackEntry ->
            CountdownTimerScreen(activity = activity, navBackStackEntry = backStackEntry)
        }
    }
}
