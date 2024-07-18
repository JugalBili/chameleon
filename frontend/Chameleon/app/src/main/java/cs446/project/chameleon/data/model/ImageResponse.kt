package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName

data class ImageResponse (
    @SerializedName("original_image") val originalImage: String,
    @SerializedName("processed_images") val processedImages: List<Image>
)