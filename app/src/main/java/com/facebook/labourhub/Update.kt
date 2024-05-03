package com.facebook.labourhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.facebook.labourhub.databinding.ActivityUpdateBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class Update : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private val network = Network() // Initialize Network class
    var fetchedName:String=""
    var fetchedArea:String=""
    var fetchedAge:String=""
    var fetchedAdharno:String=""
    var fetchedcategory:String=""
    var  fetchedphoneNumber:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editTextaadharverification.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this implementation
            }

            override fun afterTextChanged(s: Editable?) {
                // Check if the entered text is exactly 12 digits and contains only numeric characters
                val aadharNumber = s.toString().trim()
                val isValidAadhar = aadharNumber.length == 12 && aadharNumber.all { it.isDigit() }

                // Enable or disable the button based on the validity of the Aadhaar number
                binding.validateadhaar.isEnabled = isValidAadhar

            }
        })
        binding.validateadhaar.setOnClickListener {
            checkingforadhaardetails()
            // Set alpha value of phone verification card to 1 (visible)
            binding.phoneverificationcardview.alpha = 1f
        }
         /*binding.sendotpbutton.setOnClickListener {
                   initiatePhoneVerification("+917006080848")
               }*/
    }

    private fun checkingforadhaardetails() {
        val aadharNumber = binding.editTextaadharverification.text.toString().trim()
        // Make a GET request using the Network class
        // Launch a coroutine within a scope
        CoroutineScope(Dispatchers.Main).launch {
            // Make a suspend function call using withContext
            val fetchedData = withContext(Dispatchers.IO) {
                network.fetchData()
            }

            // Check if the entered Aadhaar number exists in the fetched data
            val userData = fetchedData.find { it?.adhaar == aadharNumber }
            if (userData != null) {
                // Set alpha value of Aadhaar verification card to 0 (invisible)
                binding.adhaarverificationcardview.alpha = 0f
                // Populate phone number in phone verification EditText
                binding.Textphoneverification.setText(userData.mobile)
                // Additional data retrieval and processing if needed
                // userData.username, userData.category, etc.
                // Perform phone number verification using Firebase Authentication
                 fetchedName = userData.username
                 fetchedArea = userData.area
                 fetchedAge = userData.age
                 fetchedAdharno = userData.adhaar
                 fetchedphoneNumber = userData.mobile
                 fetchedcategory = userData.category
                 binding.sendotpbutton.setOnClickListener {
                    initiatePhoneVerification("+91${fetchedphoneNumber}")
                }
            } else {
               Toast.makeText(this@Update,"Details Not Found",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initiatePhoneVerification(phoneNumber: String) {
        Log.d("NavigateToOTP", "initiatePhoneVerification called")
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Verification successful, navigate to OTP verification activity
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@Update, "${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Save the verification ID somewhere if needed
                    // Pass both credential and verificationId to OTPVerificationActivity
                    val intent = Intent(this@Update, OTPVerificationActivity::class.java)
                    intent.putExtra("verificationId", verificationId)
                    intent.putExtra("fetchedphoneNumber", phoneNumber)
                    intent.putExtra("fetchedname", fetchedName)
                    intent.putExtra("fetchedarea", fetchedArea)
                    intent.putExtra("fetchedage", fetchedAge)
                    intent.putExtra("fetchedadhar", fetchedAdharno)
                    intent.putExtra("fetchedcategory", fetchedcategory)
                    startActivity(intent)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}