package com.gabrielmaz.poda.views.signup

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
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class SignupFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()
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
            signupClicked()
        }

        signup_login.setOnClickListener {
            loginClicked()
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

    private fun signupClicked() {
        signup_password_confirmation.hideKeyboard()

        val name = signup_name.textString()
        val email = signup_email.textString()
        val password = signup_password.textString()
        val passwordConfirmation = signup_password_confirmation.textString()

        if (name == ""
            || email == ""
            || password == ""
            || passwordConfirmation == ""
        ) {
            Toast.makeText(activity, "Empty fields", Toast.LENGTH_SHORT).show()
        } else if (password != passwordConfirmation) {
            Toast.makeText(activity, "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            signup_loading.visible()
            launch(Dispatchers.IO) {
                try {
                    authController.signup(name, email, password, passwordConfirmation)
                    withContext(Dispatchers.Main) {
                        listener?.onSignupSuccess(
                            signup_name.textString(),
                            signup_email.textString(),
                            signup_password.textString()
                        )
                        signup_loading.gone()
                    }
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        var toastMessage = R.string.connection_error
                        if (exception is HttpException && exception.code() == 422) {
                            toastMessage = R.string.invalid_signup_format
                        }
                        Toast.makeText(activity, toastMessage, Toast.LENGTH_LONG).show()
                        signup_loading.gone()
                    }
                }
            }
        }
    }

    private fun loginClicked() {
        listener?.onLoginClicked()
    }

    interface OnFragmentInteractionListener {
        fun onSignupSuccess(name: String, email: String, password: String)
        fun onLoginClicked()
    }
}
