package com.gvapps.triviagame.service

import com.gvapps.triviagame.model.MainGame
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Api {

    @GET("random")
     fun getQuestion(): Call<MutableList<MainGame>>



    companion object {

        private var api: Api? = null

        fun getInstance(): Api {
            if (api == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://jservice.io/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                api = retrofit.create(Api::class.java)
            }
            return api!!
        }
    }
}