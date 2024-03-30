package com.vandalay.synergy

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.vandalay.synergy.ui.theme.SynergyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sideEffectRouter: MainSideEffectRouter
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED).onEach { state ->
            state.sideEffects.forEach(sideEffectRouter::onSideEffect)
            viewModel.sideEffectsHandled()
        }.launchIn(viewModel.viewModelScope)


        setContent {
            val ui = viewModel.state.collectAsState().value.ui
            SynergyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (ui) {
                        MainUi.PermissionGranted -> Greeting("Kind Sir!")
                        MainUi.Initial,
                        is MainUi.RequirePermission -> {
                            PermissionRequired(PermissionRequiredUIModel.Required) {
                                viewModel.onEvent(MainEvent.RequestPermission)
                            }
                        }

                        MainUi.PermissionDenied -> {
                            PermissionRequired(PermissionRequiredUIModel.Denied) {
                                viewModel.onEvent(MainEvent.RequestPermission)
                            }
                        }
                    }
                }
            }
        }
        viewModel.onEvent(MainEvent.CheckPermission())
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SynergyTheme {
        Greeting("Android")
    }
}