package team.yi.rsql.ktorm.converter

import org.ktorm.schema.BaseTable
import java.sql.Time

class TimeValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            Time.valueOf(it)
        }
    }
}
