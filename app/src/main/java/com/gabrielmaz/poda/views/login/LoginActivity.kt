package com.gabrielmaz.poda.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.gabrielmaz.poda.R

class LoginActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, LoginFragment(), null)
            .commit()
    }
}
