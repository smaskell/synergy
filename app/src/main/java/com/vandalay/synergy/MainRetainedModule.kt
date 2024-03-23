package com.vandalay.synergy

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainRetainedModule {
    @Provides
    @ActivityRetainedScoped
    fun coroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
}