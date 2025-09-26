package com.example.todoapp.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import kotlinx.coroutines.delay
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavBackStackEntry
import com.example.todoapp.helper.DataStoreHelper
import kotlinx.coroutines.launch

@Suppress("DefaultLocale")
@Composable
fun CountdownTimerScreen(
    activity: ComponentActivity? = null,
    navBackStackEntry: NavBackStackEntry? = null,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var totalTime by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var isRinging by remember { mutableStateOf(false) }
    var triggerTime by remember { mutableLongStateOf(0L) }
    var timeLeft by remember { mutableLongStateOf(totalTime) }
    var hourInput by remember { mutableStateOf("0") }
    var minuteInput by remember { mutableStateOf("0") }

    LaunchedEffect(isRunning, isRinging, triggerTime) {
        DataStoreHelper.saveIsRinging(context, isRinging)
        DataStoreHelper.saveIsRunning(context, isRunning)
        isRunning = DataStoreHelper.getIsRunning(context)
        isRinging = DataStoreHelper.getIsRinging(context)
        triggerTime = DataStoreHelper.getTriggerTime(context)
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(navBackStackEntry) {
        val ringingFromArgs = navBackStackEntry?.arguments?.getBoolean("isRinging") ?: false
        Log.d("CountdownTimerScreen", "isRinging from NavArgs: $ringingFromArgs")
        if (ringingFromArgs) {
            isRinging = true
        }

        activity?.intent?.getBooleanExtra("isRinging", false)?.let { ringingFromIntent ->
            if (ringingFromIntent && !isRinging) {
                isRinging = true
                Log.d("CountdownTimerScreen", "isRinging from Intent: $ringingFromIntent")
            }
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        100
                    )
                } ?: permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0 && isRunning) {
                delay(1000L)
                timeLeft -= 1000L
            }
            if (timeLeft <= 0 && isRunning) {
                isRunning = false
                isRinging = true
            }
        }
    }


    val progress = remember(timeLeft, totalTime) {
        if (totalTime > 0) timeLeft.toFloat() / totalTime else 0f
    }

    val formattedTime = remember(timeLeft) {
        val hours = (timeLeft / 1000) / 3600
        val minutes = ((timeLeft / 1000) % 3600) / 60
        val seconds = (timeLeft / 1000) % 60
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        triggerTime = System.currentTimeMillis() + timeLeft
        isRunning = true
        isRinging = false
        coroutineScope.launch {
            DataStoreHelper.saveTriggerTime(context, triggerTime)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun cancelAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        isRunning = false
        isRinging = false
        coroutineScope.launch {
            DataStoreHelper.saveTriggerTime(context, 0L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.mountain),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isRunning && !isRinging) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = hourInput,
                        onValueChange = {
                            hourInput = it
                            val minutes = it.toLongOrNull() ?: 0L
                            totalTime = minutes * 60 * 1000
                            timeLeft = totalTime
                        },
                        label = { Text("Phút", color = Color.White) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color.LightGray,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.width(120.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    OutlinedTextField(
                        value = minuteInput,
                        onValueChange = {
                            minuteInput = it
                            val seconds = it.toLongOrNull() ?: 0L
                            totalTime += seconds * 1000
                            timeLeft = totalTime
                        },
                        label = { Text("Giây", color = Color.White) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color.LightGray,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.width(120.dp)
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(280.dp)) {
                    drawArc(
                        color = Color.DarkGray,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 16f, cap = StrokeCap.Round),
                        size = Size(size.width, size.height)
                    )
                    drawArc(
                        color = Color.Red,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = 16f, cap = StrokeCap.Round),
                        size = Size(size.width, size.height)
                    )
                }
                Text(text = formattedTime, color = Color.White, fontSize = 36.sp)
            }

            Spacer(Modifier.height(32.dp))

            Row {
                if (isRinging) {
                    Button(
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),
                            contentColor = Color(0xFF000000)
                        ),
                        onClick = {
                            val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
                                action = AlarmReceiver.ACTION_STOP_ALARM
                            }
                            context.sendBroadcast(stopIntent)
                            isRinging = false
                            timeLeft = totalTime
                            cancelAlarm()
                            coroutineScope.launch {
                                DataStoreHelper.saveIsRinging(context, false)
                                DataStoreHelper.saveTriggerTime(context, 0L)
                            }
                            Log.d("CountdownTimerScreen", "Stop alarm button clicked")
                        }
                    ) {
                        Text("Tắt chuông")
                    }
                } else {
                    Button(
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),
                            contentColor = Color(0xFF000000)
                        ),
                        onClick = {
                            if (timeLeft > 0) {
                                isRunning = !isRunning
                                if (isRunning) {
                                    setAlarm()
                                } else {
                                    cancelAlarm()
                                }
                            }
                        }
                    ) {
                        Text(if (isRunning) "Pause" else "Start")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),
                            contentColor = Color(0xFF000000)
                        ),
                        onClick = {
                            isRunning = false
                            timeLeft = totalTime
                            cancelAlarm()
                        }) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CountDownPreview() {
    CountdownTimerScreen()
}