package com.vandalay.synergy

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class MainEventDispatcher @Inject constructor(
    private val scope: CoroutineScope
) {
    private val _events = MutableSharedFlow<MainEvent>()
    val events: Flow<MainEvent>
        get() = _events.asSharedFlow()

    fun dispatch(event: MainEvent) {
        scope.launch {
            _events.emit(event)
        }
    }
}