package cs446.project.chameleon.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cs446.project.chameleon.data.model.HSL
import cs446.project.chameleon.data.model.Paint
import cs446.project.chameleon.data.model.RGB
import cs446.project.chameleon.data.paintsJson
import cs446.project.chameleon.data.paintsJsonObj
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getColour(rgb: RGB): Color {
    return Color(red = rgb.r, green = rgb.g, blue = rgb.b)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: Instant): String {
    return DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT).withZone(ZoneId.systemDefault()).format(timestamp)
}

fun getPaintById(paintId: String): Paint {
    val p = paintsJsonObj.optJSONObject(paintId)
        ?: return Paint("a","b","c","d", RGB(0,0,0), HSL(0f,0f,0f), "e", "f") // to do set default values

    val brand: String = p.optString("brand") ?: "NO BRAND FOUND"
    val url: String = p.optString("url") ?: "NO URL FOUND"
    val name: String = p.optString("name") ?: "NO NAME FOUND"
    val id: String = p.optString("id") ?: "NO ID FOUND"

    val rgbJson: JSONObject? = p.optJSONObject("rgb")
    val hslJson: JSONObject? = p.optJSONObject("hsl")

    val rgb: RGB
    val hsl: HSL

    if (rgbJson != null && hslJson != null) {
        rgb = RGB(rgbJson.optInt("r"), rgbJson.optInt("g"), rgbJson.optInt("b"))
        hsl = HSL(hslJson.optString("h").toFloat(), hslJson.optString("s").toFloat(), hslJson.optString("l").toFloat())
    } else {
        rgb = RGB(0, 0, 0)
        hsl = HSL(0f, 0f, 0f)
    }

    val labelRGB: String = p.optString("labelRGB") ?: "NO labelRGB FOUND"
    val labelHSL: String = p.optString("labelHSL") ?: "NO labelHSL FOUND"

    return Paint(brand, url, name, id, rgb, hsl, labelRGB, labelHSL)
}

fun getPaintList(): List<Paint> {
    val gson = Gson()
    val type = object : TypeToken<Map<String, Paint>>() {}.type
    val paintMap: Map<String, Paint> = gson.fromJson(paintsJson, type)
    val paints: List<Paint> = paintMap.values.toList()

    return paints
}
