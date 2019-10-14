package com.gabrielmaz.poda.views.categories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.RetrofitController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.models.TodoListItem
import com.gabrielmaz.poda.views.createTodo.CreateTodoActivity
import com.gabrielmaz.todolist.adapters.TodoListAdapter
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {

    private var todos = ArrayList<Todo>()
    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todos = arguments?.getParcelableArrayList(todosTag) ?: arrayListOf()
        category = arguments?.getParcelable(categoryTag)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        category_name.text = category?.name ?: ""

        Glide
            .with(this@CategoryFragment)
            .load("${RetrofitController.baseUrl}/${category?.photo}")
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(category_image)

        setList()

        category_button.setOnClickListener {
            context?.let {
                val intent = Intent(it, CreateTodoActivity::class.java)
                if (category != null) {
                    intent.putExtra("category", category)
                }
                it.startActivity(intent)
            }
        }
    }

    private fun setList() {

        val adapter = context?.let {
            TodoListAdapter(ArrayList(todos.map { todo -> TodoListItem(todo, null) })) {}
        }

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

        const val todosTag = "todosTag"
        const val categoryTag = "categoryTag"

        @JvmStatic
        fun newInstance(todos: ArrayList<Todo>, category: Category) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(todosTag, todos)
                    putParcelable(categoryTag, category)
                }
            }
    }
}
