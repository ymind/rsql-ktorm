package team.yi.rsql.ktorm.converter

class LongValueConverter : NumberValueConverter<Long>() {
    override fun toNumber(s: String): Long? = s.toLongOrNull()
}
