package com.gabrielmaz.poda.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.views.profile.ProfileFragment

class MainActivity : AppCompatActivity(),
    ProfileFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, ProfileFragment(), null)
            .commit()
    }
}
