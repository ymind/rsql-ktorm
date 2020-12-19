package team.yi.rsql.ktorm.converter

class ShortValueConverter : NumberValueConverter<Short>() {
    override fun toNumber(s: String): Short? = s.toShortOrNull()
}
