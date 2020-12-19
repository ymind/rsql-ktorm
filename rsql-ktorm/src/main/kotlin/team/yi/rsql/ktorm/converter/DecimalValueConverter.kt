package team.yi.rsql.ktorm.converter

import java.math.BigDecimal

class DecimalValueConverter : NumberValueConverter<BigDecimal>() {
    override fun toNumber(s: String): BigDecimal? = s.toBigDecimalOrNull()
}
