package com.facebook.labourhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.facebook.labourhub.databinding.ActivityReRegisterBinding
import kotlinx.coroutines.launch

class Re_Register : AppCompatActivity() {
    private var selectedCategory: String? = null

    private lateinit var binding: ActivityReRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve phone number from intent extras
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val name = intent.getStringExtra("fetchedname")
        val area=   intent.getStringExtra("fetchedarea")
        val age =    intent.getStringExtra("fetchedage")
        val adhaar =  intent.getStringExtra("fetchedadhar")
        val category =intent.getStringExtra("fetchedcategory")
        binding.editTextPhone.setText(phoneNumber)
        binding.textviewTextName.setText(name)
        binding.editTextArea.setText(area)
        binding.editTextAge.setText(age)
        binding.textViewaadhar.setText(adhaar)

        // Set up the spinner item selection listener
        binding.registerspinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = parent?.getItemAtPosition(position) as? String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCategory = null
            }
        }

        binding.updateButton.setOnClickListener {
            updateToServer()
        }
    }

    private fun updateToServer() {
        val area = binding.editTextArea.text.toString()
        val age = binding.editTextAge.text.toString()
        val aadhar = binding.textViewaadhar.text.toString()
        val phone = binding.editTextPhone.text.toString()

        lifecycleScope.launch {
            try {
                val response = UpdateRepository().updateToServer(
                     area, age, aadhar, phone, selectedCategory ?: ""
                )
                response?.let {
                    // Handle success response
                    showMessage("Updation Complete")
                    finish()
                } ?: run {
                    // Handle null response
                    showMessage("Cannot Cannot:Server Error.")
                }
            } catch (e: Exception) {
                // Handle exception and display the error message
                showMessage("Error Updating data: ${e.message}")
            }
        }
    }
    private fun showMessage(message: String) {
        // Show the message using a Toast or any other UI element
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}