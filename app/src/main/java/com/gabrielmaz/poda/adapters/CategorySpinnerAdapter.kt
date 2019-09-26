package com.gabrielmaz.poda.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.models.Category

class CategorySpinnerAdapter(
    private val context: Context,
    var list: ArrayList<Category>
) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_spinner_categories, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        val category = list[position]

        vh.category.setBackgroundColor(Color.parseColor(category.color))
        vh.description.text = category.name

        return view
    }

    override fun getItem(position: Int): Any? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    private class ItemRowHolder(mView: View) {

        val description: TextView = mView.findViewById(R.id.item_description_spinner)
        val category: View = mView.findViewById(R.id.item_category_spinner)

    }
}