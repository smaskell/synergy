package com.vandalay.synergy

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MainSideEffectRouter @Inject constructor(
    private val requestPermissionEffectHandler: RequestPermissionEffectHandler,
) {

    fun onSideEffect(
        sideEffect: MainSideEffect,
    ) {
        when (sideEffect) {
            MainSideEffect.RequestPermission -> requestPermissionEffectHandler.onSideEffect()
        }
    }
}

sealed interface MainSideEffect {
    object RequestPermission : MainSideEffect
}