package com.example.my_android_labs.tasks.bmi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BMIState(
    val height: String = "",
    val weight: String = "",
    val measurementSystem: String = "metric",
    val gender: String = "male",
    val includeAge: Boolean = false,
    val bmiResult: Float? = null,
    val bmiCategory: String = "",
    val idealWeight: String = ""
)

class BMIViewModel : ViewModel() {
    private val _state = MutableStateFlow(BMIState())
    val state = _state.asStateFlow()

    fun updateHeight(height: String) {
        if (height.isEmpty() || height.matches(Regex("^\\d*\\.?\\d*$"))) {
            _state.update { it.copy(height = height) }
        }
    }

    fun updateWeight(weight: String) {
        if (weight.isEmpty() || weight.matches(Regex("^\\d*\\.?\\d*$"))) {
            _state.update { it.copy(weight = weight) }
        }
    }

    fun setMeasurementSystem(system: String) {
        _state.update { it.copy(measurementSystem = system) }
    }

    fun setGender(gender: String) {
        _state.update { it.copy(gender = gender) }
    }

    fun toggleIncludeAge() {
        _state.update { it.copy(includeAge = !it.includeAge) }
    }

    fun calculateBMI() {
        viewModelScope.launch {
            val height = _state.value.height.toFloatOrNull() ?: 0f
            val weight = _state.value.weight.toFloatOrNull() ?: 0f

            if (height > 0 && weight > 0) {
                val bmi = calculateBMIValue(
                    height,
                    weight,
                    _state.value.measurementSystem
                )

                val category = getBMICategory(bmi)
                val ideal = calculateIdealWeight(
                    height,
                    _state.value.gender,
                    _state.value.measurementSystem,
                    _state.value.includeAge
                )

                _state.update {
                    it.copy(
                        bmiResult = bmi,
                        bmiCategory = category,
                        idealWeight = ideal
                    )
                }
            }
        }
    }

    private fun calculateBMIValue(height: Float, weight: Float, system: String): Float {
        return if (system == "metric") {
            weight / ((height / 100) * (height / 100))
        } else {
            (weight * 703) / (height * height)
        }
    }

    private fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 16.5 -> "Выраженный дефицит веса"
            bmi < 18.5 -> "Недостаточный вес"
            bmi < 25 -> "Нормальный вес"
            bmi < 30 -> "Избыточный вес"
            bmi < 35 -> "Ожирение I степени"
            bmi < 40 -> "Ожирение II степени"
            else -> "Ожирение III степени"
        }
    }

    private fun calculateIdealWeight(
        height: Float,
        gender: String,
        system: String,
        includeAge: Boolean
    ): String {
        var baseWeight = if (gender == "male") 50f else 45.5f
        baseWeight += 0.9f * (height - 152)

        if (includeAge) {
            baseWeight *= 0.98f
        }

        val range = if (system == "metric") {
            val min = baseWeight * 0.95f
            val max = baseWeight * 1.05f
            "${min.toInt()}-${max.toInt()} кг"
        } else {
            val lbs = baseWeight * 2.20462f
            val min = lbs * 0.95f
            val max = lbs * 1.05f
            "${min.toInt()}-${max.toInt()} фунтов"
        }

        return range
    }
}