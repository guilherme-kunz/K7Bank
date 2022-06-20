package guilhermekunz.com.br.k7bank.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {

    const val FORMAT_DATE_TIME_TIMEZONE_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTimeFormatter(date: String, isDateCompleted: Boolean): String {
        val dateReplace = date.replace("Z", ".000Z")
        val formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME_TIMEZONE_STRING, Locale.ROOT)
        val dateFormatter = LocalDateTime.parse(dateReplace, formatter)
        val formatted: String = if (isDateCompleted) {
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss").toString()
        } else {
            DateTimeFormatter.ofPattern("dd/MM").toString()
        }

        return formatted.format(dateFormatter)
    }

}