package com.example.unidirectional.presentation

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.unidirectional.R

class GreetingFragment : Fragment(R.layout.fragment_greeting) {
    private val viewModel: GreetingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.compose_view).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Greeting(
                    viewModel = viewModel,
                    onNextClick = { view.findNavController().navigate(R.id.to_otherFragment) }
                )
            }
        }
    }
}