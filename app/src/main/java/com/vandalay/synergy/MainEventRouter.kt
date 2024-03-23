package com.vandalay.synergy

import javax.inject.Inject

class MainEventRouter @Inject constructor(
    private val checkPermissionEventHandler: CheckPermissionEventHandler,
) {
    fun onEvent(
        state: MainState,
        event: MainEvent,
    ): MainState {
        return when (event) {
            is MainEvent.CheckPermission -> checkPermissionEventHandler.onEvent(state, event)
            MainEvent.RequestPermission -> {
                state.addSideEffect(MainSideEffect.RequestPermission)
            }
        }
    }
}

sealed interface MainEvent {
    data class CheckPermission(val requested: Boolean = false) : MainEvent
    data object RequestPermission : MainEvent
}
