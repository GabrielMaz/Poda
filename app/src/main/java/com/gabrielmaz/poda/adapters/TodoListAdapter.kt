package com.gabrielmaz.poda.adapters

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
import com.gabrielmaz.poda.models.TodoListItem
import kotlinx.android.synthetic.main.item_todolist.view.*
import kotlinx.android.synthetic.main.item_todolist_header.view.*

class TodoListAdapter(
    private var data: ArrayList<TodoListItem>,
    private var onCompleted: (Todo) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tasks: ArrayList<TodoListItem>
        get() = data
        set(value) {
            data = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_TYPE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todolist_header, parent, false)
            TodoListViewHolderHeader(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todolist, parent, false)
            TodoListViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].header != null) HEADER_TYPE else TODO_TYPE
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = tasks[position]

        if (holder is TodoListViewHolder) {
            val todo = row.todo!!

            holder.check.isChecked = todo.completed
            holder.description.text = todo.description
            holder.priority.text = todo.priority

            holder.category.setBackgroundColor(Color.parseColor(todo.category.color))

            if (holder.check.isChecked) {
                holder.description.paintFlags =
                    holder.description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.priority.paintFlags =
                    holder.priority.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            holder.check.setOnClickListener {
                todo.completed = holder.check.isChecked

                onCompleted(todo)
            }
        } else if (holder is TodoListViewHolderHeader) {
            val header = row.header!!
            holder.day.text = header.dayOfWeek.toString()

            holder.date.text = "${header.dayOfMonth} ${header.month}, ${header.year}"
        }
    }

    inner class TodoListViewHolderHeader(mView: View) : RecyclerView.ViewHolder(mView) {
        val day: TextView = mView.item_todo_header_day
        val date: TextView = mView.item_todo_header_date
    }

    inner class TodoListViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val check: CheckBox = mView.item_check
        val description: TextView = mView.item_description
        val priority: TextView = mView.item_priority
        val category: View = mView.item_category
    }

    companion object {
        const val HEADER_TYPE = 0
        const val TODO_TYPE = 1
    }
}