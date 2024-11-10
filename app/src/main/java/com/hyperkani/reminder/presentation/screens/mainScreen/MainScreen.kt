package com.hyperkani.reminder.presentation.screens.mainScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hyperkani.reminder.R
import com.hyperkani.reminder.data.model.NotificationPeriod
import com.hyperkani.reminder.data.model.ReminderItem
import com.hyperkani.reminder.data.scheduler.AlarmScheduler
import com.hyperkani.reminder.data.scheduler.AlarmSchedulerImpl
import com.hyperkani.reminder.presentation.components.ApproveButton
import com.hyperkani.reminder.presentation.components.CancelButton
import com.hyperkani.reminder.presentation.navigation.Screens
import com.hyperkani.reminder.presentation.utils.toDateString
import com.hyperkani.reminder.presentation.utils.toTimeString
import java.time.LocalDateTime


@Composable
fun MainScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val reminders by viewModel.reminders.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setReminders()
    }
    MainScreenContent(
        reminders,
        {
            viewModel.setNewReminder(it)
        },
        {
            navHostController.navigate("${Screens.ADD_REMINDER_SCREEN.route}/${-1}")
        },
        {
            viewModel.deleteReminder(it)
        },
        {
            navHostController.navigate("${Screens.ADD_REMINDER_SCREEN.route}/$it")
        })
}

@Composable
fun MainScreenContent(
    reminders: List<ReminderItem>,
    updateReminder: (ReminderItem) -> Unit,
    onAddReminder: () -> Unit,
    onDelete: (ReminderItem) -> Unit,
    onEdit: (id: Long) -> Unit
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)

    val screenWidthPx = configuration.screenWidthDp
    val screenHeightPx = configuration.screenHeightDp

    if (reminders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            SetCircles(screenWidthPx, screenHeightPx)
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(22.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.main_screen_title),
                        fontSize = 18.sp,
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Button(
                            onClick = { onAddReminder() },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.Black,
                                containerColor = colorResource(id = R.color.add_task_button_container_color),
                                disabledContentColor = Color.White,
                                disabledContainerColor = Color.Black
                            ),
                            border = BorderStroke(
                                1.dp,
                                colorResource(id = R.color.add_task_button_border_color)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            modifier = Modifier
                                .height(20.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.add_task_button_text),
                                color = colorResource(id = R.color.add_task_button_text_color)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.mipmap.ic_add),
                            contentDescription = stringResource(R.string.add_button_desc),
                            modifier = Modifier
                                .size(30.dp)
                                .offset((-45).dp)
                                .clickable {
                                    onAddReminder()
                                }
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = stringResource(R.string.arrow_desc),
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(22.dp))


                Spacer(modifier = Modifier.height(33.dp))

            }

        }
        HorizontalDivider(color = Color.Black, modifier = Modifier.offset(y = 80.dp))
        if (reminders.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                Image(
                    painter = painterResource(R.mipmap.main_screen_base_image),
                    contentDescription = stringResource(R.string.base_image_desc),
                    modifier = Modifier.size(250.dp)
                )
                Text(
                    text = stringResource(R.string.add_task_hint_text),
                    color = colorResource(R.color.secondary_text_color),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    } else {

        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.main_screen_title),
                    fontSize = 18.sp,
                )
                Image(
                    painter = painterResource(id = R.mipmap.ic_add),
                    contentDescription = stringResource(R.string.add_button),
                    modifier = Modifier
                        .size(30.dp)
                        .offset(x = (-20).dp)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            onAddReminder()
                        }
                )


            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Black)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reminders) { task ->
                    TaskItemCard(alarmScheduler, updateReminder, task, onDelete, onEdit)
                }
            }
        }
    }
}

@Composable
fun SetCircles(screenWidthPx: Int, screenHeightPx: Int) {

    Circle(
        colorId = R.color.dark_circle_color,
        modifier = Modifier.offset(
            (screenWidthPx / 10).dp, (screenHeightPx / 4).dp
        )
    )
    Circle(
        colorId = R.color.light_circle_color,
        modifier = Modifier
            .offset(
                (-40).dp, (screenHeightPx / 4 + 100).dp

            )
            .scale(1.3f)
    )
    Circle(
        colorId = R.color.dark_circle_color,
        modifier = Modifier.offset(
            (screenWidthPx / 10).dp * 7, (screenHeightPx / 5).dp * 4
        )
    )

    Circle(
        colorId = R.color.light_circle_color,
        modifier = Modifier
            .offset(
                (screenWidthPx - 50).dp, (screenHeightPx / 5).dp * 3 + 50.dp
            )
            .scale(1.3f)
    )

    Circle(
        colorId = R.color.dark_circle_color,
        modifier = Modifier
            .offset(
                (-30).dp, (screenHeightPx / 5).dp * 4
            )
            .scale(0.8f)
    )

    Circle(
        colorId = R.color.light_circle_color,
        modifier = Modifier
            .offset(
                (-40).dp, (screenHeightPx - 40).dp
            )
            .scale(1.3f)
    )
    Circle(
        colorId = R.color.dark_circle_color,
        modifier = Modifier
            .offset(
                50.dp, (screenHeightPx - 60).dp
            )
    )

}

@Composable
fun Circle(colorId: Int, modifier: Modifier) {
    val color = colorResource(id = colorId)
    Box(
        modifier = modifier
            .size(80.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val radius = size.minDimension / 2
            val innerRadius = radius / 2

            drawCircle(
                color = color,
                radius = radius
            )

            drawCircle(
                color = Color.White,
                radius = innerRadius
            )
        }
    }
}

@Composable
fun TaskItemCard(
    alarmScheduler: AlarmScheduler,
    updateReminder: (ReminderItem) -> Unit,
    reminderItem: ReminderItem,
    onDelete: (ReminderItem) -> Unit,
    onEdit: (id: Long) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    fun cancelReminder() {
        alarmScheduler.cancel(item = reminderItem)
        if (reminderItem.period == NotificationPeriod.ONCE) {
            onDelete(reminderItem)
        } else {
            val newItem = ReminderItem.getReminderWithUpdateTime(reminderItem)
            updateReminder(newItem)
        }
    }
    Card(
        colors = CardColors(
            contentColor = Color.White,
            containerColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.White
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                spotColor = Color.Blue
            )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .blur(if (expanded) 2.dp else 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = reminderItem.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reminderItem.time.toDateString(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.mipmap.ic_overflow_menu),
                    contentDescription = stringResource(R.string.overflow_menu_desc),
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { expanded = true }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },

                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(border = BorderStroke(1.dp, Color.Black), RoundedCornerShape(12.dp))
                        .background(Color.White)

                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.edit_button_text)) },
                        onClick = {
                            onEdit(reminderItem.id)
                            expanded = false
                        }
                    )
                    HorizontalDivider(color = Color.Black, thickness = 1.dp)
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.delete_button_text), color = Color.Red)
                        },
                        onClick = {
                            onDelete(reminderItem)
                            expanded = false
                        }
                    )
                }
            }

            Column {

                Text(
                    text = reminderItem.time.toTimeString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    ApproveButton(modifier = Modifier) {
                        cancelReminder()
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    CancelButton(modifier = Modifier) {
                        cancelReminder()
                    }
                }
            }
        }
    }
}

@Composable
@Preview
    (
    heightDp = 800,
    widthDp = 400
)
fun MainScreenPreview() {

    MainScreenContent(
        listOf(
            ReminderItem(
                0L,
                "Yeeeeeees",
                LocalDateTime.now(),
                NotificationPeriod.ONCE
            )
        ), {}, {}, {}, {})
}