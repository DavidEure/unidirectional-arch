package com.example.unidirectional.presentation

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.unidirectional.R
import com.example.unidirectional.infoWorkingIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GreetingFragment : Fragment(R.layout.fragment_greeting) {
    private val viewModel: GreetingViewModel by viewModels()
    private var uiStateJob: Job? = null
    private var nickname: AppCompatEditText? = null
    private var successMessage: AppCompatTextView? = null
    private var failureMessage: AppCompatTextView? = null
    private var loading: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nickname = view.findViewById(R.id.nickname)
        successMessage = view.findViewById(R.id.success_message)
        failureMessage = view.findViewById(R.id.failure_message)
        loading = view.findViewById(R.id.loading)
        view.findViewById<View>(R.id.next).apply {
            setOnClickListener {
                view.findNavController().navigate(R.id.to_otherFragment)
            }
        }
        view.findViewById<AppCompatButton>(R.id.greet).apply {
            setOnClickListener {
                lifecycleScope.launch {
                    viewModel.actionFlow.emit(GreetingAction.Greet(nickname?.text.toString()))
                }
            }
        }
    }

    private fun handleState(state: GreetingState) {
        infoWorkingIn("handleState $state")
        loading?.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
        successMessage?.text = state.successMessage
        failureMessage?.text = state.failureMessage
    }

    override fun onStart() {
        super.onStart()
        uiStateJob = lifecycleScope.launch {
            viewModel.state.collect { state -> handleState(state) }
        }
    }

    override fun onStop() {
        uiStateJob?.cancel()
        super.onStop()
    }
}