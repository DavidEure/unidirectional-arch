package com.example.unidirectional.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.unidirectional.data.GreetingRepositoryImpl
import com.example.unidirectional.infoWorkingIn
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GreetingViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val isLoading = MutableStateFlow(false)
    private val successMessage = savedStateHandle.getLiveData("successMessage", "")
    private val failureMessage = savedStateHandle.getLiveData("failureMessage", "")

    val state = combineTuple(
        isLoading,
        successMessage.asFlow(),
        failureMessage.asFlow()
    ).map { (isLoading, successMessage, failureMessage) ->
        GreetingViewState(
            isLoading,
            successMessage,
            failureMessage
        )
    }

    fun greet(nickname: String) = viewModelScope.launch {
        if (nickname.isBlank()) {
            failureMessage.value = "Please write your nickname"
            successMessage.value = ""
        } else {
            isLoading.value = true
            failureMessage.value = ""
            successMessage.value = ""

            val message = GreetingRepositoryImpl.getMessage(nickname)
            infoWorkingIn("Greet Action: finished")
            isLoading.value = false
            successMessage.value = message
        }
    }
}

data class GreetingViewState(
    val isLoading: Boolean,
    val successMessage: String,
    val failureMessage: String
)
