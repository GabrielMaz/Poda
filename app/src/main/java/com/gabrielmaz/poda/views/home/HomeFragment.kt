package com.gabrielmaz.poda.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.adapters.HomeAdapter
import com.gabrielmaz.poda.models.HomeItem
import com.gabrielmaz.poda.views.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var list = ArrayList<HomeItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = MainActivity.user

        home_name.text = user.name
        home_status.text = "Your task status for " +
                "${user.updatedAt.month}".substring(0, 3).toLowerCase().capitalize() +
                " ${user.updatedAt.dayOfMonth}" +
                " , ${user.updatedAt.year}"
        Glide
            .with(this@HomeFragment)
            .load(user.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(image)

        generateList()
        home_grid.adapter = activity?.let { HomeAdapter(list, it) }
    }

    private fun generateList() {

        MainActivity.tasksTotal.forEach { task ->
            val progress =
                if (MainActivity.tasksCompleted[task.key] != null) MainActivity.tasksCompleted[task.key]!! / task.value else 0
            list.add(HomeItem(task.key, progress))
        }

//        list.add(HomeItem("Home", 10))
//        list.add(HomeItem("Work", 30))
//        list.add(HomeItem("Study", 70))
//        list.add(HomeItem("Ucu", 100))
    }
}