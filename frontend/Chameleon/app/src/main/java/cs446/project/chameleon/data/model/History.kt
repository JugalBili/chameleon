package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName
import java.time.format.DateTimeFormatter


data class History (
    val history: List<HistoryData>
)

data class HistoryData (
    @SerializedName("base_image") val baseImage: String,
    @SerializedName("last_accessed") val lastAccessed: Any, // TODO find the right date time format
    val colors: List<Color>
)
