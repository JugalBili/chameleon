package cs446.project.chameleon.data.model

data class Image (
    val uid: String,
    val processedImageHash: String,
    val color: Color
)

data class Color (
    val paintId: String,
    val rgb: RGB
)

data class RGB (
    val r: Int,
    val g: Int,
    val b: Int
)