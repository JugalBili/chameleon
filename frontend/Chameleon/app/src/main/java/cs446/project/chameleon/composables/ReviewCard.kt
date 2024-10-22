package cs446.project.chameleon.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cs446.project.chameleon.composables.styling.ChameleonDivider
import cs446.project.chameleon.composables.styling.ChameleonText
import cs446.project.chameleon.data.model.Review
import cs446.project.chameleon.utils.BODY
import cs446.project.chameleon.utils.formatTimestamp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewCard(review: Review) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = review.timeStamp.toString().substring(0,10))
            }

            ChameleonDivider()
            ChameleonText(review.review, BODY)
        }
    }
}
