package com.gvapps.triviagame.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gvapps.triviagame.model.MainGame
import com.gvapps.triviagame.repo.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val question = MutableLiveData<MutableList<MainGame>>()
    val errorMessage = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    var isFirstTime = MutableLiveData(true)
    var showAns = MutableLiveData<Boolean>()
    var setEnable = MutableLiveData<Boolean>()
    var givenAns = MutableLiveData<String>()

    fun getQuestion() {
        isLoading.postValue(true)
        val response = repository.getQuestion()
        response.enqueue(object : Callback<MutableList<MainGame>> {
            override fun onResponse(call: Call<MutableList<MainGame>>, response: Response<MutableList<MainGame>>) {
                isFirstTime.postValue(false)
                isLoading.postValue(false)

                if (response.isSuccessful && response.body() != null)
                    question.postValue(response.body())
                else
                    errorMessage.postValue("Something Went Wrong")
            }

            override fun onFailure(call: Call<MutableList<MainGame>>, t: Throwable) {
                isLoading.postValue(false)
                errorMessage.postValue(t.message)
            }
        })
    }
}