package cs446.project.chameleon.data.model

data class Paint(
    val id: String,
    val brand: String,
    val name: String,
    val rgb: List<Int>

    // this below should be the new data class - keeping old ones now b/c changes to uis needed
//    val brand: String,
//    val url: String,
//    val name: String,
//    val id: String,
//    val rgb: RGB,
//    val hsl: HSL,
//    val labelRGB: String,
//    val labelHSL: String
)
