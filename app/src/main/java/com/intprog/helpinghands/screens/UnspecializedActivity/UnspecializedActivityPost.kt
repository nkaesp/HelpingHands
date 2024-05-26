package com.intprog.helpinghands.model

import android.os.Parcel
import android.os.Parcelable
import com.intprog.helpinghands.models.CampaignType

data class UnspecializedActivityPost(
    val title: String = "",
    val noOfParticipants: String = "",
    val description: String = "",
    val imageUri: String? = null,
    val type: CampaignType = CampaignType.UNSPECIALIZED,
    var documentId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        noOfParticipants = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        imageUri = parcel.readString(),
        type = CampaignType.valueOf(parcel.readString() ?: CampaignType.UNSPECIALIZED.name)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(noOfParticipants)
        parcel.writeString(description)
        parcel.writeString(imageUri)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnspecializedActivityPost> {
        override fun createFromParcel(parcel: Parcel): UnspecializedActivityPost {
            return UnspecializedActivityPost(parcel)
        }

        override fun newArray(size: Int): Array<UnspecializedActivityPost?> {
            return arrayOfNulls(size)
        }
    }
}
