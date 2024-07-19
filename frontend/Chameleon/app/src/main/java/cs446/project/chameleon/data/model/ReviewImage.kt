package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName

data class ReviewImage (
    @SerializedName("paint_id") val paintId: String,
    @SerializedName("image_hash") val imageHash: String
)
