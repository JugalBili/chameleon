package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName

data class Review (
    @SerializedName("paint_id") val paintId: String,
    val uid: String,
    val review: String,
    val timeStamp: Any,
    @SerializedName("image_hashes") val imageHashes: List<String>
)

data class ReviewOLD (
    val firstName: String,
    val lastName: String,
    val date: String,
    val reviewText: String
)
