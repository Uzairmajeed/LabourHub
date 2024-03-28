package com.facebook.labourhub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MyViewModel : ViewModel() {
    private val network = Network()

    // MutableLiveData to hold the list of Post data
    private val _postListLiveData = MutableLiveData<List<Post?>>()

    // Expose an immutable LiveData to be observed by the UI
    val postListLiveData: LiveData<List<Post?>> get() = _postListLiveData

    suspend fun fetchFromModel() {
        val postList = network.fetchData()
        _postListLiveData.postValue(postList)
    }
}
