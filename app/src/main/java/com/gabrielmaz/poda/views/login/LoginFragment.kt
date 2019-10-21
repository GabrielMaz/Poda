package com.gabrielmaz.poda.views.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.AuthController
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.hideKeyboard
import com.gabrielmaz.poda.helpers.textString
import com.gabrielmaz.poda.helpers.visible
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class LoginFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()
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
            loginClicked()
        }

        login_signup.setOnClickListener {
            signupClicked()
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
        job.cancel()
        listener = null
    }

    private fun loginClicked() {
        login_password.hideKeyboard()


        val email = login_email.textString()
        val password = login_password.textString()

        if (email == "" || password == "") {
            Toast.makeText(activity, "Empty fields", Toast.LENGTH_SHORT).show()
        } else {
            login_loading.visible()
            launch(Dispatchers.IO) {
                try {
                    authController.login(email, password)
                    withContext(Dispatchers.Main) {
                        listener?.onLoginSuccess()
                        login_loading.gone()
                    }
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
                        login_loading.gone()
                    }
                }
            }
        }
    }

    private fun signupClicked() {
        listener?.onSignupClicked()
    }

    interface OnFragmentInteractionListener {
        fun onLoginSuccess()
        fun onSignupClicked()
    }
}
