/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.countdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (timerSet, circular, controller) = createRefs()

        val countStarted: Boolean by viewModel.countStarted.observeAsState(initial = false)

        val timeProgress by viewModel.timeProgress.observeAsState()

        CircularProgressIndicator(
            progress = timeProgress ?: 0f,
            modifier = Modifier
                .constrainAs(circular) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(250.dp)
        )

        if (countStarted) {
            val minute by viewModel.remainingMinute.observeAsState()
            val second by viewModel.remainingSecond.observeAsState()

            CountdownText(
                minute = minute ?: "",
                second = second ?: "",
                modifier = Modifier
                    .constrainAs(timerSet) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .height(70.dp)
            )
            ControllerCounting(
                onPauseClick = viewModel::onPauseTimer,
                onStopClick = viewModel::onStopTimer,
                modifier = Modifier.constrainAs(controller) {
                    top.linkTo(timerSet.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        } else {
            val settingMinute by viewModel.settingMinute.observeAsState()
            val settingSecond by viewModel.settingSecond.observeAsState()

            TimerSet(
                minute = settingMinute ?: "",
                onMinuteChange = viewModel::onSettingMinuteChange,
                second = settingSecond ?: "",
                onSecondChange = viewModel::onSettingSecondChange,
                modifier = Modifier
                    .constrainAs(timerSet) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .height(70.dp)
            )

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
fun CountdownText(
    minute: String,
    second: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        TimerText(text = minute)
        Text(
            text = ":",
            style = MaterialTheme.typography.body1.copy(
                fontSize = 30.sp
            )
        )
        TimerText(text = second)
    }
}

@Composable
fun TimerText(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier
            .width(70.dp),
        style = MaterialTheme.typography.body2.copy(
            fontSize = 30.sp
        ),
        textAlign = TextAlign.Center
    )
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
            .size(70.dp),
        textStyle = MaterialTheme.typography.body2.copy(
            fontSize = 30.sp
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(
                text = "00",
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 30.sp
                )
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
}

@Preview
@Composable
fun CountdownTextPreview() {
    CountdownText(
        minute = "02",
        second = "01",
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
