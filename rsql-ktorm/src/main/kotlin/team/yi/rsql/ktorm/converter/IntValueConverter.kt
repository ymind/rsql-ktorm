package team.yi.rsql.ktorm.converter

class IntValueConverter : NumberValueConverter<Int>() {
    override fun toNumber(s: String): Int? = s.toIntOrNull()
}
