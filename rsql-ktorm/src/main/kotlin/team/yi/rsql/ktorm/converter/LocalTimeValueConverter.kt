package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.time.LocalTime

class LocalTimeValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            LocalTime.parse(it)
        }
    }
}
