package com.intprog.helpinghands.screens.VolunteerCampaign

import android.os.Parcel
import android.os.Parcelable
import com.intprog.helpinghands.models.CampaignType

data class VolunteerCampaignPost(
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val noOfVolunteers: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val age: String = "",
    val location: String = "",
    val email: String? = "",
    val imageUri: String? = null,
    val type: CampaignType = CampaignType.VOLUNTEER,
    var documentId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        category = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        noOfVolunteers = parcel.readString() ?: "",
        startDate = parcel.readString() ?: "",
        endDate = parcel.readString() ?: "",
        age = parcel.readString() ?: "",
        location = parcel.readString() ?: "",
        imageUri = parcel.readString(),
        type = CampaignType.valueOf(parcel.readString() ?: CampaignType.VOLUNTEER.name)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeString(noOfVolunteers)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(age)
        parcel.writeString(location)
        parcel.writeString(imageUri)
        parcel.writeString(type.name)
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

