package com.gabrielmaz.poda.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.CategoryController
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.controllers.UserController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.invisible
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.models.User
import com.gabrielmaz.poda.views.categories.CategoriesFragment
import com.gabrielmaz.poda.views.home.HomeFragment
import com.gabrielmaz.poda.views.profile.ProfileFragment
import com.gabrielmaz.poda.views.todos.TodosFragment
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope,
    ProfileFragment.OnFragmentInteractionListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    lateinit var todos: ArrayList<Todo>
    lateinit var categories: ArrayList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        bottomNavigation.isClickable = false

        main_loading.setIndeterminateDrawable(FadingCircle())

        if (savedInstanceState == null) {
            load(HomeFragmentTag)
        } else {
            setBottomNavigationBar()
            main_loading.gone()
        }
    }

    private fun removeActiveFragment() {
        listOf(
            HomeFragmentTag,
            TodosFragmentTag,
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

    private fun setBottomNavigationBar() {
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            removeActiveFragment()

            when (menuItem.itemId) {
                R.id.home -> showFragment(HomeFragment(), HomeFragmentTag)
                R.id.tasks -> showFragment(TodosFragment(), TodosFragmentTag)
                R.id.categories -> showFragment(CategoriesFragment(), CategoriesFragmentTag)
                R.id.profile -> showFragment(ProfileFragment(), ProfileFragmentTag)
            }

            true
        }
    }

    private fun load(fragment: String) {
        main_loading.invisible()

        setBottomNavigationBar()
        when (fragment) {
            HomeFragmentTag -> showFragment(HomeFragment(), HomeFragmentTag)
            TodosFragmentTag -> showFragment(TodosFragment(), TodosFragmentTag)
            CategoriesFragmentTag -> showFragment(
                CategoriesFragment(),
                CategoriesFragmentTag
            )
            ProfileFragmentTag -> supportFragmentManager.findFragmentByTag(
                ProfileFragmentTag
            ).also {
                it?.let { fragment ->
                    if (fragment is ProfileFragment) {
                        fragment.setUserData()
                    }
                }
            }
            else -> showFragment(HomeFragment(), HomeFragmentTag)
        }
    }

    override fun onFragmentInteraction(tag: String, action: String) {
        if (action == ProfileFragment.LOAD_IMAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        } else if (action == ProfileFragment.LOAD_DATA) {
            load(ProfileFragmentTag)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            supportFragmentManager.findFragmentByTag(ProfileFragmentTag).also {
                it.let { fragment ->
                    if (fragment is ProfileFragment) {
                        fragment.updateProfileFragment(data)
                    }
                }
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001

        const val HomeFragmentTag = "HomeFragment"
        const val TodosFragmentTag = "TodosFragment"
        const val CategoriesFragmentTag = "CategoriesFragment"
        const val ProfileFragmentTag = "ProfileFragment"

    }
}
