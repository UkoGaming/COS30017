package com.example.workshop2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class LightState {
    OFF, DIM, ON
}

class LightViewModel : ViewModel() {
    private val _lightState = MutableLiveData(LightState.OFF)
    val lightState: LiveData<LightState> = _lightState

    fun toggleLight() {
        _lightState.value = when (_lightState.value) {
            LightState.OFF -> LightState.DIM
            LightState.DIM -> LightState.ON
            LightState.ON -> LightState.OFF
            null -> LightState.OFF
        }
    }

    fun getDrawableId(): Int {
        return when (_lightState.value) {
            LightState.OFF -> R.drawable.ic_light_off
            LightState.DIM -> R.drawable.ic_light_dim
            LightState.ON -> R.drawable.ic_light_on
            null -> R.drawable.ic_light_off
        }
    }
}
