package social.bondoo.bookhive.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import social.bondoo.bookhive.R

//@Preview(showBackground = true)
@Composable
fun IconWithName() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.library),
            contentDescription = "Icon with app name",
            modifier = Modifier.size(45.dp),
            tint = Color.Red
        )
        Text(
            text = "Book Hive",
            fontSize = 40.sp,
            color = Color.Red
        )
    }
}