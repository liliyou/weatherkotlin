package com.example.weatherkotlin.ui.components

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * 位置權限處理元件
 *
 * 封裝位置權限請求邏輯，將權限處理與 UI 分離。
 *
 * @param shouldRequest 是否應該請求權限
 * @param onResult 權限結果回調（true = 已授權）
 * @param content 子內容
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
    shouldRequest: Boolean,
    onResult: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) { permissions ->
        val granted = permissions.values.any { it }
        onResult(granted)
    }

    LaunchedEffect(shouldRequest) {
        if (shouldRequest) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    content()
}
