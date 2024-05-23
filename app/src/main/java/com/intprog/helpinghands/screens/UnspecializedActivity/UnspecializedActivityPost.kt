package com.intprog.helpinghands.model

import android.os.Parcel
import android.os.Parcelable

data class UnspecializedActivityPost(
    val title: String,
    val noOfParticipants: String,
    val description: String,
    val imageUri: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(noOfParticipants)
        parcel.writeString(description)
        parcel.writeString(imageUri)
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
