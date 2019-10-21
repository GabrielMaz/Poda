package com.gabrielmaz.poda.views.categories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CategoriesFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()
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

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MODIFY_CATEGORY) {
            data?.getParcelableArrayListExtra<Todo>(CategoryActivity.modifiedTodosTag)?.let { modifiedTodos ->
                modifiedTodos.forEach { newTodo ->
                    val existingTodoIndex = todos.indexOfFirst { oldTodo -> oldTodo.id == newTodo.id }
                    if (existingTodoIndex != -1) {
                        todos[existingTodoIndex] = newTodo
                    } else {
                        todos.add(newTodo)
                    }
                }
            }
        }
    }

    private fun load() {
        launch(Dispatchers.IO) {
            try {
                todos = todosController.getTodos()
                categories = categoryController.getCategories()

                withContext(Dispatchers.Main) {
                    categories_loading.gone()

                    if (categories.isNotEmpty()) {
                        categories_grid.visible()

                        categories_grid.adapter = context?.let {
                            CategoryGridAdapter(categories, it) { category ->
                                val list = arrayListOf<Todo>()
                                list.addAll(todos.filter { todo -> todo.categoryId == category.id })

                                val intent = Intent(it, CategoryActivity::class.java)
                                intent.putParcelableArrayListExtra(CategoryActivity.todosTag, list)
                                intent.putExtra(CategoryActivity.categoryTag, category)

                                startActivityForResult(intent, MODIFY_CATEGORY)
                            }
                        }
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    categories_loading.gone()
                    Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        const val MODIFY_CATEGORY = 1005
    }
}