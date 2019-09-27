package com.gabrielmaz.poda.views.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.adapters.CategoryGridAdapter
import com.gabrielmaz.poda.views.MainActivity
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        categories_grid.adapter = activity?.let { CategoryGridAdapter(MainActivity.categories, it, MainActivity.todos) }
    }
}