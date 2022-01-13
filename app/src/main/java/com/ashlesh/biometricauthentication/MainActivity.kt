package com.ashlesh.biometricauthentication

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ashlesh.biometricauthentication.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    val SUCCESFULL_AUTH = "Successfully Authenticated"
    val FAILED_AUTH = "Authentication Failed"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(
            this@MainActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == 11) {
                        binding.messageAuth.text = errString
                        startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                        Toast.makeText(
                            this@MainActivity,
                            "Please Set Up Biometric first!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        binding.messageAuth.text = errString
                    }

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.messageAuth.text = FAILED_AUTH
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    binding.messageAuth.text = SUCCESFULL_AUTH
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Please Authenticate to Proceed")
            .setNegativeButtonText("Cancel")
            .build()

        binding.buttonAuth.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}