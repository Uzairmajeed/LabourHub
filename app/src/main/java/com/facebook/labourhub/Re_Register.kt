package com.facebook.labourhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.labourhub.databinding.ActivityReRegisterBinding

class Re_Register : AppCompatActivity() {
    private lateinit var binding: ActivityReRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve phone number from intent extras
        val phoneNumber = intent.getStringExtra("phoneNumber")
        binding.editTextPhone.setText(phoneNumber)
    }
}