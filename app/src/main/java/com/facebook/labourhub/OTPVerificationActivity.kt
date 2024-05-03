package com.facebook.labourhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPVerificationActivity : AppCompatActivity() {
    private lateinit var editTextOTP: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)

        editTextOTP = findViewById(R.id.editTextOTP)
        val buttonVerifyOTP: Button = findViewById(R.id.buttonVerifyOTP)

        // Retrieve verificationId from intent extras
        val verificationId = intent.getStringExtra("verificationId")
        val phoneNumber =  intent.getStringExtra("fetchedphoneNumber")
        val name = intent.getStringExtra("fetchedname")
        val area=   intent.getStringExtra("fetchedarea")
        val age =    intent.getStringExtra("fetchedage")
        val adhaar =  intent.getStringExtra("fetchedadhar")
        val category =intent.getStringExtra("fetchedcategory")


        buttonVerifyOTP.setOnClickListener {
            val otpCode = editTextOTP.text.toString().trim()
            if (otpCode.length == 6 && verificationId != null) {
                // Complete the verification process with the entered OTP code and verificationId
                verifyOTP(verificationId, otpCode,phoneNumber,name,area,age,adhaar,category)
            } else {
                Toast.makeText(this, "Please enter a valid OTP code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyOTP(verificationId: String, otpCode: String,phoneNumber: String?,name: String?,area: String?,
                          age: String?,
                          adhaar:String?,category: String?) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Navigate to Re_Register activity with the phone number
                    navigateToNextActivity(phoneNumber,name,area,age,adhaar,category)
                } else {
                    Toast.makeText(this, "Verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToNextActivity(phoneNumber: String?,
                                       name: String?,
                                       area: String?,
                                       age: String?,
                                       adhaar:String?,
                                       category: String?) {
        // Navigate to the desired activity after successful verification
        // Example:
        val intent = Intent(this, Re_Register::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("fetchedname", name)
        intent.putExtra("fetchedarea", area)
        intent.putExtra("fetchedage", age)
        intent.putExtra("fetchedadhar", adhaar)
        intent.putExtra("fetchedcategory", category)
        startActivity(intent)
        finish() // Finish the OTP verification activity
    }
}