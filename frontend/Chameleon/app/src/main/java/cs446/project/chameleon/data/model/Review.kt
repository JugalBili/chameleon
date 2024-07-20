package cs446.project.chameleon.data.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Review (
    @SerializedName("paint_id") val paintId: String,
    val uid: String,
    val review: String,
    val timeStamp: Instant, // TODO: figure out what type this will actually take
    @SerializedName("image_hashes") val imageHashes: List<String>
)
