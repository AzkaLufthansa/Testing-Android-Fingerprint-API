package com.example.androidFingerprintAPI

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.androidFingerprintAPI.ui.theme.AndroidFingerprintAPITheme
import com.example.androidFingerprintAPI.ui.utility.KeyUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidFingerprintAPITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        GenerateKeyPairButton(
                            onClick = {
                                Log.d("GenerateKeyPairButton", "Button clicked!")

                                KeyUtils.generateKeyPair()
                            }
                        )
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