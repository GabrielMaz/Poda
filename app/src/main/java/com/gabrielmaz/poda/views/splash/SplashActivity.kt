package com.gabrielmaz.poda.views.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.views.login.LoginActivity
import com.gabrielmaz.todolist.views.splash.SplashFragment

class SplashActivity : AppCompatActivity(),
    SplashFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction() {
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 1500)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, SplashFragment(), null)
            .commit()
    }
}
