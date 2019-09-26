package com.gabrielmaz.poda.views.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.AuthController
import com.gabrielmaz.poda.helpers.hideKeyboard
import com.gabrielmaz.poda.helpers.textString
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.views.MainActivity
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class LoginFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val authController = AuthController()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_loading.setIndeterminateDrawable(FadingCircle())

        login_button.setOnClickListener {
            login_password.hideKeyboard()
            login_loading.visible()
            loginButton()
        }

        login_signup.setOnClickListener {
            signupButton()
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

    private fun loginButton() {
        val email = login_email.textString()
        val password = login_password.textString()

        if (email == "" || password == "") {
            Toast.makeText(activity, "Empty fields", Toast.LENGTH_SHORT).show()
        } else {
            launch(Dispatchers.IO) {
                try {
                    authController.login(email, password)
                    withContext(Dispatchers.Main) {
                        activity?.let {
                            it.startActivity(Intent(it, MainActivity::class.java))
                            it.finish()
                        }
                    }
                } catch (exception: Exception) {
                    Log.i("login", exception.message)
                }
            }

            listener?.onFragmentInteraction(login_email.textString(), login_password.textString())
        }
    }

    private fun signupButton() {
        listener?.onFragmentInteraction("", "")
    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(email: String, password: String)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
