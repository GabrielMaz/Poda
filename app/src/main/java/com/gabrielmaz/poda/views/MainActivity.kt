package com.gabrielmaz.poda.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.views.home.HomeFragment
import com.gabrielmaz.poda.views.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    ProfileFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        showFragment(HomeFragment(), HomeFragmentTag)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            removeActiveFragment()

            when (menuItem.itemId) {
                R.id.home -> showFragment(HomeFragment(), HomeFragmentTag)
                R.id.tasks -> showFragment(ProfileFragment(), TasksFragmentTag)
                R.id.categories -> showFragment(ProfileFragment(), CategoriesFragmentTag)
                R.id.profile -> showFragment(ProfileFragment(), ProfileFragmentTag)
            }

            true
        }
    }

    private fun removeActiveFragment() {
        listOf(
            HomeFragmentTag,
            TasksFragmentTag,
            CategoriesFragmentTag,
            ProfileFragmentTag
        ).forEach { tag ->
            val fragment = supportFragmentManager.findFragmentByTag(tag)
            fragment?.let {
                supportFragmentManager
                    .beginTransaction()
                    .remove(it)
                    .commit()
            }
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment, tag)
            .commit()
    }

    companion object {
        private const val HomeFragmentTag = "HomeFragment"
        private const val TasksFragmentTag = "TasksFragment"
        private const val CategoriesFragmentTag = "CategoriesFragment"
        private const val ProfileFragmentTag = "ProfileFragment"
    }
}
