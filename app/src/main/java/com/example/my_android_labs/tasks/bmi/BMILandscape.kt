package com.example.my_android_labs.tasks.bmi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my_android_labs.tasks.bmi.components.AgeCheckbox
import com.example.my_android_labs.tasks.bmi.components.CalculateButton
import com.example.my_android_labs.tasks.bmi.components.GenderSelector
import com.example.my_android_labs.tasks.bmi.components.HeightInput
import com.example.my_android_labs.tasks.bmi.components.MeasurementSystemSelector
import com.example.my_android_labs.tasks.bmi.components.ResultDisplay
import com.example.my_android_labs.tasks.bmi.components.WeightInput

@Composable
fun BMILandscape(
    modifier: Modifier = Modifier,
    viewModel: BMIViewModel,
    state: BMIState,
    innerPadding: PaddingValues
) {
    Row(
        modifier = modifier
            .padding(innerPadding)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MeasurementSystemSelector(
                selectedSystem = state.measurementSystem,
                onSystemSelected = viewModel::setMeasurementSystem
            )

            GenderSelector(
                selectedGender = state.gender,
                onGenderSelected = viewModel::setGender
            )

            AgeCheckbox(
                includeAge = state.includeAge,
                onCheckedChange = { viewModel.toggleIncludeAge() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CalculateButton(onCalculate = viewModel::calculateBMI)
        }

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeightInput(
                height = state.height,
                onHeightChanged = viewModel::updateHeight,
                isMetric = state.measurementSystem == "metric"
            )

            WeightInput(
                weight = state.weight,
                onWeightChanged = viewModel::updateWeight,
                isMetric = state.measurementSystem == "metric"
            )

            if (state.bmiResult != null) {
                ResultDisplay(
                    bmi = state.bmiResult,
                    category = state.bmiCategory,
                    idealWeight = state.idealWeight
                )
            }
        }
    }
}