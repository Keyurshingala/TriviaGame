package com.gvapps.triviagame

import androidx.lifecycle.ViewModelProvider
import com.gvapps.triviagame.model.MainGame
import com.gvapps.triviagame.repo.MainRepository
import com.gvapps.triviagame.service.Api
import com.gvapps.triviagame.view.MainActivity
import com.gvapps.triviagame.view_model.MainViewModel
import com.gvapps.triviagame.view_model.MyViewModelFactory
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var context: MainActivity

    @Before
    fun setUp(){
        viewModel = ViewModelProvider(context, MyViewModelFactory(MainRepository(Api.getInstance())))[MainViewModel::class.java]
    }



}