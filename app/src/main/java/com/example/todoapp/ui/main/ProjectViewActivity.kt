package com.example.todoapp.ui.main

import com.example.todoapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.database.entities.ListEntity
import com.example.todoapp.database.entities.ProjectEntity
import com.example.todoapp.model.List
import com.example.todoapp.ui.common.DialogCustom
import com.example.todoapp.viewmodel.ListViewModel
import com.example.todoapp.viewmodel.ProjectViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@Composable
fun ProjectViewScreen(projectViewModel: ProjectViewModel, listViewModel: ListViewModel, navController: NavController, projectId: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .statusBarsPadding()
    ) {
        val coroutineScope = rememberCoroutineScope()
        val currentProject = projectViewModel.getProjectById(projectId).collectAsState(initial = null)
        val currentList = listViewModel.getListsByProjectId(projectId).collectAsState(initial = emptyList())
        val lists = remember { mutableStateListOf<List?>() }
        val pagerState = rememberPagerState( pageCount = { lists.size } )
        var isShowDialogAddList by remember { mutableStateOf(false) }
        var newList by remember { mutableStateOf("") }
        var version by remember { mutableStateOf(0) }
        fun getLists() {
            lists.clear()
            currentList.value.map {
                List(it.listName)
            }.let { lists.addAll(it) }
        }
        LaunchedEffect( version, currentList.value ) {
            getLists()
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFF4343),
                            Color(0xFFFF6C6C),
                            Color(0xFFFF7E7E)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_list2),
                        contentDescription = "icon list",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier
                            .padding(start = 25.dp, end = 10.dp)
                            .size(45.dp)
                    )
                    Text(
                        text = currentProject.value?.projectName ?: "",
                        fontSize = 24.sp,
                        color = Color(0xFFFFFFFF),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (lists.isNotEmpty()) {
                        ScrollableTabRow(
                            selectedTabIndex = pagerState.currentPage,
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            lists.forEachIndexed { index, list ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    text = {
                                        Text(
                                            list?.listName ?: "",
                                            maxLines = 1,
                                        )
                                    },
                                    modifier = Modifier
                                        .width(100.dp)
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Icon(
                        painter = painterResource(R.drawable.ic_add_project),
                        contentDescription = "add list",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .size(30.dp)
                            .clickable {
                                isShowDialogAddList = true
                            }
                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
        ) {
            page ->
                TaskScreen()
        }
        if (isShowDialogAddList) {
            DialogCustom(
                title = "Thêm list mới",
                label = "Tên list",
                value = newList,
                onValueChange = { newList = it},
                onDelete = {},
                onDismiss = {
                    isShowDialogAddList = false
                },
                onConfirm = {
                    listViewModel.insertList(
                        ListEntity(
                            projectId = projectId,
                            listName = newList
                        )
                    )
                    newList = ""
                    version ++
                    isShowDialogAddList = false
                }
            )
        }
    }
}


@Preview
@Composable
fun ProjectViewPreview() {
    ProjectViewScreen(hiltViewModel(), hiltViewModel(),  rememberNavController(), -1)
}