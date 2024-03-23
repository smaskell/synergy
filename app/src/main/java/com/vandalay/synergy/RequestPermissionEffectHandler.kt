package com.vandalay.synergy

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RequestPermissionEffectHandler @Inject constructor(
    @ActivityContext private val context: Context,
    private val dispatcher: MainEventDispatcher,
) {
    private val activity = context as ComponentActivity
    private val requestPermission = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        Log.d("RequestPermissionEffectHandler", "onActivityResult: requested permission")
        dispatcher.dispatch(MainEvent.CheckPermission(requested = true))
    }

    fun onSideEffect() {
        requestPermission.launch(
            Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)
        )
    }

    companion object {
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }
}
