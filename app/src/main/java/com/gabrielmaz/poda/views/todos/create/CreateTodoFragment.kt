package com.gabrielmaz.poda.views.todos.create

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.adapters.CategorySpinnerAdapter
import com.gabrielmaz.poda.controllers.CategoryController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.textString
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Category
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_create_todo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import kotlin.coroutines.CoroutineContext

class CreateTodoFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var selectedCategory: Category? = null

    private var categoryController = CategoryController()

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var categories: ArrayList<Category>

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

        create_loading.setIndeterminateDrawable(FadingCircle())

        val prioritiesAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            arrayOf("HIGH", "MEDIUM", "LOW")
        )

        prioritiesAdapter.setDropDownViewResource(R.layout.priority_view)

        priorities_spinner.adapter = prioritiesAdapter

        if (selectedCategory != null) {
            categories_loading.gone()
        } else {
            categories_loading.visible()
            loadCategories()
        }

        create_button.setOnClickListener {
            val category = selectedCategory ?: categories_spinner.selectedItem as Category
            listener?.onTodoSubmit(
                description.textString(),
                category.id,
                priorities_spinner.selectedItem as String,
                ZonedDateTime.now()
            )
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

    interface OnFragmentInteractionListener {
        fun onTodoSubmit(description: String, categoryId: Int, priority: String, dueDate: ZonedDateTime)
    }

    private fun loadCategories() {
        launch(Dispatchers.IO) {
            categories = categoryController.getCategories()

            withContext(Dispatchers.Main) {
                if (categories.isNotEmpty()) {
                    categories_spinner.visible()
                    categories_loading.gone()

                    categories_spinner.adapter =
                        activity?.let { CategorySpinnerAdapter(it, categories) }
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