package com.hyperkani.reminder.presentation.screens.addReminder

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hyperkani.reminder.R
import com.hyperkani.reminder.data.model.NotificationPeriod
import com.hyperkani.reminder.data.model.ReminderItem
import com.hyperkani.reminder.presentation.components.ApproveButton
import com.hyperkani.reminder.presentation.components.CancelButton
import com.hyperkani.reminder.presentation.utils.toDateString
import com.hyperkani.reminder.presentation.utils.toTimeString
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun AddReminderScreen(
    navHostController: NavHostController,
    id: Long,
    viewModel: AddReminderViewModel = hiltViewModel()
) {

    val reminderItem by viewModel.reminderItem.collectAsState()

    LaunchedEffect(Unit) {
        if (id >= 0)
            viewModel.setReminder(id)
    }
    AddReminderScreenContent(
        reminderItem,
        {
            navHostController.navigateUp()
        },
        { viewModel.addReminder(it) },
        {
            viewModel.cancelReminder(it)
        })
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreenContent(
    reminderItem: ReminderItem,
    onBack: () -> Unit,
    addReminder: (ReminderItem) -> Unit,
    cancelReminder: (ReminderItem) -> Unit
) {
    val context = LocalContext.current
    val options = NotificationPeriod.entries

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }

    var title by remember { mutableStateOf(reminderItem.title) }
    var time by remember { mutableStateOf(reminderItem.time.toTimeString()) }
    var date by remember { mutableStateOf(reminderItem.time.toDateString()) }

    LaunchedEffect(reminderItem) {
        title = reminderItem.title
        time = reminderItem.time.toTimeString()
        date = reminderItem.time.toDateString()
    }
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            time = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = String.format("%02d:%02d:%04d", dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CancelButton(Modifier) { onBack() }
            Text(
                text = stringResource(R.string.main_screen_title),
                fontSize = 32.sp,
            )
            ApproveButton(modifier = Modifier) {


                convertToLocalDateTime(date, time)?.let {
                    val item = ReminderItem(
                        reminderItem.id,
                        title,
                        it,
                        selectedOption
                    )
                    cancelReminder(reminderItem)
                    addReminder(item)

                    onBack()
                }
            }
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.15f))

        Box {
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
                        spotColor = Color.Black
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(28.dp))
                    Column {
                        Row {
                            Text(
                                text = stringResource(R.string.title_placeholder),
                                fontSize = 21.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Image(
                                painter = painterResource(id = R.drawable.ic_pen),
                                contentDescription = stringResource(R.string.pen_icon_desc)
                            )
                        }

                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.insert_title_placeholder)) },
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.LightGray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row {
                                Text(text = stringResource(R.string.time), fontSize = 21.sp, color = Color.Black)
                                Spacer(modifier = Modifier.width(5.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_time),
                                    contentDescription = stringResource(R.string.clock_icon_desc)
                                )
                            }

                            Text(
                                text = time,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { timePickerDialog.show() },
                                color = Color.Gray,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row {
                                Text(text = stringResource(R.string.date), fontSize = 21.sp, color = Color.Black)
                                Spacer(modifier = Modifier.width(5.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_calendar),
                                    contentDescription = stringResource(R.string.calendar_icon_desc)
                                )
                            }
                            Text(
                                text = date,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable
                                    { datePickerDialog.show() },
                                color = Color.Gray,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Text(text = stringResource(R.string.repeat), fontSize = 21.sp, color = Color.Black)
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_repeat),
                            contentDescription = stringResource(R.string.repeat_icon_desc)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Gray),
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedOption.toString(),
                            textStyle = TextStyle(color = Color.Gray, fontSize = 14.sp),
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor(),
                            trailingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_dropdown_menu_button),
                                        contentDescription = stringResource(R.string.dropdown_icon_desc),
                                    )
                                }
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = colorResource(id = R.color.period_menu_background),
                                focusedIndicatorColor = colorResource(id = R.color.period_menu_background),
                                unfocusedIndicatorColor = colorResource(id = R.color.period_menu_background)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.period_menu_background)),
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                        options.forEach { option ->
                                DropdownMenuItem(
                                    colors = MenuDefaults.itemColors(

                                    ),
                                    text = { Text(text = option.toString(), color = Color.Gray) },
                                    onClick = {
                                        selectedOption = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Text(
                text = stringResource(R.string.title_add_text),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(
                        color = Color(0xFF2196F3),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .align(Alignment.TopCenter)
                ,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


fun convertToLocalDateTime(date: String, time: String): LocalDateTime? {
    return try {
        val dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val localDate = LocalDate.parse(date, dateFormatter)
        val localTime = LocalTime.parse(time, timeFormatter)

        LocalDateTime.of(localDate, localTime)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Preview
@Composable
fun AddReminderScreenPreview() {
    AddReminderScreenContent(
        ReminderItem(0L, "", LocalDateTime.now(), NotificationPeriod.ONCE),
        {},
        {},{})
}
