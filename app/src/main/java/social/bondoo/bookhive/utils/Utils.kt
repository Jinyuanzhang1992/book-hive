package social.bondoo.bookhive.utils

import android.icu.text.DateFormat
import android.util.Patterns.EMAIL_ADDRESS
import com.google.firebase.Timestamp

fun isEmailValid(email: String): Boolean {
    return EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    val passwordPattern =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&+=#^()\\-])[A-Za-z\\d@\$!%*?&+=#^()\\-]{8,}$"
    val passwordMatcher = Regex(passwordPattern)
    return passwordMatcher.matches(password)
}
//至少包含 8 个字符、一个大写字母、一个小写字母、一个数字和一个特殊字符。

fun passwordMatch(password: String, confirmPassword: String): Boolean {
    return password == confirmPassword
}

fun formatDate(timestamp: Timestamp): String {
    return  DateFormat.getDateInstance().format(timestamp.toDate()).split(",")[0]
}