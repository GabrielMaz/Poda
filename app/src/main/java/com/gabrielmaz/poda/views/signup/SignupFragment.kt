package com.gabrielmaz.poda.views.signup

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
import com.gabrielmaz.poda.views.login.LoginFragment
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class SignupFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val authController = AuthController()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signup_loading.setIndeterminateDrawable(FadingCircle())

        signup_button.setOnClickListener {
            signup_password_confirmation.hideKeyboard()
            signup_loading.visible()
            signupButton()
        }

        signup_login.setOnClickListener {
            loginButton()
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

    private fun signupButton() {
        val name = signup_name.textString()
        val email = signup_email.textString()
        val password = signup_password.textString()
        val passwordConfirmation = signup_password_confirmation.textString()

        if (name == ""
            || email == ""
            || password == ""
            || passwordConfirmation == ""
        ) {

        } else if (password != passwordConfirmation) {

        } else {

            launch(Dispatchers.IO) {
                try {
                    authController.signup(name, email, password, passwordConfirmation)
                    withContext(Dispatchers.Main) {
                        activity?.let {
                            it.startActivity(Intent(it, MainActivity::class.java))
                            it.finish()
                        }
                    }
                } catch (exception: Exception) {
                    Log.i("signup", exception.message)
                }
            }


            listener?.onFragmentInteraction(
                signup_name.textString(),
                signup_email.textString(),
                signup_password.textString()
            )
        }
    }

    private fun loginButton() {
        listener?.onFragmentInteraction("", "", "")
    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(name: String, email: String, password: String)
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
