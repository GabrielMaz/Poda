package com.gabrielmaz.poda.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.models.HomeItem
import kotlinx.android.synthetic.main.item_home.view.*

class HomeGridAdapter(private var homeList: ArrayList<HomeItem>, private var context: Context) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = this.homeList[position]

        var inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflator.inflate(R.layout.item_home, null)

        view.item_home_progress_bar.progress = item.progress
        view.item_home_progress_number.text = "${item.progress}%"
        view.item_home_category_name.text = item.name

        return view
    }

    override fun getItem(position: Int): Any {
        return homeList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return homeList.size
    }
}