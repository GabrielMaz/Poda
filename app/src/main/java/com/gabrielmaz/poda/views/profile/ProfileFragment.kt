package com.gabrielmaz.poda.views.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.AuthController
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.views.MainActivity
import com.gabrielmaz.poda.views.login.LoginActivity
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ProfileFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val authController = AuthController()
    private val user = MainActivity.user

    private var listener: OnFragmentInteractionListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {

            profile_loading.setIndeterminateDrawable(FadingCircle())

            setUserData()

            logout.setOnClickListener {
                profile_loading.visible()
                logout()
            }

            profile_image_add.setOnClickListener {
                listener?.onFragmentInteraction(MainActivity.ProfileFragmentTag)
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
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(tag: String)
    }

    private fun setUserData() {

        name.text = user.name
        joined.text = "${user.createdAt.dayOfWeek}".substring(0, 3).toLowerCase().capitalize() +
                ", ${user.createdAt.dayOfMonth} " +
                "${user.createdAt.month} ".toLowerCase().capitalize() +
                "${user.createdAt.year}"

        categories_used.text = MainActivity.tasksTotal.size.toString()
        total_tasks.text = getCount(MainActivity.tasksTotal).toString()
        tasks_completed.text = getCount(MainActivity.tasksCompleted).toString()
        most_used_category.text = MainActivity.tasksTotal.maxBy { it.value }?.key.toString()

        Glide
            .with(this@ProfileFragment)
            .load(user.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(profile_image)
    }

    private fun logout() {
        launch(Dispatchers.IO) {
            try {
                authController.logout()
                withContext(Dispatchers.Main) {
                    activity?.let {
                        it.startActivity(Intent(it, LoginActivity::class.java))
                        it.finish()
                    }
                }
            } catch (error: Exception) {

            }
        }
    }

    private fun getCount(map: HashMap<String, Int>): Int {
        var acc = 0
        map.forEach { task -> acc += task.value }
        return acc
    }


    fun updateProfileFragment(data: Intent?) {
        profile_image.setImageURI(data?.data)

        Glide
            .with(this@ProfileFragment)
            .load(data?.data)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(profile_image)


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
