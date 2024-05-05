package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.R

class DonationCampaignAdapter(
    private val adapterContext: Context,
    private val donationCampaigns: List<DonationCampaign>
) : ArrayAdapter<DonationCampaign>(adapterContext, R.layout.donation_selection_page_item, donationCampaigns) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.donation_selection_page_item, parent, false)

        val titleTextView = view.findViewById<TextView>(R.id.donationCampaignTitleTextView)
        val imageView = view.findViewById<ImageView>(R.id.donationImageView)

        val donationCampaign = donationCampaigns[position]

        titleTextView.text = donationCampaign.title
        imageView.setImageResource(donationCampaign.imageResId)

        return view
    }
}
