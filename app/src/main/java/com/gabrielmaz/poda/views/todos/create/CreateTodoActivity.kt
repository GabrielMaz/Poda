package com.gabrielmaz.poda.views.todos.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.models.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.ZonedDateTime
import kotlin.coroutines.CoroutineContext

class CreateTodoActivity : AppCompatActivity(), CoroutineScope, CreateTodoFragment.OnFragmentInteractionListener {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val todoController = TodoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        if (savedInstanceState == null) {
            val category = intent.getParcelableExtra<Category>("category")
            title = "Create a ${category?.name?: "new"} todo"
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, CreateTodoFragment.newInstance(category), null)
                .commit()
        }
    }

    override fun onTodoSubmit(description: String, categoryId: Int, priority: String, dueDate: ZonedDateTime) {
        launch(Dispatchers.IO) {
            todoController.createTodo(description, categoryId, priority, dueDate)
            finish()
        }
    }
}
