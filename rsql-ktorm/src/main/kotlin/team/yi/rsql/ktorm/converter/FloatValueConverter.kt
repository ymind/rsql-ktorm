package team.yi.rsql.ktorm.converter

class FloatValueConverter : NumberValueConverter<Float>() {
    override fun toNumber(s: String): Float? = s.toFloatOrNull()
}
