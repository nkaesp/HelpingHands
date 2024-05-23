// FeaturedOpportunitiesAdapter.kt
package com.intprog.helpinghands.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intprog.helpinghands.R
import com.intprog.helpinghands.models.Campaign
import com.squareup.picasso.Picasso

class FeaturedOpportunitiesAdapter(
    private val campaigns: List<Campaign>,
    private val onViewDetailsClicked: (Campaign) -> Unit
) : RecyclerView.Adapter<FeaturedOpportunitiesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_featured_opportunities, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return campaigns.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val campaign = campaigns[position]
        holder.titleTextView.text = campaign.title
        Picasso.get().load(campaign.imageUri).into(holder.imageView)
        holder.viewDetailsButton.setOnClickListener {
            onViewDetailsClicked(campaign)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image1)
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetailsButton)
    }
}
