package com.intprog.helpinghands.screens.DonationCampaign

import android.os.Parcel
import android.os.Parcelable
import com.intprog.helpinghands.models.CampaignType

data class DonationCampaignPost(
    val title: String = "",
    val description: String = "",
    val amountNeeded: String = "",
    val category: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val contactMethod: String = "",
    val imageUri: String? = null,
    val type: CampaignType = CampaignType.DONATION,
    var documentId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        amountNeeded = parcel.readString() ?: "",
        category = parcel.readString() ?: "",
        fullName = parcel.readString() ?: "",
        email = parcel.readString() ?: "",
        phoneNumber = parcel.readString() ?: "",
        contactMethod = parcel.readString() ?: "",
        imageUri = parcel.readString(),
        type = CampaignType.valueOf(parcel.readString() ?: CampaignType.DONATION.name)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(amountNeeded)
        parcel.writeString(category)
        parcel.writeString(fullName)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeString(contactMethod)
        parcel.writeString(imageUri)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DonationCampaignPost> {
        override fun createFromParcel(parcel: Parcel): DonationCampaignPost {
            return DonationCampaignPost(parcel)
        }

        override fun newArray(size: Int): Array<DonationCampaignPost?> {
            return arrayOfNulls(size)
        }
    }
}
