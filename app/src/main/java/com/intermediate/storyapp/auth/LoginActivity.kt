package com.intermediate.storyapp.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.intermediate.storyapp.story.MainActivity
import com.intermediate.storyapp.R
import com.intermediate.storyapp.helper.Status
import com.intermediate.storyapp.databinding.ActivityLoginBinding
import com.intermediate.storyapp.helper.ViewModelFactory
import com.intermediate.storyapp.model.User


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class LoginActivity  : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()

        setupAction()
        setAnimation()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            if (valid()) {
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()
                authViewModel.userLogin(email, password).observe(this) {
                    when (it) {
                        is Status.Success ->{
                            showLoading(false)
                            val response = it.data
                            saveUserData(
                                User(
                                    response.loginResult?.name.toString(),
                                    response.loginResult?.token.toString(),
                                    true
                                )
                            )
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        }
                        is Status.Loading -> showLoading(true)
                        is Status.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.check_input),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvSignup.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun valid() =
        binding.inputEmail.error == null && binding.inputPassword.error == null && !binding.inputEmail.text.isNullOrEmpty() && !binding.inputPassword.text.isNullOrEmpty()

    private fun setAnimation() {

        val txtWelcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val txtDescription = ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(500)
        val isEmail = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val isPassword = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val txtHaveAc = ObjectAnimator.ofFloat(binding.tvDontHaveAcc, View.ALPHA, 1f).setDuration(500)
        val txtSignup = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(500)

        val textAnim = AnimatorSet().apply {
            playTogether(txtWelcome,txtDescription, txtSignup, txtHaveAc)
        }
        val layoutAnim = AnimatorSet().apply {
            playTogether(isPassword, isEmail)
        }

        AnimatorSet().apply {
            playSequentially(
                textAnim,
                layoutAnim,
                btnLogin
            )
            start()
        }
    }


    private fun saveUserData(user: User) {
        authViewModel.saveUser(user)
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }
}