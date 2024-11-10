package com.hyperkani.reminder.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyperkani.reminder.R


@Composable
fun ApproveButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.approve_ic_background),
            contentDescription = stringResource(R.string.background_desc),
            modifier = Modifier.size(38.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_approve),
            contentDescription = stringResource(R.string.cancel_desc),
            modifier
                .size(20.dp)
                .clickable {
                    onClick()
                }
        )
    }

}