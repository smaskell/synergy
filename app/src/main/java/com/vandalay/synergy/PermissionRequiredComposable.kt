package com.vandalay.synergy

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vandalay.synergy.ui.theme.SynergyTheme

@Composable
fun PermissionRequired(
    uiModel: PermissionRequiredUIModel,
    onCLick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 32.dp,
                    vertical = 64.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(uiModel.iconBackground())
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "notif icon rotation")
                    val angle = infiniteTransition.animateFloat(
                        initialValue = -5f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800,
                                delayMillis = 300,
                                easing = {
                                    OvershootInterpolator().getInterpolation(it)
                                }
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "notif icon rotation"
                    )
                    Image(
                        Icons.Rounded.Notifications,
                        "Notification icon",
                        modifier = Modifier.fillMaxSize()
                            .rotate(angle.value)
                            .offset(y = (-4).dp),
                        colorFilter = ColorFilter.tint(uiModel.iconTint())
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = uiModel.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.clearAndSetSemantics {
                        heading()
                    }
                )
            }

            Button(onClick = onCLick) {
                Text(
                    text = uiModel.buttonText,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

sealed interface PermissionRequiredUIModel {
    @Composable fun iconBackground(): Color
    @Composable fun iconTint(): Color
    val title: String
    val buttonText: String

    data object Required : PermissionRequiredUIModel {
        @Composable
        override fun iconBackground(): Color = MaterialTheme.colorScheme.secondary
        @Composable
        override fun iconTint(): Color = MaterialTheme.colorScheme.onSecondary
        override val title: String = "Synergy needs permission to read your notifications"
        override val buttonText: String = "Go to settings"
    }
    data object Denied : PermissionRequiredUIModel {
        @Composable
        override fun iconBackground(): Color = MaterialTheme.colorScheme.errorContainer
        @Composable
        override fun iconTint(): Color = MaterialTheme.colorScheme.error
        override val title: String = "Oops! It looks like the permission is still not granted"
        override val buttonText: String = "Try again"
    }
}

@Preview
@Composable
fun PermissionRequiredPreview() {
    SynergyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PermissionRequired(PermissionRequiredUIModel.Required)
        }
    }
}

@Preview
@Composable
fun PermissionDeniedPreview() {
    SynergyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PermissionRequired(PermissionRequiredUIModel.Denied)
        }
    }
}