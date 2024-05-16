package com.intprog.helpinghands.screens.VolunteerCampaign

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.intprog.helpinghands.R

class AgeSpinnerAdapter(context: Context, private val ageChoices: Array<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ageChoices) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextColor(ContextCompat.getColor(context, R.color.black)) // Set text color
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) // Set text size
        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextColor(ContextCompat.getColor(context, R.color.black)) // Set text color
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) // Set text size
        return view
    }
}
