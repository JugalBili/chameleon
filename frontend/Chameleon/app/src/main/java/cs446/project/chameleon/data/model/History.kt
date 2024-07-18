package cs446.project.chameleon.data.model

import java.time.format.DateTimeFormatter


data class History (
    val baseImage: String,
    val lastAccessed: DateTimeFormatter, // is this the right format ?
    val colors: List<Color>
)
