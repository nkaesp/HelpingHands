// FeaturedOpportunitiesAdapter.kt
package com.intprog.helpinghands.screens

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intprog.helpinghands.R
import com.intprog.helpinghands.models.Campaign
import com.intprog.helpinghands.models.CampaignType
import com.intprog.helpinghands.screens.DonationCampaign.DonationCampaignStatusPageActivity
import com.intprog.helpinghands.screens.UnspecializedActivity.UnspecializedActivityStatusPageActivity
import com.intprog.helpinghands.screens.VolunteerCampaign.VolunteerCampaignStatusPageActivity

class FeaturedOpportunitiesAdapter(private val campaigns: List<Campaign>) : RecyclerView.Adapter<FeaturedOpportunitiesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_featured_opportunities, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return campaigns.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val campaign = campaigns[position]
        holder.bind(campaign)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val image: ImageView = itemView.findViewById(R.id.image1)
        private val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetailsButton)
        private val bell: ImageButton = itemView.findViewById(R.id.bell)

        fun bind(campaign: Campaign) {
            title.text = campaign.title
            category.text = campaign.category
            Glide.with(itemView.context).load(campaign.imageUrl).into(image)

            viewDetailsButton.setOnClickListener {
                val context = it.context
                val intent = when (campaign.type) {
                    CampaignType.DONATION -> Intent(context, DonationCampaignStatusPageActivity::class.java)
                    CampaignType.UNSPECIALIZED -> Intent(context, UnspecializedActivityStatusPageActivity::class.java)
                    CampaignType.VOLUNTEER -> Intent(context, VolunteerCampaignStatusPageActivity::class.java)
                }
                intent.putExtra("campaignId", campaign.id)
                context.startActivity(intent)
            }

            // Handle bell button click if needed
        }
    }
}
