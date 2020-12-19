package team.yi.rsql.ktorm.converter

class DoubleValueConverter : NumberValueConverter<Double>() {
    override fun toNumber(s: String): Double? = s.toDoubleOrNull()
}
