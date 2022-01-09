package com.gvapps.triviagame.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gvapps.triviagame.databinding.MainActivityBinding
import com.gvapps.triviagame.repo.MainRepository
import com.gvapps.triviagame.service.Api.Companion.getInstance
import com.gvapps.triviagame.view_model.MainViewModel
import com.gvapps.triviagame.view_model.MyViewModelFactory

class MainActivity : AppCompatActivity() {


    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initui()

    }

    private fun initui() {
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(MainRepository(getInstance()))
        )[MainViewModel::class.java]

        observers()
        listeners()
    }

    private fun observers() {
        viewModel.question.observe(this, {
            if (it.isSuccessful && it.body() != null) {

                val res = it.body()!![0]

                binding.tvTitle.text = res.category.title
                binding.tvQuestion.text = res.question

            } else {
                tos("Something Went Wrong")
            }
        })

        viewModel.isLoading.observe(this, {
            isLoading(it)
        })

        viewModel.errorMessage.observe(this, {
            tos(it)
        })

        viewModel.isFirstTime.observe(this, {
            if (it) viewModel.getQuestion()
        })

        viewModel.showAns.observe(this, {
            binding.tvAns.visibility = if (it) VISIBLE else GONE

        })

        viewModel.givenAns.observe(this, {
            binding.tvAns.text = "Answer : $it"
        })
    }

    private fun listeners() {
        binding.btnSubmit.setOnClickListener {

            val ans = binding.etAns.text.toString().trim()

            if (ans.isEmpty()) {
                tos("Empty Answer")

            } else {
                val mainGame = viewModel.question.value!!.body()!![0]

                if (mainGame.answer.equals(ans, true)) {
                    tos("correct answer")
                } else {
                    tos("incorrect answer")
                }

                viewModel.showAns.value = true
                viewModel.givenAns.value = mainGame.answer

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.etAns.setText("")
                    viewModel.getQuestion()
                }, 5000)

            }

        }
    }


    private fun isLoading(loading: Boolean) {
        binding.llQ.visibility = if (loading) GONE else VISIBLE
        binding.pb.visibility = if (loading) VISIBLE else GONE

        if (loading)
            viewModel.showAns.value = false

    }

    private fun tos(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}