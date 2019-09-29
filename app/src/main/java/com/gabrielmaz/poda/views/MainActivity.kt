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
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.models.User
import com.gabrielmaz.poda.views.MainActivity.Companion.categories
import com.gabrielmaz.poda.views.categories.CategoriesFragment
import com.gabrielmaz.poda.views.home.HomeFragment
import com.gabrielmaz.poda.views.profile.ProfileFragment
import com.gabrielmaz.poda.views.todos.TodosFragment
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope,
    ProfileFragment.OnFragmentInteractionListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val userController = UserController()
    private val todoController = TodoController()
    private val categoryController = CategoryController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        main_loading.setIndeterminateDrawable(FadingCircle())

        load()
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

    private fun load() {
        launch(Dispatchers.IO) {
            try {
                user = userController.getUser()
                todos = todoController.getTodos()
                categories = categoryController.getCategories()
                getTasksCompleted()

                withContext(Dispatchers.Main) {
                    setBottomNavigationBar()
                    main_loading.gone()
                    showFragment(HomeFragment(), HomeFragmentTag)
                }


            } catch (exception: Exception) {
                Log.i("asd", "asd")
            }
        }
    }

    private fun getTasksCompleted() {
        todos.forEach { todo ->

            tasksTotal[todo.category.name] = tasksTotal[todo.category.name]?.plus(1) ?: 1


//            if (tasksTotal[todo.category.name] == null) {
//                tasksTotal[todo.category.name] = 1
//            } else {
//                val x = tasksTotal[todo.category.name]?.plus(1)
//                tasksTotal[todo.category.name] = x
////                tasksTotal[todo.category.name] = tasksTotal[todo.category.name]?.plus(1)
//            }
            if (todo.completed) {

                tasksCompleted[todo.category.name] = tasksCompleted[todo.category.name]?.plus(1) ?: 1

//                if(tasksCompleted[todo.category.name] == null) {
//                    tasksCompleted[todo.category.name] = 1
//                } else {
//                    tasksCompleted[todo.category.name]?.plus(1)
////                    tasksCompleted[todo.category.name] = tasksCompleted[todo.category.name]?.plus(1)
//                }
            }
        }
    }

    override fun onFragmentInteraction(tag: String) {
        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                pickImageFromGallery();
            }
        }
        else{
            //system OS is < Marshmallow
            pickImageFromGallery();
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){

            supportFragmentManager.findFragmentByTag(ProfileFragmentTag).also {
                it.let { fragment ->
                    if (fragment is ProfileFragment) {
                        fragment.updateProfileFragment(data)
                        user.avatar = data?.data.toString()
                    }
                }
            }
        }
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
        const val HomeFragmentTag = "HomeFragment"
        const val TodosFragmentTag = "TodosFragment"
        const val CategoriesFragmentTag = "CategoriesFragment"
        const val ProfileFragmentTag = "ProfileFragment"

        lateinit var user: User
        lateinit var todos: ArrayList<Todo>
        lateinit var categories: ArrayList<Category>
        var tasksCompleted: HashMap<String, Int> = HashMap()
        var tasksTotal: HashMap<String, Int> = HashMap()
    }
}
