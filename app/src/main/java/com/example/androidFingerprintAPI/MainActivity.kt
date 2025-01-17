package com.example.androidFingerprintAPI

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidFingerprintAPI.ui.theme.AndroidFingerprintAPITheme

class MainActivity : AppCompatActivity() {
    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidFingerprintAPITheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val biometricResult by promptManager.promptResults.collectAsState(
                        initial = null
                    )

//                    LaunchedEffect(biometricResult) {
//                        if (biometricResult is BiometricResult.AuthenticationNotSet) {
//                            if (Build.VERSION.SDK_INT >= 30) {
//                                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                                    putExtra(
//                                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED
//                                    )
//                                }
//                            }
//                        }
//                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                promptManager.showBiometricPrompt(
                                    title = "Sample prompt",
                                    description = "Sample prompt description"
                                )
                            }
                        ) {
                            Text(text = "Authenticate")
                        }

//                        biometricResult?.let { result ->
//                            Text(
//                                text = when(result) {
//                                    is BiometricResult.AuthenticationError -> "Authentication error"
//                                    BiometricRe
//                                }
//                            )
//                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenerateKeyPairButton(onClick: () -> Unit) {
    Button (
        onClick = { onClick() })
    {
        Text("Filled")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidFingerprintAPITheme {
        GenerateKeyPairButton(
            onClick = {}
        )
    }
}