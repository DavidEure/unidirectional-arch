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
    private val nickname = savedStateHandle.getLiveData("nickname", "")
    private val successMessage = savedStateHandle.getLiveData("successMessage", "")
    private val failureMessage = savedStateHandle.getLiveData("failureMessage", "")

    val state = combineTuple(
        isLoading,
        nickname.asFlow(),
        successMessage.asFlow(),
        failureMessage.asFlow()
    ).map { (isLoading, nickname, successMessage, failureMessage) ->
        GreetingViewState(
            isLoading,
            nickname,
            successMessage,
            failureMessage
        )
    }

    fun greet() = viewModelScope.launch {
        val nickname = nickname.value ?: ""
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

    fun onNickNameChange(text: String) = viewModelScope.launch {
        if (text.length <= 20) {
            nickname.value = text
        }
    }
}

data class GreetingViewState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val successMessage: String = "",
    val failureMessage: String = ""
)
