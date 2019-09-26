package com.gabrielmaz.poda.views.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.AuthController
import com.gabrielmaz.poda.controllers.UserController
import com.gabrielmaz.poda.models.User
import com.gabrielmaz.poda.views.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class ProfileFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val userController = UserController()
    private val authController = AuthController()
    private lateinit var user: User

    private var listener: OnFragmentInteractionListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()

        logout.setOnClickListener {
            logout()
        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction()
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
        fun onFragmentInteraction()
    }

    private fun getUser() {
        launch(Dispatchers.IO) {
            try {
                user = userController.getUser()
                withContext(Dispatchers.Main) {
                    name.text = user.name
                    joined.text = "${user.createdAt.dayOfWeek},".substring(0,3).toLowerCase().capitalize() +
                            " ${user.createdAt.dayOfMonth} " +
                            "${user.createdAt.month} ".toLowerCase().capitalize() +
                            "${user.createdAt.year}"




                    categories.text = user.createdAt.dayOfMonth.toString()
                    total_tasks.text = user.createdAt.dayOfWeek.toString()
                    tasks_completed.text = user.createdAt.year.toString()
                    most_used_category.text = user.createdAt.month.toString()

                    Glide
                        .with(this@ProfileFragment)
                        .load(user.avatar)
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .into(image)

                }
            } catch (exception: Exception) {
                Log.i("asd", "asd")
            }
        }
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

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
