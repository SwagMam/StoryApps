package com.intermediate.storyapp.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.lifecycle.ViewModelProvider

import com.intermediate.storyapp.R
import com.intermediate.storyapp.helper.Status
import com.intermediate.storyapp.databinding.ActivityRegisterBinding

import com.intermediate.storyapp.helper.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        setAnimation()
    }

    private fun setupAction() {
        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnSignup.setOnClickListener{
            if (valid()) {
                val name = binding.inputUsername.text.toString()
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()
                authViewModel.userRegister(name, email, password).observe(this) {
                    when (it) {
                        is Status.Success -> {
                            showLoading(false)
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
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
    }

    private fun valid() =
        binding.inputEmail.error == null && binding.inputPassword.error == null && binding.inputUsername.error == null && !binding.inputEmail.text.isNullOrEmpty() && !binding.inputPassword.text.isNullOrEmpty() && !binding.inputUsername.text.isNullOrEmpty()

    private fun setAnimation() {
        val txtReg = ObjectAnimator.ofFloat(binding.tvReg, View.ALPHA, 1f).setDuration(500)
        val isName = ObjectAnimator.ofFloat(binding.inputUsername, View.ALPHA, 1f).setDuration(500)
        val isEmail = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val isPass = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val txtHaveAc = ObjectAnimator.ofFloat(binding.tvHaveAcc, View.ALPHA, 1f).setDuration(500)
        val txtLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        val textAnim = AnimatorSet().apply {
            playTogether(txtReg, txtLogin, txtHaveAc)
        }
        val layoutAnim = AnimatorSet().apply {
            playTogether(isName, isPass, isEmail)
        }

        AnimatorSet().apply {
            playSequentially(
                textAnim,
                layoutAnim,
                btnSignup
            )
            start()
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        authViewModel= ViewModelProvider(this, factory)[AuthViewModel::class.java]
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