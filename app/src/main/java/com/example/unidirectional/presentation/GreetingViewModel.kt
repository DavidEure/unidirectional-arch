package com.example.unidirectional.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unidirectional.arch.State
import com.example.unidirectional.data.GreetingRepositoryImpl
import com.example.unidirectional.infoWorkingIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.Serializable

class GreetingViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val mutableState = MutableStateFlow(GreetingState(false, "", ""))
    val state: StateFlow<GreetingState>
        get() = mutableState

    init {
        savedStateHandle.get<GreetingState>("state")?.let { savedState ->
            mutableState.value = savedState
        }
    }

    fun greet(nickname: String) = viewModelScope.launch {
        if (nickname.isBlank()) {
            mutableState.value.copy(
                failureMessage = "Please write your nickname",
                successMessage = ""
            ).let { newState -> applyState(newState) }
        } else {
            mutableState.value.copy(
                isLoading = true,
                successMessage = "",
                failureMessage = ""
            ).let { newState -> applyState(newState) }

            GreetingRepositoryImpl.getMessage(nickname).let {
                infoWorkingIn("Greet Action: finished")
                mutableState.value.copy(
                    isLoading = false,
                    successMessage = it,
                    failureMessage = ""
                ).let { newState -> applyState(newState) }
            }
        }
    }

    private fun applyState(newState: GreetingState) {
        savedStateHandle["state"] = newState
        mutableState.value = newState
    }
}

data class GreetingState(
    val isLoading: Boolean,
    val successMessage: String,
    val failureMessage: String
) : State, Serializable
