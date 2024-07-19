package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName

data class Favorite (
    @SerializedName("paint_id") val paintId: String,
    val rgb: RGB
)
