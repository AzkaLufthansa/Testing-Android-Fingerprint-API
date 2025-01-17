package com.example.androidFingerprintAPI.ui.utility

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECGenParameterSpec

// CryptoUtil.kt
object KeyUtils {
    const val KEY_NAME = "my_fingerprint_key"

    fun generateKeyPair() {
        // Create an asymmetric key pair
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")
        keyPairGenerator.initialize(
            KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_SIGN)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
                .setUserAuthenticationRequired(true)
                .build()
        )
        keyPairGenerator.generateKeyPair()


        // Retrieve the created private and public keys
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val publicKey: PublicKey = keyStore.getCertificate(KEY_NAME).publicKey

        Log.d("PublicKey", "Public Key: $publicKey")

        val keyStore2 = KeyStore.getInstance("AndroidKeyStore")
        keyStore2.load(null)
        val privateKey: PrivateKey = keyStore2.getKey(KEY_NAME, null) as PrivateKey

        Log.d("PrivateKey", "Private Key: $privateKey")
    }

    fun startListening() {
//        val signature: Signature = Signature.getInstance("SHA256withECDSA")
//        val keyStore = KeyStore.getInstance("AndroidKeyStore")
//        keyStore.load(null)
//        val key = keyStore.getKey(KEY_NAME, null) as PrivateKey
//        signature.initSign(key)
//
//        val cryptObject: CryptoObject =
//            FingerprintManager.CryptoObject(signature)
//
//        val cancellationSignal: CancellationSignal = CancellationSignal()
//        val fingerprintManager: FingerprintManager =
//            context.getSystemService(FingerprintManager::class.java)
//
//        fingerprintManager.authenticate(
//            cryptObject,
//            cancellationSignal,
//            0,
//            this,
//            null
//        )
    }
}
