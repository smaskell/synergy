package com.vandalay.synergy

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CheckPermissionEventHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcher: MainEventDispatcher,
) {
    fun onEvent(
        state: MainState,
        event: MainEvent.CheckPermission
    ): MainState {
        Log.d("CheckPermissionEventHandler", "onEvent: $event")
        Log.d("CheckPermissionEventHandler", "state: $state")
        return if (state.ui !is MainUi.Initial && state.ui !is MainUi.RequirePermission && state.ui !is MainUi.PermissionDenied) {
            state
        } else if (
            isNotificationServiceEnabled()
        ) {
            state.copy(ui = MainUi.PermissionGranted)
        } else {
            if (event.requested) {
                state.copy(ui = MainUi.PermissionDenied)
            } else {
                state.copy(ui = MainUi.RequirePermission())
            }
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName: String = context.packageName
        val flattenedComponents: String = Settings.Secure.getString(
            context.contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        return flattenedComponents.takeIf(String::isNotBlank)
            ?.split(":")
            ?.mapNotNull { ComponentName.unflattenFromString(it) }
            ?.exists {
                it.packageName == pkgName
            } ?: false
    }

    private fun <T> Iterable<T>.exists(predicate: (T) -> Boolean): Boolean {
        for (element in this) {
            if (predicate(element)) {
                return true
            }
        }
        return false
    }

    companion object {
        private val PERMISSIONS = listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
        )

        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    }
}