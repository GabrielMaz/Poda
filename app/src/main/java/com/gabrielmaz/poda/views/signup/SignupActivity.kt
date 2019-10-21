package com.gabrielmaz.poda.views.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.views.MainActivity
import com.gabrielmaz.poda.views.login.LoginActivity

class SignupActivity : AppCompatActivity(), SignupFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, SignupFragment(), null)
            .commit()
    }

    override fun onSignupSuccess(name: String, email: String, password: String) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onLoginClicked() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
