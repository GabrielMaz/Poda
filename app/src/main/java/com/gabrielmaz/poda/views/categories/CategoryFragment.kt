package com.gabrielmaz.poda.views.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.todolist.adapters.TodoListAdapter
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {

    private var todos = ArrayList<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todos = arguments?.getParcelableArrayList(todosTag) ?: arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        setList()
    }

    fun setList() {
        val adapter = context?.let { TodoListAdapter(todos, it) }

        category_list.layoutManager = LinearLayoutManager(activity)
        category_list.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        category_list.adapter = adapter

        listVisibility()
    }

    private fun listVisibility() {
        if (todos.isEmpty()) {
            category_list.gone()
            category_emptyview.visible()
        } else {
            category_list.visible()
            category_emptyview.gone()
        }
    }

    companion object {

        const val todosParams = "todosParams"
        const val todosTag = "todosTag"

        @JvmStatic
        fun newInstance(todos: ArrayList<Todo>) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(todosTag, todos)
                }
            }
    }
}
