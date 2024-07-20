package cs446.project.chameleon.data.model

data class Paint(
    val brand: String,
    val url: String,
    val name: String,
    val id: String,
    val rgb: RGB,
    val hsl: HSL,
    val labelRGB: String,
    val labelHSL: String
)
