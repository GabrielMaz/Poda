package com.gabrielmaz.poda.views.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentController
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.adapters.CategoryGridAdapter
import com.gabrielmaz.poda.controllers.CategoryController
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.views.MainActivity
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CategoriesFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var todosController = TodoController()
    private var categoryController = CategoryController()

    private lateinit var todos: ArrayList<Todo>
    private lateinit var categories: ArrayList<Category>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categories_loading.setIndeterminateDrawable(FadingCircle())

        load()
    }

    private fun load() {
        launch(Dispatchers.IO) {
            todos = todosController.getTodos()
            categories = categoryController.getCategories()

            withContext(Dispatchers.Main) {

                categories_loading.gone()

                if (categories.isNotEmpty()) {
                    categories_grid.visible()

                    categories_grid.adapter =
                        activity?.let { CategoryGridAdapter(categories, it, todos) }
                }
            }
        }
    }
}