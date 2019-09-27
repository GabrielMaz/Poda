package com.gabrielmaz.todolist.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.models.Todo
import kotlinx.android.synthetic.main.item_todolist.view.*

class TodoListAdapter(
    private var data: ArrayList<Todo>,
    private val context: Context
) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    var tasks: ArrayList<Todo>
        get() = data
        set(value) {
            data = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todolist, parent, false)
        return TodoListViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val row = tasks[position]

        holder.check.isChecked = row.completed
        holder.description.text = row.description
        holder.priority.text = row.priority

//        holder.category.setBackgroundColor(ContextCompat.getColor(context, row.category.color))
        holder.category.setBackgroundColor(Color.parseColor(row.category.color))

        if (holder.check.isChecked) {
            holder.description.paintFlags =
                holder.description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.priority.paintFlags = holder.priority.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        holder.check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                row.completed = true
                holder.description.paintFlags =
                    holder.description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.priority.paintFlags =
                    holder.priority.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                row.completed = false
                holder.description.paintFlags =
                    holder.description.paintFlags xor Paint.STRIKE_THRU_TEXT_FLAG
                holder.priority.paintFlags =
                    holder.priority.paintFlags xor Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }


    inner class TodoListViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val check: CheckBox = mView.item_check
        val description: TextView = mView.item_description
        val priority: TextView = mView.item_priority
        val category: View = mView.item_category
    }
}