package com.intprog.helpinghands.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intprog.helpinghands.R

class FeaturedOpportunitiesAdapter: RecyclerView.Adapter<FeaturedOpportunitiesAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_featured_opportunities,parent,false)
            return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 25
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }
}