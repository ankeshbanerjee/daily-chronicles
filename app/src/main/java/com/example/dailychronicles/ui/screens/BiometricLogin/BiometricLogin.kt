package com.example.dailychronicles.ui.screens.BiometricLogin

import androidx.activity.compose.BackHandler
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.dailychronicles.app.host.LocalNavController
import com.example.dailychronicles.utils.showToast

fun authenticateWithBiometrics(context: FragmentActivity, onSuccess: () -> Unit) {
    val biometricPrompt = BiometricPrompt(
        context,
        ContextCompat.getMainExecutor(context),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showToast(context, "Authentication Error: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast(context, "Failed to authenticate")
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Login")
        .setSubtitle("Login using your biometric credentials")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .setConfirmationRequired(true)
        .build()

    biometricPrompt.authenticate(promptInfo)
}

@Preview
@Composable
fun BiometricLoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current as FragmentActivity
    val navController = LocalNavController.current

    BackHandler {

    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Button(onClick = {
            authenticateWithBiometrics(context, onSuccess = {
//                navController.navigate(Home) {
//                    popUpTo(navController.graph.id) {
//                        inclusive = true
//                    }
//                }
                navController.popBackStack()
            })
        }, modifier = modifier.align(Alignment.Center)) {
            Text(text = "Unlock")
        }
    }
}