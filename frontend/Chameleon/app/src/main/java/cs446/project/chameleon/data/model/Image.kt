package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName

data class Image (
    val uid: String,
    @SerializedName("processed_image_hash") val processedImageHash: String,
    val color: Color
)

data class Color (
    @SerializedName("paint_id") val paintId: String,
    val rgb: RGB
)

data class RGB (
    val r: Int,
    val g: Int,
    val b: Int
)

data class HSL (
    val h: Float,
    val s: Float,
    val l: Float
)
