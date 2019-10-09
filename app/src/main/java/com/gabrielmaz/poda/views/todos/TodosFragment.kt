package com.gabrielmaz.poda.views.todos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.controllers.UserController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.models.TodoListItem
import com.gabrielmaz.todolist.adapters.TodoListAdapter
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_todos.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class TodosFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val userController = UserController()
    private val todoController = TodoController()

    private lateinit var todos: ArrayList<Todo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_todos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todo_loading.setIndeterminateDrawable(FadingCircle())

        load()
    }

    private fun listVisibility() {
        if (todos.isEmpty()) {
            todo_list.gone()
            todo_emptyview.visible()
        } else {
            todo_list.visible()
            todo_emptyview.gone()
        }
    }

    private fun load() {
        launch(Dispatchers.IO) {
            try {
                todos = todoController.getTodos()

                withContext(Dispatchers.Main) {
                    todo_loading.gone()
                    setList()
                }

            } catch (exception: Exception) {
                Log.i("asd", "asd")
            }
        }
    }

    private fun setList() {

        val adapter = context?.let {
            TodoListAdapter(todoController.getTodoWithHeaders(todos)) { todo ->
                launch(Dispatchers.IO) {
                    try {

                        val user = userController.getUser()
                        todoController.setTodo(todo, user.id)

                        load()
                    } catch (ex: java.lang.Exception) {

                    }
                }
            }
        }

        todo_list.layoutManager = LinearLayoutManager(activity)
        todo_list.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        todo_list.adapter = adapter

        listVisibility()
    }
}