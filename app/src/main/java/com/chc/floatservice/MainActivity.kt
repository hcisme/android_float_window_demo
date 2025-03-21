package com.chc.floatservice

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.chc.floatservice.services.FloatingWindowManager
import com.chc.floatservice.ui.theme.FloatServiceTheme
import com.chc.floatservice.utils.PreferenceManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FloatServiceTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .systemBarsPadding()
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        val permissionState =
                            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

                        LaunchedEffect(Unit) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val isFirstOpen = PreferenceManager.isFirstLaunch(this@MainActivity)

                                if (isFirstOpen) {
                                    permissionState.launchPermissionRequest()
                                    PreferenceManager.setFirstLaunchComplete(this@MainActivity)
                                }
                            }
                        }

                        Button(
                            onClick = {
                                checkOverlayPermission(context = this@MainActivity) {
                                    if (FloatingWindowManager.intent == null) {
                                        FloatingWindowManager.start(this@MainActivity)
                                    }
                                }
                            }
                        ) {
                            Text("开启")
                        }

                        Button(
                            onClick = {
                                if (FloatingWindowManager.intent != null) {
                                    FloatingWindowManager.stop(this@MainActivity)
                                }
                            }
                        ) {
                            Text("关闭")
                        }
                    }
                }
            }
        }
    }
}

fun checkOverlayPermission(context: Context, onGranted: () -> Unit) {
    if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${context.packageName}".toUri()
        )
        (context as Activity).startActivityForResult(intent, 1001)
        Toast.makeText(context, "请开启应用悬浮窗权限", Toast.LENGTH_SHORT).show()
    } else {
        onGranted()
    }
}
