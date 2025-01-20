package com.example.androidFingerprintAPI.ui.utility

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.ECGenParameterSpec

// CryptoUtil.kt
object KeyUtils {
    const val KEY_NAME = "my_fingerprint_key"

    fun generateKeyPair() {
        // Create an asymmetric key pair
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")

        val timeout = 10
        val builder = KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_SIGN)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .setUserAuthenticationRequired(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setUserAuthenticationParameters(
                timeout,
                KeyProperties.AUTH_DEVICE_CREDENTIAL or KeyProperties.AUTH_BIOMETRIC_STRONG
            )
        } else {
            builder.setUserAuthenticationValidityDurationSeconds(timeout)
        }

        keyPairGenerator.initialize(builder.build())
        keyPairGenerator.generateKeyPair()
    }

    fun signPayload(payload: ByteArray): ByteArray? {
        return try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            val privateKey = keyStore.getKey(KEY_NAME, null) as PrivateKey

            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initSign(privateKey)

            // Sign the payload
            signature.update(payload)
            signature.sign()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ERROR PRINT", e.printStackTrace().toString())
            null
        }
    }
}
