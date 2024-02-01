package com.applepulse.activeaura.ui.auth.signInScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.applepulse.activeaura.R
import com.applepulse.activeaura.base.ViewModelFactory
import com.applepulse.activeaura.databinding.ActivitySignInBinding
import com.applepulse.activeaura.ui.HomeActivity
import com.applepulse.activeaura.ui.auth.signUpScreen.firstScreen.SignUpFirstScreen
import com.applepulse.activeaura.utils.Constants

class SignInScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val signInViewModel by viewModels<SignInViewModel> { ViewModelFactory() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()
        initViews()

    }

    private fun initViews() {
        binding.run {
            emailEditText.setUserInputListener {
                signInViewModel.setEmail(it)
            }
            passwordEditText.apply {
                setUserInputListener {
                    signInViewModel.setPassword(it)
                }
                setEndDrawableIcon(
                    ResourcesCompat.getDrawable(resources, R.drawable.pass_show, null)
                )

            }
            bottomDualEditText.apply {
                firstTextView.text = getString(R.string.don_t_have_an_account)
                secondTextView.apply {
                    text = context.getString(R.string.sign_up)
                    setOnClickListener {
                        startActivity(Intent(this@SignInScreen, SignUpFirstScreen::class.java))
                    }
                }
            }
            loginButton.setOnClickListener {
                signInViewModel.login()
            }
        }
    }

    private fun initObservers() {
        signInViewModel.run {
            errorLiveData.observe(this@SignInScreen) {
                Toast.makeText(this@SignInScreen, it, Toast.LENGTH_SHORT).show()
            }
            enableLoginButton.observe(this@SignInScreen) {
                binding.loginButton.isEnabled = it
                binding.loginButton.setButtonEnabled(it)
            }
            userIDLiveData.observe(this@SignInScreen) {
                getUserFromFirebase()
            }
            userLiveData.observe(this@SignInScreen) {
                val sharedPreferences =
                    getSharedPreferences(Constants.UserData, Context.MODE_PRIVATE)
                saveInSharedPreference(sharedPreferences)
            }
            sharedPreferenceLiveData.observe(this@SignInScreen) {
                if (it) {
                    Toast.makeText(
                        this@SignInScreen,
                        getString(R.string.logged_in),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@SignInScreen, HomeActivity::class.java))
                    finishAffinity()
                }
            }
        }
    }
}