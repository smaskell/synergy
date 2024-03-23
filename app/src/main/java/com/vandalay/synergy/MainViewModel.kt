package com.vandalay.synergy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val eventRouter: MainEventRouter,
    private val dispatcher: MainEventDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state

    init {
        viewModelScope.launch {
            dispatcher.events.collect { event ->
                _state.update { state ->
                    eventRouter.onEvent(state, event)
                }
            }
        }
    }

    fun onEvent(event: MainEvent) {
        dispatcher.dispatch(event)
    }

    fun sideEffectsHandled() {
        _state.update { state ->
            state.sideEffectsHandled()
        }
    }
}

data class MainState(
    val ui: MainUi = MainUi.Initial,
    val sideEffects: List<MainSideEffect> = emptyList()
) {
    fun addSideEffect(sideEffect: MainSideEffect): MainState {
        return copy(sideEffects = sideEffects + sideEffect)
    }

    fun sideEffectsHandled(): MainState {
        return copy(sideEffects = emptyList())
    }
}

sealed interface MainUi {
    data object Initial : MainUi
    data class RequirePermission(val requested: Boolean = false) : MainUi
    data object PermissionGranted : MainUi
    data object PermissionDenied : MainUi
}