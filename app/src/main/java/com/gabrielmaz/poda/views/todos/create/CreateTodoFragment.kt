package com.gabrielmaz.poda.views.todos.create

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.adapters.CategorySpinnerAdapter
import com.gabrielmaz.poda.controllers.CategoryController
import com.gabrielmaz.poda.controllers.TodoController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.textString
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_create_todo.*
import kotlinx.coroutines.*
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class CreateTodoFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()
    private var selectedCategory: Category? = null

    private var categoryController = CategoryController()
    private val todoController = TodoController()

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var categories: ArrayList<Category>
    private lateinit var picker: DatePickerDialog

    private val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedCategory = arguments?.getParcelable(selectedCategoryTag)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()

        picker = DatePickerDialog(
            activity,
            R.style.DatePickerTheme,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                due_date.setText(sdf.format(newDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        create_loading.setIndeterminateDrawable(FadingCircle())

        val prioritiesAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            arrayOf("HIGH", "MEDIUM", "LOW")
        )

        prioritiesAdapter.setDropDownViewResource(R.layout.priority_view)

        priorities_spinner.adapter = prioritiesAdapter

        if (selectedCategory != null) {
            create_loading.gone()
            create_button.isClickable = true
        } else {
            create_button.isClickable = false
            create_loading.visible()
            loadCategories()
        }

        due_date.setOnClickListener {
            picker.show()
        }

        create_button.setOnClickListener {
            create_button.isClickable = false
            create_loading.visible()
            val messages = ArrayList<String>()

            if (description.textString() == "") messages.add("Description is required")
            if (due_date.textString() == "") messages.add("Due date is required")
            if (selectedCategory == null && categories_spinner.selectedItem == null) messages.add("Category is required")

            if (messages.isNotEmpty()) {
                Toast.makeText(activity, messages.joinToString(separator = "\n"), Toast.LENGTH_LONG)
                    .show()
            } else {
                val category = selectedCategory ?: categories_spinner.selectedItem as Category
                launch(Dispatchers.IO) {
                    try {
                        val localDate = LocalDate.parse(
                            due_date.textString(),
                            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US)
                        )
                        val newTodo = todoController.createTodo(
                            description.textString(),
                            category.id,
                            priorities_spinner.selectedItem as String,
                            localDate.atStartOfDay(ZoneId.systemDefault())
                        )
                        withContext(Dispatchers.Main) {
                            create_loading.gone()
                            create_button.isClickable = true
                            listener?.onTodoSubmit(newTodo)
                        }
                    } catch (exception: Exception) {
                        withContext(Dispatchers.Main) {
                            create_loading.gone()
                            create_button.isClickable = true
                            Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
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

    interface OnFragmentInteractionListener {
        fun onTodoSubmit(todo: Todo)
    }

    private fun loadCategories() {
        launch(Dispatchers.IO) {
            try {
                categories = categoryController.getCategories()

                withContext(Dispatchers.Main) {
                    if (categories.isNotEmpty()) {
                        categories_text.visible()
                        categories_spinner.visible()
                        categories_spinner.adapter =
                            activity?.let { CategorySpinnerAdapter(it, categories) }
                        create_loading.gone()
                        create_button.isClickable = true
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    create_loading.gone()
                    create_button.isClickable = true
                    Toast.makeText(
                        activity,
                        R.string.category_fetch_error,
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.finish()
                }
            }
        }
    }

    companion object {
        const val selectedCategoryTag = "selectedCategoryTag"

        @JvmStatic
        fun newInstance(selectedCategory: Category?) =
            CreateTodoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(selectedCategoryTag, selectedCategory)
                }
            }
    }
}