package com.intprog.helpinghands.screens.DonationCampaign

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.intprog.helpinghands.R
import com.squareup.picasso.Picasso
import java.util.*

class DonationCampaignAdapter(
    context: Context,
    private val resourceId: Int,
    private val donationCampaigns: List<DonationCampaign>
) : ArrayAdapter<DonationCampaign>(context, resourceId, donationCampaigns) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val viewHolder: ViewHolder

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(resourceId, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        } else {
            viewHolder = itemView.tag as ViewHolder
        }

        val donationCampaign = donationCampaigns[position]
        viewHolder.titleTextView.text = donationCampaign.title
        // Load image using Picasso or any other image loading library
        Picasso.get().load(donationCampaign.imageUri).into(viewHolder.imageView)

        viewHolder.viewDetailsBtn.setOnClickListener {
            // Handle view details button click
            // You can navigate to another activity to display full details
        }

        return itemView!!
    }

    private class ViewHolder(view: View) {
        val titleTextView: TextView = view.findViewById(R.id.donationCampaignTitleTextView)
        val imageView: ImageView = view.findViewById(R.id.donationImageView)
        val viewDetailsBtn: Button = view.findViewById(R.id.viewDetailsBtn)
    }
}