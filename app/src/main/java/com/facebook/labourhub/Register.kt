package com.facebook.labourhub

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.facebook.labourhub.databinding.ActivityRegisterBinding
import com.google.firebase.storage.FirebaseStorage
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.launch
import org.json.JSONObject

class Register : AppCompatActivity(),PaymentResultListener{

    private lateinit var binding: ActivityRegisterBinding
    // Public property to hold the image URI
    var imageUrl: String?= null
    // Variable to hold the selected category
    private var selectedCategory: String? = null
    private var isPaymentSuccessful = false // Add this variable to track payment status


    companion object {
        private const val REQUEST_CODE_PERMISSION = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set up the spinner item selection listener
        binding.registerspinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = parent?.getItemAtPosition(position) as? String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCategory = null
            }
        }

        binding.imageUploadButton.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }


        binding.registerButton.setOnClickListener {
            checkForCorrectDetails()
        }

        binding.paymentbutton.setOnClickListener {
            startPayment()
        }
    }

    private fun startPayment() {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity:Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","LabourHub")
            options.put("description","Demoing_Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount","100")//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email","uzairmajeed995@gmail.com")
            prefill.put("contact","7006080848")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUrl=imageUri.toString()
            binding.registerProfile.setImageURI(imageUri)
            if (imageUri != null) {
                // Upload image to Firebase Cloud Storage
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("images/${System.currentTimeMillis()}_image.jpg")

                val uploadTask = imageRef.putFile(imageUri)
                uploadTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Image uploaded successfully, now get the download URL
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                             imageUrl = uri.toString()
                            Log.d("IMAGE_UPLOAD", "Image URL: $imageUrl")
                            // Save the image URL or use it as needed (e.g., store in Firebase Database)
                        }.addOnFailureListener { exception ->
                            // Handle any errors while getting download URL
                            Log.e("IMAGE_UPLOAD", "Error getting download URL: ${exception.message}")
                        }
                    } else {
                        // Handle upload failure
                        Log.e("IMAGE_UPLOAD", "Upload failed: ${task.exception?.message}")
                    }
                }
            }
        }
    }


    private fun checkPermissionsAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }else {
            // Permission denied
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }


    private fun checkForCorrectDetails() {
        val name = binding.editTextName.text.toString()
        val area = binding.editTextArea.text.toString()
        val age = binding.editTextAge.text.toString()
        val aadhar = binding.editTextaadhar.text.toString()
        val phone = binding.editTextPhone.text.toString()
        val imageUri = imageUrl

        if (name.isEmpty() || area.isEmpty() || age.isEmpty() || aadhar.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }else if (imageUri == null){
            Toast.makeText(this, "Please Upload an image.", Toast.LENGTH_SHORT).show()
        } else if (!isValidName(name)) {
            Toast.makeText(this, "Name should not contain numbers and atleast have 2 characters.", Toast.LENGTH_SHORT).show()
        } else if (!isValidArea(area)) {
            Toast.makeText(this, "Area should not contain numbers and atleast have 2 characters..", Toast.LENGTH_SHORT).show()
        } else if (!isValidAge(age)) {
            Toast.makeText(this, "Age should < 100.", Toast.LENGTH_SHORT).show()
        } else if (!isValidAadhar(aadhar)) {
            Toast.makeText(this, "Aadhar Should be of 12 Digits.", Toast.LENGTH_SHORT).show()
        } else if (!isValidPhone(phone)) {
            Toast.makeText(this, "Phone Number Should be of 10 Digits.", Toast.LENGTH_SHORT).show()
        }else if (!isPaymentSuccessful()) {
            Toast.makeText(this, "Please make the payment first.", Toast.LENGTH_SHORT).show()
        } else {
            postToServer()
            //Toast.makeText(this, "Done Succesfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isPaymentSuccessful(): Boolean {
        // Return the actual payment status
        return isPaymentSuccessful
    }

    private fun isValidName(name: String): Boolean {
        // Check if name contains any numbers
        val containsNumber = name.any { it.isDigit() }

        // Check if name has at least three characters
        val hasAtLeastThreeChars = name.length >= 3

        // Return true if name meets both conditions, false otherwise
        return !containsNumber && hasAtLeastThreeChars
    }


    private fun isValidArea(area: String): Boolean {
        // Check if area contains any numbers
        val containsNumber = area.any { it.isDigit() }

        // Check if area has at least three characters
        val hasAtLeastThreeChars = area.length >= 3

        // Return true if area meets both conditions, false otherwise
        return !containsNumber && hasAtLeastThreeChars
    }


    private fun isValidAge(age: String): Boolean {
        // Add your validation logic for age (e.g., checking if it is a valid number)
        return age.length == 2 && age.matches(Regex("\\d+"))
    }

    private fun isValidPhone(phone: String): Boolean {
        // Check if phone number has exactly 10 digits
        return phone.length == 10 && phone.matches(Regex("\\d+"))
    }

    private fun isValidAadhar(aadhar: String): Boolean {
        // Check if Aadhar number has exactly 12 digits
        return aadhar.length == 12 && aadhar.matches(Regex("\\d+"))
    }

    private fun postToServer() {
        val name = binding.editTextName.text.toString()
        val area = binding.editTextArea.text.toString()
        val age = binding.editTextAge.text.toString()
        val aadhar = binding.editTextaadhar.text.toString()
        val phone = binding.editTextPhone.text.toString()

        // Pass image URI instead of just image name
        val imageUri = imageUrl

        lifecycleScope.launch {
            try {
                val response = PostRepository().postToServer(
                    name, area, age, aadhar, phone, imageUri, selectedCategory ?: ""
                )
                response?.let {
                    // Handle success response
                    showMessage("Registration Complete")
                    finish()
                } ?: run {
                    // Handle null response
                    showMessage("Cannot Register:Server Error.")
                }
            } catch (e: Exception) {
                // Handle exception and display the error message
                showMessage("Error posting data: ${e.message}")
            }
        }
    }


    private fun showMessage(message: String) {
        // Show the message using a Toast or any other UI element
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentSuccess(p0: String?) {
        // Handle payment success logic here
        showMessage("Payment Successful")
        isPaymentSuccessful = true // Set payment status to true on success
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        // Handle payment failure/cancellation logic here
        showMessage("Payment Failed or Cancelled")
        isPaymentSuccessful = false // Set payment status to false on failure or cancellation

    }


}
