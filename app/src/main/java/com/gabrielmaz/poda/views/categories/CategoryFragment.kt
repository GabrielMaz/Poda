package com.gabrielmaz.poda.views.categories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.gabrielmaz.poda.views.todos.create.CreateTodoActivity
import com.gabrielmaz.poda.adapters.TodoListAdapter
import com.gabrielmaz.poda.controllers.TodoController
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CategoryFragment : Fragment(), CoroutineScope {
    interface OnFragmentInteractionListener {
        fun todoListUpdated(todos: ArrayList<Todo>)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()
    private val todoController = TodoController()
    private var listener: OnFragmentInteractionListener? = null


    private lateinit var category: Category
    private lateinit var todos: ArrayList<Todo>
    private lateinit var adapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = arguments?.getParcelable(categoryTag)!!
        todos = arguments?.getParcelableArrayList(todosTag) ?: arrayListOf()

        val savedSortOption = savedInstanceState?.getParcelable<SortOption>(sortOptionTag)
        if (savedSortOption != null) {
            sortListAux(savedSortOption)
        } else {
            sortListAux(SortOption.CreationDate)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        category_name.text = category.name

        Glide
            .with(this)
            .load("${RetrofitController.baseUrl}/${category.photo}")
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(category_image)

        setList()

        category_button.setOnClickListener {
            context?.let {
                val intent = Intent(it, CreateTodoActivity::class.java)
                intent.putExtra(CreateTodoActivity.categoryTag, category)
                startActivityForResult(intent, CREATE_TODO)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
        listener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_TODO && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<Todo>(CreateTodoActivity.createdTodoTag)?.let { newTodo ->
                todos.add(newTodo)
                listener?.todoListUpdated(todos)

                adapter.tasks = todoController.getTodosWithoutHeaders(todos)
                val insertedTodoIndex = adapter.tasks.indexOfFirst { todoItem ->
                    todoItem.todo == newTodo
                }
                adapter.notifyItemInserted(insertedTodoIndex)
            }
        }
    }

    private fun setList() {
        adapter = context?.let {
            TodoListAdapter(todoController.getTodosWithoutHeaders(todos)) { todo ->
                launch(Dispatchers.IO) {
                    try {
                        todoController.updateTodo(todo, todo.id)

                        withContext(Dispatchers.Main) {
                            listener?.todoListUpdated(todos)
                            adapter.tasks = todoController.getTodosWithoutHeaders(todos)
                            adapter.notifyItemChanged(adapter.tasks.indexOfFirst { it.todo?.id == todo.id })
                        }
                    } catch (exception: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(activity, "Connection Error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }!!

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

    @Parcelize
    enum class SortOption : Parcelable {
        Priority,
        DueDate,
        CreationDate
    }

    private var sortOption: SortOption? = null

    private fun sortListAux(option: SortOption) {
        sortOption = option
        when (option) {
            SortOption.Priority -> {
                todos.sortBy { priorityOrderMap[it?.priority]?: 999 }
            }
            SortOption.DueDate -> {
                todos.sortBy { it?.dueDate }
            }
            SortOption.CreationDate -> {
                todos.sortBy { it?.createdAt }
            }
        }
    }

    fun sortList(option: SortOption) {
        sortListAux(option)
        adapter.tasks = todoController.getTodosWithoutHeaders(todos)
        adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(sortOptionTag, sortOption)
    }

    companion object {
        const val CREATE_TODO = 1420
        const val sortOptionTag = "sortOption"
        const val todosTag = "todosTag"
        const val categoryTag = "categoryTag"
        val priorityOrderMap = mapOf(
            "HIGH" to 0,
            "MEDIUM" to 1,
            "LOW" to 2
        )

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
