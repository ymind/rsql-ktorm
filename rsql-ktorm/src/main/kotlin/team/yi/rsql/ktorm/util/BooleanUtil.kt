package team.yi.rsql.ktorm.util

object BooleanUtil {
    fun parse(value: String?, defaultValue: Boolean) = parse(value) ?: defaultValue

    fun parse(value: String?): Boolean? {
        return value?.trim()?.let {
            when {
                it == "1" -> true
                it.equals("yes", true) -> true
                it.equals("on", true) -> true
                it.equals("true", true) -> true
                else -> null
            }
        }
    }
}

fun String.toBoolOrNull() = BooleanUtil.parse(this)
fun String.toBoolOrDefault(defaultValue: Boolean = false) = BooleanUtil.parse(this, defaultValue)
