package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.time.YearMonth

class YearMonthValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            YearMonth.parse(it)
        }
    }
}
