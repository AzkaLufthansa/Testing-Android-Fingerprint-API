package com.example.androidFingerprintAPI

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import com.example.androidFingerprintAPI.ui.utility.KeyUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class BiometricPromptManager(
    private val activity: AppCompatActivity
) {
    private val resultChannel = Channel<BiometricResult>()
    val promptResults = resultChannel.receiveAsFlow()

    fun showBiometricPrompt(
        title: String,
        description: String,
    ) {
        val  manager = BiometricManager.from(activity)
        val authenticators = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        val promptInfo = PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)
            .setConfirmationRequired(true)

        if (Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }

        when(manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.HardwareUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.FeatureUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
                return
            }
            else -> {
                Unit
            }
        }

        // Defining prompt
        val prompt = BiometricPrompt(
            activity,
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    resultChannel.trySend(BiometricResult.AuthenticationError(errString.toString()))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    // Generate keystore
                    KeyUtils.generateKeyPair()

                    // Payload that will be signed
                    val payload = "Your data to sign".toByteArray()

                    val signedPayload = KeyUtils.signPayload(payload)
                    Log.d("SIGNED PAYLOAD PRINT", signedPayload.toString())

                    if (signedPayload != null) {
                        // Send payload to server
                        resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                    } else {
                        resultChannel.trySend(BiometricResult.AuthenticationError("Failed to sign payload"))
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    resultChannel.trySend(BiometricResult.AuthenticationFailed)
                }
            }
        )

        // Authenticate
        prompt.authenticate(promptInfo.build())
    }

    sealed class BiometricResult {
        // Biometric is unavailable, hardware is busy
        data object HardwareUnavailable: BiometricResult()
        // Android doesn't have biometric feature
        data object FeatureUnavailable: BiometricResult()
        data class AuthenticationError(val error: String): BiometricResult()
        data object AuthenticationFailed: BiometricResult()
        data object AuthenticationSuccess: BiometricResult()
        data object AuthenticationNotSet: BiometricResult()
    }
}