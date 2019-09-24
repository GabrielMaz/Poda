package com.gabrielmaz.poda.views.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.helpers.textString
import com.gabrielmaz.poda.views.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignupFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signup_button.setOnClickListener {
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
        if (signup_name.textString() == ""
            || login_email.textString() == ""
            || login_password.textString() == ""
        ) {
        } else {
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
