package com.gabrielmaz.poda.views.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.views.login.LoginActivity

class SignupActivity : AppCompatActivity(), SignupFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(name: String, email: String, password: String) {
        if (name == "") {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, SignupFragment(), null)
            .commit()
    }
}
