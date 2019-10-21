package com.gabrielmaz.poda.views.todos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.views.todos.create.CreateTodoActivity
import com.gabrielmaz.poda.adapters.TodoListAdapter
import com.gabrielmaz.poda.helpers.sameDayAs
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_todos.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TodosFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()
    private val todoController = TodoController()

    private lateinit var todos: ArrayList<Todo>
    private lateinit var adapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todo_loading.setIndeterminateDrawable(FadingCircle())

        create_todo_button.setOnClickListener {
            context?.let {
                val intent = Intent(it, CreateTodoActivity::class.java)
                startActivityForResult(intent, CREATE_TODO)
            }
        }

        load()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_TODO && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<Todo>(CreateTodoActivity.createdTodoTag)?.let { newTodo ->
                todos.add(newTodo)

                val header = adapter.tasks.find {
                    it.header?.sameDayAs(newTodo.dueDate)?: false
                }
                adapter.tasks = todoController.getTodosWithHeaders(todos)
                val insertedTodoIndex = adapter.tasks.indexOfFirst { todoItem ->
                    todoItem.todo == newTodo
                }
                adapter.notifyItemInserted(insertedTodoIndex)

                if (header == null) {
                    val insertedHeaderIndex = adapter.tasks.indexOfFirst {
                        it.header?.sameDayAs(newTodo.dueDate)?: false
                    }
                    if (insertedHeaderIndex != -1) {
                        adapter.notifyItemInserted(insertedHeaderIndex)
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
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
                withContext(Dispatchers.Main) {
                    todo_loading.gone()
                    Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setList() {
        adapter = context?.let {
            TodoListAdapter(todoController.getTodosWithHeaders(todos)) { todo ->
                launch(Dispatchers.IO) {
                    try {
                        todoController.updateTodo(todo, todo.id)

                        withContext(Dispatchers.Main) {
                            adapter.tasks = todoController.getTodosWithHeaders(todos)
                            adapter.notifyItemChanged(adapter.tasks.indexOfFirst { it.todo?.id == todo.id })
                        }
                    } catch (exception: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }!!

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

    private fun listVisibility() {
        if (todos.isEmpty()) {
            todo_list.gone()
            todo_emptyview.visible()
        } else {
            todo_list.visible()
            todo_emptyview.gone()
        }
    }

    companion object {
        const val CREATE_TODO = 1000
    }
}