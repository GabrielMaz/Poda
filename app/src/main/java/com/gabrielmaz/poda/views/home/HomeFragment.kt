package com.gabrielmaz.poda.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.adapters.HomeGridAdapter
import com.gabrielmaz.poda.controllers.CategoryController
import com.gabrielmaz.poda.controllers.RetrofitController
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.controllers.UserController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.invisible
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.HomeItem
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.models.User
import com.gabrielmaz.poda.views.MainActivity
import com.gabrielmaz.poda.views.categories.CategoriesFragment
import com.gabrielmaz.poda.views.profile.ProfileFragment
import com.gabrielmaz.poda.views.todos.TodosFragment
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val userController = UserController()
    private val categoryController = CategoryController()
    private val todoController = TodoController()

    var list = ArrayList<HomeItem>()
    private lateinit var user: User
    private lateinit var categories: ArrayList<Category>
    private lateinit var todos: ArrayList<Todo>
    private lateinit var totalTasks: HashMap<String, Int>
    private lateinit var completedTasks: HashMap<String, Int>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home_loading.setIndeterminateDrawable(FadingCircle())

        load()
    }

    private fun generateList() {

        list = ArrayList()

        totalTasks.forEach { task ->
            val progress =
                if (completedTasks[task.key] != null) completedTasks[task.key]!! * 100 / task.value else 0
            list.add(HomeItem(task.key, progress))
        }
    }

    private fun load() {
        launch(Dispatchers.IO) {
            try {
                user = userController.getUser()
                categories = categoryController.getCategories()
                todos = todoController.getTodos()
                totalTasks = todoController.getTotalTasks(todos)
                completedTasks = todoController.getCompletedTasks(todos)
                generateList()


                withContext(Dispatchers.Main) {
                    home_name.text = user.name
                    home_status.text = "Your task status for " +
                            "${user.updatedAt.month}".substring(0, 3).toLowerCase().capitalize() +
                            " ${user.updatedAt.dayOfMonth}" +
                            " , ${user.updatedAt.year}"
                    Glide
                        .with(this@HomeFragment)
                        .load("${RetrofitController.baseUrl}/${user.avatar}")
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .into(image)

                    home_loading.gone()

                    if (list.isNotEmpty()) {
                        home_emptyview.gone()
                        home_grid.visible()

                        home_grid.adapter = activity?.let { HomeGridAdapter(list, it) }

                    } else {
                        home_emptyview.visible()
                        home_grid.gone()
                    }
                }

            } catch (exception: Exception) {
                Log.i("asd", "asd")
            }
        }
    }
}