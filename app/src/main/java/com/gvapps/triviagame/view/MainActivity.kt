package com.gvapps.triviagame.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
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
        viewModel.apply {
            question.observe(this@MainActivity, {
                val res = it[0]

                binding.tvTitle.text = res.category.title
                binding.tvQuestion.text = res.question
            })

            setSubmitBtnEnable.observe(this@MainActivity, {
                binding.btnSubmit.isEnabled = it
            })

            isLoading.observe(this@MainActivity, {
                binding.llQ.visibility = if (it) GONE else VISIBLE
                binding.pb.visibility = if (it) VISIBLE else GONE

                if (it)
                    viewModel.showAns.value = false
            })

            errorMessage.observe(this@MainActivity, {
                error(it)
                (it == null)
                binding.llQ.visibility = GONE
            })

            isFirstTime.observe(this@MainActivity, {
                if (it) viewModel.getQuestion()
            })

            showAns.observe(this@MainActivity, {
                binding.tvAns.visibility = if (it) VISIBLE else GONE

            })

            givenAns.observe(this@MainActivity, {
                binding.tvAns.text = "Answer : $it"
            })
        }
    }

    private fun listeners() {
        binding.btnSubmit.setOnClickListener {

            val ans = binding.etAns.text.toString().trim()

            if (ans.isEmpty()) {
             "Enter Answer Before Submitting".tos()

            } else {
                viewModel.setSubmitBtnEnable.value = false

                val mainGame = viewModel.question.value!![0]

                if (mainGame.answer.contains(ans, true)) {
                     "correct answer".tos()
                } else {
                    "incorrect answer".tos();
                }

                viewModel.showAns.value = true
                viewModel.givenAns.value = mainGame.answer

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.etAns.setText("")
                    viewModel.setSubmitBtnEnable.value = true
                    viewModel.getQuestion()
                }, 5000)
            }
        }
    }

    private fun String.tos() {
        Toast.makeText(this@MainActivity, this, Toast.LENGTH_LONG).show()
    }

    private fun error(msg: String) {

        val snackBar = Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("Try Again") {
            viewModel.getQuestion()
        }
        snackBar.show()
    }
}