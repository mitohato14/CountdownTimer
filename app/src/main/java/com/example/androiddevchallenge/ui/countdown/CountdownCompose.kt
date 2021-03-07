package com.example.androiddevchallenge.ui.countdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Created by mitohato14 on 2021/03/06.
 */

@Composable
fun CountdownTimerCompose() {
    val viewModel: CountdownViewModel = viewModel()
    
    val minute by viewModel.settingMinute.observeAsState()
    val second by viewModel.settingSecond.observeAsState()
    
    ConstraintLayout {
        val (timerSet, controller) = createRefs()
        TimerSet(
            minute = minute ?: "",
            onMinuteChange = viewModel::onSettingMinuteChange,
            second = second ?: "",
            onSecondChange = viewModel::onSettingSecondChange,
            modifier = Modifier.constrainAs(timerSet) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        )
        if (viewModel.countStarted) {
            ControllerCounting(
                onPauseClick = viewModel::onPauseTimer,
                onStopClick = viewModel::onStopTimer
            )
        } else {
            ControllerBeforeCount(
                onStartClick = viewModel::onClickStart,
                modifier = Modifier.constrainAs(controller) {
                    top.linkTo(timerSet.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
fun ControllerBeforeCount(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        ControllerButton(
            text = "Start",
            color = MaterialTheme.colors.primary,
            onClick = onStartClick
        )
    }
}

@Composable
fun ControllerCounting(
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        ControllerButton(
            text = "Pause",
            color = MaterialTheme.colors.primaryVariant,
            onClick = onPauseClick
        )
        Spacer(modifier = Modifier.width(20.dp))
        ControllerButton(
            text = "Stop",
            color = MaterialTheme.colors.secondary,
            onClick = onStopClick
        )
    }
}

@Composable
fun ControllerButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button.copy(
                color = contentColorFor(backgroundColor = color)
            )
        )
    }
}

@Composable
fun TimerSet(
    minute: String,
    onMinuteChange: (String) -> Unit,
    second: String,
    onSecondChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TimerTextField(
            text = minute,
            onTextChange = onMinuteChange
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.body1.copy(
                fontSize = 30.sp
            )
        )
        TimerTextField(
            text = second,
            onTextChange = onSecondChange
        )
    }
}

@Composable
fun TimerTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .size(70.dp)
            .background(Color.Transparent),
        textStyle = MaterialTheme.typography.body1.copy(
            fontSize = 30.sp
        )
    )
}

@Preview
@Composable
fun EditTimerPreview() {
    TimerSet(
        minute = String.format("%02d", 1),
        onMinuteChange = {},
        second = String.format("%02d", 20),
        onSecondChange = {}
    )
}

@Preview
@Composable
fun ControllerButtonPreview() {
    ControllerButton(
        text = "start",
        color = Color.Cyan,
        onClick = { /*TODO*/ }
    )
}

@Preview
@Composable
fun ControllerBeforeCountCompose() {
    ControllerBeforeCount(
        onStartClick = {}
    )
}

@Preview
@Composable
fun ControllerCountingCompose() {
    ControllerCounting(
        onPauseClick = {},
        onStopClick = {}
    )
}
