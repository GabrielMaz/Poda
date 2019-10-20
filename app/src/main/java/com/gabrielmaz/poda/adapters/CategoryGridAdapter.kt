package com.gabrielmaz.poda.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.RetrofitController
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.views.categories.CategoryActivity
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryGridAdapter(
    private var categoryList: ArrayList<Category>,
    private var context: Context,
    private var onCategorySelected: (Category) -> Unit
) :
    BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = this.categoryList[position]

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_category, null)

        view.item_category_text.text = item.name

        Glide
            .with(context)
            .load("${RetrofitController.baseUrl}/${item.photo}")
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(view.item_category_photo)

        view.category_item.setOnClickListener {
            val category = categoryList.find { it.id == item.id }
            category?.let { onCategorySelected(it) }
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return categoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return categoryList.size
    }
}