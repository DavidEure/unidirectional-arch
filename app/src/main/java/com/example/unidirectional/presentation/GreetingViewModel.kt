package com.example.unidirectional.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unidirectional.arch.Action
import com.example.unidirectional.arch.State
import com.example.unidirectional.data.GreetingRepositoryImpl
import com.example.unidirectional.infoWorkingIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Serializable

class GreetingViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val actionFlow = MutableSharedFlow<GreetingAction>()
    private val mutableState = MutableStateFlow(GreetingState(false, "", ""))
    val state: StateFlow<GreetingState>
        get() = mutableState

    init {
        viewModelScope.launch {
            handleActions()
        }

        savedStateHandle.get<GreetingState>("state")?.let { savedState ->
            mutableState.value = savedState
        }
    }

    private suspend fun handleActions() {
        actionFlow.collect { action ->
            infoWorkingIn("handle Action: ${action.javaClass}")
            when (action) {
                is GreetingAction.Greet -> {
                    if (action.nickname.isBlank()) {
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

                        GreetingRepositoryImpl.getMessage(action.nickname).let {
                            infoWorkingIn("Greet Action: finished")
                            mutableState.value.copy(
                                isLoading = false,
                                successMessage = it,
                                failureMessage = ""
                            ).let { newState -> applyState(newState) }
                        }
                    }
                }
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

sealed class GreetingAction : Action, Serializable {
    data class Greet(val nickname: String) : GreetingAction()
}

