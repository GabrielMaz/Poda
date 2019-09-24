package com.gabrielmaz.poda.views.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gabrielmaz.poda.R
import kotlinx.android.synthetic.main.fragment_login.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {

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

        login_button.setOnClickListener {
            loginButton()
        }

        login_signup.setOnClickListener {
            signupButton()
        }

        return inflater.inflate(R.layout.fragment_login, container, false)
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
        if (login_email.toString() == "" || login_password.toString() == "") {
            Toast.makeText(activity, "Empty fields", Toast.LENGTH_SHORT).show()
        } else {
            listener?.onFragmentInteraction(login_email.toString(), login_password.toString())
        }
    }

    private fun signupButton() {

    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(email: String, password: String)
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
