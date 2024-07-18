package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName
import java.time.format.DateTimeFormatter


data class History (
    @SerializedName("base_image") val baseImage: String,
    @SerializedName("last_accessed") val lastAccessed: DateTimeFormatter, // is this the right format ?
    val colors: List<Color>
)
