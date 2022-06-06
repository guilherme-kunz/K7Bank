package guilhermekunz.com.br.k7bank.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val FORMAT_DATE_TIME_TIMEZONE_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    fun Date.format(
        format: String
    ): String {

        val formatter = SimpleDateFormat(format, Locale.getDefault())

        return formatter.format(this)
    }

    fun formatDateDayMonth(date: String): String?{
        val dateFormat = SimpleDateFormat(FORMAT_DATE_TIME_TIMEZONE_STRING, Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(date).format("dd/MM")
        } catch (e: Exception) {
            null
        }
    }

}