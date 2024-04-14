package com.facebook.labourhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.facebook.labourhub.databinding.ActivityUpdateBinding

class Update : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateBinding.inflate(layoutInflater)
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
                binding.generateotpbutton.isEnabled = isValidAadhar

            }
        })
        binding.generateotpbutton.setOnClickListener {
            // Set alpha value of Aadhaar verification card to 0 (invisible)
            binding.adhaarverificationcardview.alpha = 0f
            // Set alpha value of phone verification card to 1 (visible)
            binding.phoneverificationcardview.alpha = 1f
        }


    }
}