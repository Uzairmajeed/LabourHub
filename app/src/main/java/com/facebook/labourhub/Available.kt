package com.facebook.labourhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.labourhub.databinding.AvailableBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Available : AppCompatActivity() {
    private lateinit var binding: AvailableBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private var mainPostList: List<Post> = emptyList() // Initialize as an empty list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AvailableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        postAdapter = PostAdapter(emptyList())

        // Initialize Spinner with ArrayAdapter
        val categories = resources.getStringArray(R.array.category_options)
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.fetchFromModel()
        }

        viewModel.postListLiveData.observe(this) { postList ->
            // Check if postList is not null before saving it
            postList?.let {
                mainPostList = it as List<Post> // Save the main list when data is fetched
                Log.d("MainActivity", "Observed data: ${it.size}")
                filterPostList(binding.spinnerCategory.selectedItem.toString())
            }
        }
        // Set a listener for the search bar
        binding.searchbar.addTextChangedListener { text ->
            performSearch(text.toString())
        }
        // Use GridLayoutManager for horizontal layout with two items per row
        binding.recyclerview.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = postAdapter

        binding.nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 2) {
                binding.fabNeumorphism.show()
            } else {
                binding.fabNeumorphism.hide()
            }
        })

        binding.fabNeumorphism.setOnClickListener {
            binding.nestedscrollview.smoothScrollTo(0, 0)
        }

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterPostList(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected (if needed)
            }
        }
    }

    private fun filterPostList(category: String?) {
        category?.let {
            // Convert the selected category to uppercase and remove spaces
            val formattedCategory = it.toUpperCase().replace("\\s".toRegex(), "")

            // Filter the main list based on the formatted category
            val filteredList = if (formattedCategory == "ALL") {
                mainPostList // Return the main list if "All" is selected
            } else {
                mainPostList.filter { post ->
                    post.category.toUpperCase().replace("\\s".toRegex(), "") == formattedCategory
                }
            }
            postAdapter.updateData(filteredList)

            if (filteredList.isEmpty() && formattedCategory != "ALL") {
                // Show a toast message indicating that the category is not available
                Toast.makeText(this@Available, "This category is not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun performSearch(query: String) {
        Log.d("SearchQuery", "Query: $query")
        val filteredList = mainPostList.filter { post ->
            post.area.contains(query, ignoreCase = true)
        }
        Log.d("FilteredListSize", "Filtered List Size: ${filteredList.size}")
        postAdapter.updateData(filteredList)
    }


}

