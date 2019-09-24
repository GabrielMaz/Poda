package com.gabrielmaz.poda.helpers

import android.view.View
import android.widget.EditText

fun EditText.textString() = text.toString()

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}