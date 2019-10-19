package com.gabrielmaz.poda.views.todos.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.models.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.ZonedDateTime
import kotlin.coroutines.CoroutineContext

class CreateTodoActivity : AppCompatActivity(),
    CreateTodoFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        if (savedInstanceState == null) {
            val category = intent.getParcelableExtra<Category>("category")
            title = "Create a ${category?.name ?: "new"} todo"
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, CreateTodoFragment.newInstance(category), null)
                .commit()
        }
    }

    override fun onTodoSubmit() {
        finish()
    }
}
