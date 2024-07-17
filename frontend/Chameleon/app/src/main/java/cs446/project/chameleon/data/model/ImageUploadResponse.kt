package cs446.project.chameleon.data.model

data class ImageUploadResponse (
    val originalImage: String,
    val processedImages: List<Image>
)