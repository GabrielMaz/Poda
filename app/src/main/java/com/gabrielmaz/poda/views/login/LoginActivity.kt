package com.gabrielmaz.poda.views.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.views.MainActivity
import com.gabrielmaz.poda.views.signup.SignupActivity

class LoginActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, LoginFragment(), null)
            .commit()
    }

    override fun onLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onSignupClicked() {
        startActivity(Intent(this, SignupActivity::class.java))
        finish()
    }
}
