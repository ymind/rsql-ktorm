package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.time.LocalDate

class LocalDateValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            LocalDate.parse(it)
        }
    }
}
