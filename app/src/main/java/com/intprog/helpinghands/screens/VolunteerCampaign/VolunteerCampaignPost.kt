package com.intprog.helpinghands.screens.VolunteerCampaign

import android.os.Parcel
import android.os.Parcelable

data class VolunteerCampaignPost(
    val title: String,
    val category: String,
    val description: String,
    val startDate: String,
    val duration: String,
    val age: String,
    val imageUri: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeString(startDate)
        parcel.writeString(duration)
        parcel.writeString(age)
        parcel.writeString(imageUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VolunteerCampaignPost> {
        override fun createFromParcel(parcel: Parcel): VolunteerCampaignPost {
            return VolunteerCampaignPost(parcel)
        }

        override fun newArray(size: Int): Array<VolunteerCampaignPost?> {
            return arrayOfNulls(size)
        }
    }
}

