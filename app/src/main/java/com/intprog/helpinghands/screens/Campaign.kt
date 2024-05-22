package com.intprog.helpinghands.models

data class Campaign(
    val id: Int,
    val title: String,
    val category: String,
    val imageUrl: String,
    val type: CampaignType
)

enum class CampaignType {
    DONATION, UNSPECIALIZED, VOLUNTEER
}