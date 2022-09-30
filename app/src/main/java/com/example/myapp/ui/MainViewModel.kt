package com.example.myapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.model.SearchResponse
import com.example.myapp.services.PixabayApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val apiService: PixabayApiService by lazy {
        PixabayApiService.invoke()
    }

    val searchResult = MutableLiveData<SearchResponse>()
    val loadingState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<Pair<Boolean, Exception?>>()

    fun searchPost(query: String) {
        loadingState.postValue(true)
        errorState.postValue(Pair(false, null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = apiService.searchPhoto(query = query)
                viewModelScope.launch(Dispatchers.Main) {
                    searchResult.postValue(data)
                    loadingState.postValue(false)
                    errorState.postValue(Pair(false, null))
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    loadingState.postValue(false)
                    errorState.postValue(Pair(true, e))
                }
            }
        }
    }
}