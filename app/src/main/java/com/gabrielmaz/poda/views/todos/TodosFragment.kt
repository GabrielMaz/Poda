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
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.views.MainActivity
import com.gabrielmaz.todolist.adapters.TodoListAdapter
import kotlinx.android.synthetic.main.fragment_todos.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class TodosFragment : Fragment(), CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    val todoController = TodoController()

    var todos = MainActivity.todos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_todos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setList()

        listVisibility()

//        load()
    }

    private fun listVisibility() {
        if (todos.isEmpty()) {
            todolist_list.gone()
            todolist_emptyview2.visible()
        } else {
            todolist_list.visible()
            todolist_emptyview2.gone()
        }
    }

    private fun load() {
        launch(Dispatchers.IO) {
            try {
                todos = todoController.getTodos()

                withContext(Dispatchers.Main) {
                    setList()
                }


            } catch (exception: Exception) {
                Log.i("asd", "asd")
            }
        }
    }

    fun setList() {
        val adapter = context?.let { TodoListAdapter(todos, it) }

        todolist_list.layoutManager = LinearLayoutManager(activity)
        todolist_list.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        todolist_list.adapter = adapter

        listVisibility()
    }
}