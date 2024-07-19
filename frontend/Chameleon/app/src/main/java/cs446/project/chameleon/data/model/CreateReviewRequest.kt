package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName

data class CreateReviewRequest (
    @SerializedName("paint_id") val paintId: String,
    val review: String
)