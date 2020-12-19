package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.time.MonthDay

class MonthDayValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            MonthDay.parse(it)
        }
    }
}
