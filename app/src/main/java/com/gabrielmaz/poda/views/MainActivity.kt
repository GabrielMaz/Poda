package com.gabrielmaz.poda.views

import android.os.Bundle
import android.util.Log
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

    private val userController = UserController()
    private val todoController = TodoController()
    private val categoryController = CategoryController()

    override fun onFragmentInteraction() {

    }

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
            if (tasksTotal[todo.category.name] == null) {
                tasksTotal[todo.category.name] = 1
            } else {
                tasksTotal[todo.category.name] =+1
            }
            if (todo.completed) {
                if(tasksCompleted[todo.category.name] == null) {
                    tasksCompleted[todo.category.name] = 1
                } else {
                    tasksCompleted[todo.category.name] =+ 1
                }
            }
        }
    }

    companion object {
        private const val HomeFragmentTag = "HomeFragment"
        private const val TodosFragmentTag = "TodosFragment"
        private const val CategoriesFragmentTag = "CategoriesFragment"
        private const val ProfileFragmentTag = "ProfileFragment"

        lateinit var user: User
        lateinit var todos: ArrayList<Todo>
        lateinit var categories: ArrayList<Category>
        var tasksCompleted: HashMap<String, Int> = HashMap()
        var tasksTotal: HashMap<String, Int> = HashMap()
    }
}
